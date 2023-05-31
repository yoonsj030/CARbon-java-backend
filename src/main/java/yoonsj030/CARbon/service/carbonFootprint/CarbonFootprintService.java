package yoonsj030.CARbon.service.carbonFootprint;

import yoonsj030.CARbon.vo.carbonFootprint.AnalysisResponseVO;
import yoonsj030.CARbon.dto.carbonFootprint.CreateCarbonFootprintDTO;
import yoonsj030.CARbon.vo.carbonFootprint.CarbonFootprintResponseVO;

import java.util.List;

public interface CarbonFootprintService {
    void createCarbonFootprint(CreateCarbonFootprintDTO createCarbonFootprintDTO);

    List<CarbonFootprintResponseVO> getCarbonFootprint(Long userId, int year);

    AnalysisResponseVO analysisCarbonFootprint(Long userId);
}
