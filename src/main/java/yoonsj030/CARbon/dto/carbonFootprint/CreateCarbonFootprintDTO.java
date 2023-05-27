package yoonsj030.CARbon.dto.carbonFootprint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yoonsj030.CARbon.entity.user.User;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class CreateCarbonFootprintDTO {
    private User user;

    private Date carpoolDate;

    private Double distance;

    private Double emissions;
}
