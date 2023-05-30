package yoonsj030.CARbon.dto.carbonFootprint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class DayEmissionsDTO {
    private Integer day;

    private Double emissions;

    public void setDay(int day) {
        this.day = day;
    }

    public void setEmissions(double emissions) {
        this.emissions = emissions;
    }
}
