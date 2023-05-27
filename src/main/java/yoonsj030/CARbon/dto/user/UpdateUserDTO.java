package yoonsj030.CARbon.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class UpdateUserDTO {
    private Long userId;

    private String password;

    private String name;

    private String nickname;

    private String cellphone;

    private Boolean ownCar;

    private Boolean driving;
}
