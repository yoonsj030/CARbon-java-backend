package yoonsj030.CARbon.dto.channel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class UpdateChannelDTO {
    private Long channelId;

    private Long userId;

    private String departures;

    private Double departuresLatitude;

    private Double departuresLongitude;

    private String arrivals;

    private Double arrivalsLatitude;

    private Double arrivalsLongitude;
}
