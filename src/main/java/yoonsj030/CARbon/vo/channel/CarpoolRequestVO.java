package yoonsj030.CARbon.vo.channel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yoonsj030.CARbon.dto.channel.AttendeeInfoDTO;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class CarpoolRequestVO {
    private Long carpoolId;             // channelId

    private Long driverId;

    private List<AttendeeInfoDTO> attendeeInfoDTOList;
}
