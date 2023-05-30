package yoonsj030.CARbon.dto.carbonFootprint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class MonthEmissionsDTO {
    private Integer month;

    private List<DayEmissionsDTO> dayList = new ArrayList<>();

    public void setMonth(int month) {
        this.month = month;
    }
}
