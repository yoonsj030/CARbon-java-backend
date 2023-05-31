package yoonsj030.CARbon.service.carbonFootprint;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import yoonsj030.CARbon.vo.carbonFootprint.AnalysisResponseVO;
import yoonsj030.CARbon.dto.carbonFootprint.CreateCarbonFootprintDTO;
import yoonsj030.CARbon.dto.carbonFootprint.DayEmissionsDTO;
import yoonsj030.CARbon.dto.carbonFootprint.MonthEmissionsDTO;
import yoonsj030.CARbon.entity.carbonFootprint.CarbonFootprint;
import yoonsj030.CARbon.entity.user.User;
import yoonsj030.CARbon.repository.carbonFootprint.CarbonFootprintRepository;
import yoonsj030.CARbon.repository.user.UserRepository;
import yoonsj030.CARbon.vo.carbonFootprint.CarbonFootprintResponseVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Slf4j
@Service
public class CarbonFootprintServiceImpl implements CarbonFootprintService {
    private final CarbonFootprintRepository carbonFootprintRepository;
    private final UserRepository userRepository;

    @Override
    public void createCarbonFootprint(CreateCarbonFootprintDTO createCarbonFootprintDTO) {
        String[] carpoolDate = createCarbonFootprintDTO.getCarpoolDate().toString().split("-");

        int year = Integer.parseInt(carpoolDate[0]),
                month = Integer.parseInt(carpoolDate[1]), day = Integer.parseInt(carpoolDate[2]);

        CarbonFootprint carbonFootprint = CarbonFootprint.builder()
                .year(year)
                .month(month)
                .day(day)
                .distance(createCarbonFootprintDTO.getDistance())
                .emissions(createCarbonFootprintDTO.getEmissions())
                .user(createCarbonFootprintDTO.getUser())
                .build();

        try {
            carbonFootprintRepository.save(carbonFootprint);
        } catch (RuntimeException e) {
            log.error("Carbon Footprint 저장 중 오류 발생: " + e.getMessage());
            throw new RuntimeException("Footprint 저장 중 오류 발생");
        }

        User user = createCarbonFootprintDTO.getUser();

        user.setCarbonFootprintList(carbonFootprint);
        user.setTotalCo2(createCarbonFootprintDTO.getEmissions());

        try {
            userRepository.save(user);
        } catch (RuntimeException e) {
            log.error("User 저장 중 오류 발생: " + e.getMessage());
            throw new RuntimeException("User 저장 중 오류 발생");
        }
    }

    @Override
    public List<CarbonFootprintResponseVO> getCarbonFootprint(Long userId, int year) {
        List<Object[]> yearlyEmissionsByDate = carbonFootprintRepository.getYearlyEmissionsByUser(userId, year);

        Map<Integer, CarbonFootprintResponseVO> carbonFootprintMap = new HashMap<>();

        for (Object[] data : yearlyEmissionsByDate) {
            Integer month = (Integer) data[1];
            Integer day = (Integer) data[2];
            Double emissions = (Double) data[3];

            // 연도별 객체 생성 또는 기존 객체 가져오기
            CarbonFootprintResponseVO carbonFootprintResponseVO = carbonFootprintMap.getOrDefault(year, new CarbonFootprintResponseVO());
            carbonFootprintResponseVO.setYear(year);

            // 월별 객체 생성 또는 기존 객체 가져오기
            MonthEmissionsDTO monthObject = new MonthEmissionsDTO();
            monthObject.setMonth(month);

            // 일별 객체 생성 또는 기존 객체 가져오기
            DayEmissionsDTO dayObject = new DayEmissionsDTO();
            dayObject.setDay(day);
            dayObject.setEmissions(emissions);

            // 일별 객체를 월별 객체에 추가
            monthObject.getDayList().add(dayObject);

            // 월별 객체를 연도별 객체에 추가
            carbonFootprintResponseVO.getMonthList().add(monthObject);

            // 연도별 객체를 맵에 저장
            carbonFootprintMap.put(year, carbonFootprintResponseVO);
        }

        return new ArrayList<>(carbonFootprintMap.values());
    }

    @Override
    public AnalysisResponseVO analysisCarbonFootprint(Long userId) {
        User user = userRepository
                .findById(userId).orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

        List<User> userList = userRepository.findByLevel(user.getLevel());
        double standardCo2 = 0;

        for (User target : userList) {
            standardCo2 = standardCo2 +  target.getTotalCo2();
        }
        standardCo2 = standardCo2 / userList.size();

        AnalysisResponseVO analysisResponseVO = AnalysisResponseVO.builder()
                .level(user.getLevel())
                .totalCo2(user.getTotalCo2())
                .levelStandardCo2(standardCo2)
                .build();

        return analysisResponseVO;
    }
}
