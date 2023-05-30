package yoonsj030.CARbon.vo.carbonFootprint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yoonsj030.CARbon.dto.carbonFootprint.MonthEmissionsDTO;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class CarbonFootprintResponseVO {
    private Integer year;

    private List<MonthEmissionsDTO> monthList = new ArrayList<>();

    public void setYear(int year) {
        this.year = year;
    }
}
