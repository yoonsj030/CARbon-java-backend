package yoonsj030.CARbon.vo.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class PostResponseVO {
    private Long postId;

    private String title;

    private String content;

    private String departures;

    private Double departuresLatitude;

    private Double departuresLongitude;

    private String arrivals;

    private Double arrivalsLatitude;

    private Double arrivalsLongitude;

    private Integer personnel;

    private Integer curPersonnel;

    private Boolean regular;

    private String carpoolDate;

    private String driverNickname;

    private String hostNickname;

    private Long hostId;

    private Long channelId;
}
