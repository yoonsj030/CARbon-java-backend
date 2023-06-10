package yoonsj030.CARbon.entity.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yoonsj030.CARbon.entity.channel.Channel;
import yoonsj030.CARbon.entity.coordinate.Coordinate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "user_has_channels")
public class UserHasChannel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_has_channels_id", nullable = false, unique = true)
    private Long userHasChannelsId;

    @Column
    private String departures;

    @Column(name = "departures_latitude")
    private Double departuresLatitude;

    @Column(name = "departures_longitude")
    private Double departuresLongitude;

    @Column
    private String arrivals;

    @Column(name = "arrivals_latitude")
    private Double arrivalsLatitude;

    @Column(name = "arrivals_longitude")
    private Double arrivalsLongitude;

    @Column
    private Double distance;

    @Column(nullable = false)
    private Boolean attendance;

    @Column
    private Double rating;

    @Column
    private Integer ratingCnt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @OneToMany(mappedBy = "userHasChannel")
    private List<Coordinate> coordinateList;

    public void setAttendance(boolean attendance) {
        this.attendance = attendance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public void setRating(double rating, int ratingCnt) {
        this.rating = rating;
        this.ratingCnt = ratingCnt;
    }

    public void setCoordinateList(Coordinate coordinate) {
        if(this.coordinateList == null) {
            this.coordinateList = new ArrayList<>();
        }

        this.coordinateList.add(coordinate);
    }

    public void updateDepartures(String departures) {
        this.departures = departures;
    }

    public void updateDeparturesLatitude(Double departuresLatitude) {
        this.departuresLatitude = departuresLatitude;
    }

    public void updateDeparturesLongitude(Double departuresLongitude) {
        this.departuresLongitude = departuresLongitude;
    }

    public void updateArrivals(String arrivals) {
        this.arrivals = arrivals;
    }

    public void updateArrivalsLatitude(Double arrivalsLatitude) {
        this.arrivalsLatitude = arrivalsLatitude;
    }

    public void updateArrivalsLongitude(Double arrivalsLongitude) {
        this.arrivalsLongitude = arrivalsLongitude;
    }
}
