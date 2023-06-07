package yoonsj030.CARbon.entity.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yoonsj030.CARbon.entity.channel.Channel;
import yoonsj030.CARbon.entity.user.User;

import javax.persistence.*;
import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id", nullable = false, unique = true)
    private Long postId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String departures;

    @Column(name = "departures_latitude", nullable = false)
    private Double departuresLatitude;

    @Column(name = "departures_longitude", nullable = false)
    private Double departuresLongitude;

    @Column(nullable = false)
    private String arrivals;

    @Column(name = "arrivals_latitude", nullable = false)
    private Double arrivalsLatitude;

    @Column(name = "arrivals_longitude", nullable = false)
    private Double arrivalsLongitude;

    @Column(nullable = false)
    private Integer personnel;

    @Column(nullable = false)
    private Boolean regular;

    @Column(name = "carpool_date", nullable = false)
    private String carpoolDate;

    @Column(name = "driver_id")
    private Long driverId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Channel channel;

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
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

    public void updatePersonnel(Integer personnel) {
        this.personnel = personnel;
    }

    public  void updateRegular(Boolean regular) {
        this.regular = regular;
    }

    public void updateCarpoolDate(String carpoolDate) {
        this.carpoolDate = carpoolDate;
    }
}
