package yoonsj030.CARbon.vo.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class TargetProfileResponseVO {
    private String nickname;

    private Boolean sex;            // true 남성, false 여성

    private Boolean ownCar;

    private Boolean driving;

    private Double rating;

    private Integer carpoolCount;

    private Double totalCo2;            // 전체 탄소배출량
}
