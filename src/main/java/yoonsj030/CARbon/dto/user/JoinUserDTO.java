package yoonsj030.CARbon.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class JoinUserDTO {
    private String realId;          // 회원가입, 로그인 시 입력하는 실제 ID

    private String password;

    private String name;

    private String nickname;

    private Boolean sex;            // true 남성, false 여성

    private String cellphone;

    private String birthdayDate;

    private Boolean ownCar;

    private Boolean driving;
}
