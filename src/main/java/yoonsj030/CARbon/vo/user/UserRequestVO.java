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
public class UserRequestVO {
    private Long userId;

    private String realId;          // 회원가입, 로그인 시 입력하는 실제 ID

    private String password;

    private String name;

    private String nickname;

    private Boolean sex;            // true 남성, false 여성

    private String cellphone;

    private Date birthdayDate;

    private Boolean ownCar;

    private Boolean driving;

    private Double rating;

    private int ratingCnt;          // 평가한 인원

    private Integer point;

    private Integer carpoolCount;

    private Integer level;

    private Double totalCo2;            // 전체 탄소배출량
}
