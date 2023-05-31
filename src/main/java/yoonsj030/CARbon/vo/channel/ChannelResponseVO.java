package yoonsj030.CARbon.vo.channel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yoonsj030.CARbon.dto.user.ParticipateUserDTO;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ChannelResponseVO {
    private Long channelId;

    private Long hostId;

    private String hostNickname;

    private Long driverId;

    private String driverNickname;

    private String departures;

    private Double departuresLatitude;

    private Double departuresLongitude;

    private String arrivals;

    private Double arrivalsLatitude;

    private Double arrivalsLongitude;

    private Integer personnel;

    private Integer curPersonnel;

    private String content;

    private Boolean regular;

    private List<ParticipateUserDTO> userHasChannelList;

    public void setDriver(Long driverId, String driverNickname) {
        this.driverId = driverId;
        this.driverNickname = driverNickname;
    }
}
