package yoonsj030.CARbon.service.carbonFootprint;

import yoonsj030.CARbon.dto.carbonFootprint.CreateCarbonFootprintDTO;
import yoonsj030.CARbon.vo.carbonFootprint.CarbonFootprintResponseVO;

public interface CarbonFootprintService {
    void createCarbonFootprint(CreateCarbonFootprintDTO createCarbonFootprintDTO);

    CarbonFootprintResponseVO getCarbonFootprint(Long userId, int year);
}
