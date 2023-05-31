package yoonsj030.CARbon.dto.carbonFootprint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class AnalysisDTO {
    private Integer level;

    private Double totalCo2;

    private Double levelStandardCo2;
}
