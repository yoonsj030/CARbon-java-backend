package yoonsj030.CARbon.vo.channel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yoonsj030.CARbon.vo.coordinate.AllPoints;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class CarpoolMeasureRequestVO {
    private Long carpoolId;             // channelId

    private Long userId;

    private Double distance;            // 측정 거리

    private List<AllPoints> allPointsList;              // 이동 좌표

    private List<ParticipantsRating> participantsRatingList;            // 참가자에 대한 평가
}
