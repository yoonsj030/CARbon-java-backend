package yoonsj030.CARbon.vo.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class PostRequestVO {
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

    private Boolean regular;

    private Date carpoolDate;

    private Long driverId;

    private Long userId;            // 작성자
}
