package yoonsj030.CARbon.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ParticipateUserDTO {
    private Long userId;

    private String nickname;

    private String departures;

    private Double departuresLatitude;

    private Double departuresLongitude;

    private String arrivals;

    private Double arrivalsLatitude;

    private Double arrivalsLongitude;

    private Double rating;
}
