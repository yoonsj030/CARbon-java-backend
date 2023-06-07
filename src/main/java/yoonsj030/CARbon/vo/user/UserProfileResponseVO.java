package yoonsj030.CARbon.vo.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class UserProfileResponseVO {
    private Long userId;

    private String name;

    private String nickname;

    private Boolean sex;            // true 남성, false 여성

    private String cellphone;

    private String birthdayDate;

    private Boolean ownCar;

    private Boolean driving;

    private Double rating;

    private int ratingCnt;          // 평가한 인원

    private Integer point;

    private Integer carpoolCount;

    private Integer level;

    private Double totalCo2;            // 전체 탄소배출량
}
