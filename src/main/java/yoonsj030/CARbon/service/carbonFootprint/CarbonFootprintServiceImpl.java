package yoonsj030.CARbon.service.carbonFootprint;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import yoonsj030.CARbon.dto.carbonFootprint.CreateCarbonFootprintDTO;
import yoonsj030.CARbon.entity.carbonFootprint.CarbonFootprint;
import yoonsj030.CARbon.entity.user.User;
import yoonsj030.CARbon.repository.carbonFootprint.CarbonFootprintRepository;
import yoonsj030.CARbon.repository.user.UserRepository;
import yoonsj030.CARbon.vo.carbonFootprint.CarbonFootprintResponseVO;

import java.util.List;

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
    public CarbonFootprintResponseVO getCarbonFootprint(Long userId, int year) {
        return null;
    }
}
