package yoonsj030.CARbon.entity.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yoonsj030.CARbon.entity.carbonFootprint.CarbonFootprint;
import yoonsj030.CARbon.entity.channel.Channel;
import yoonsj030.CARbon.entity.post.Post;
import yoonsj030.CARbon.util.UserRole;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name = "id", nullable = false, unique = true)
    private String realId;          // 회원가입, 로그인 시 입력하는 실제 ID

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private Boolean sex;            // true 남성, false 여성

    @Column(nullable = false, unique = true)
    private String cellphone;

    @Column(name = "birthday_date", nullable = false)
    private String birthdayDate;

    @Column(name = "own_car", nullable = false)
    private Boolean ownCar;

    @Column(nullable = false)
    private Boolean driving;

    @Column
    private Double rating;

    @Column(name = "rating_cnt")
    private Integer ratingCnt;          // 평가한 인원

    @Column(nullable = false)
    private Integer point;

    @Column(name = "carpool_count", nullable = false)
    private Integer carpoolCount;

    @Column(nullable = false)
    private Integer level;

    @Column(nullable = false)
    private Double totalCo2;            // 전체 탄소배출량

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToMany(mappedBy = "user")
    private List<CarbonFootprint> carbonFootprintList;

    @OneToMany(mappedBy = "user")
    private List<Post> postList;

    @OneToMany(mappedBy = "user")
    private List<UserHasChannel> channelList;

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public void updateOwnCar(boolean ownCar) {
        this.ownCar = ownCar;
    }

    public void updateDriving(boolean driving) {
        this.driving = driving;
    }

    public void setPostList(Post post) {
        if(this.postList == null) {
            this.postList = new ArrayList<>();
        }

        this.postList.add(post);
    }

    public void setChannelList(UserHasChannel userHasChannel) {
        if(this.channelList == null) {
            this.channelList = new ArrayList<>();
        }

        this.channelList.add(userHasChannel);
    }

    public void setCarbonFootprintList(CarbonFootprint carbonFootprint) {
        if(this.carbonFootprintList == null) {
            this.carbonFootprintList = new ArrayList<>();
        }

        this.carbonFootprintList.add(carbonFootprint);
    }

    public void setRating(double rating, int ratingCnt) {
        this.rating = rating;
        this.ratingCnt = ratingCnt;
    }

    public void setPoint(double distance) {
        this.point = point + (int)distance;
    }

    public void setTotalCo2(double totalCo2) {
        this.totalCo2 = this.totalCo2 + totalCo2;
    }
}
