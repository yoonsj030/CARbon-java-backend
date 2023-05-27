package yoonsj030.CARbon.dto.channel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class AttendeeInfoDTO {
    private Long userId;

    private String nickname;

    private Boolean attendance;
}

