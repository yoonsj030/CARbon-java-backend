package yoonsj030.CARbon.entity.channel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yoonsj030.CARbon.entity.post.Post;
import yoonsj030.CARbon.entity.user.UserHasChannel;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "channels")
public class Channel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "channel_id", nullable = false, unique = true)
    private Long channelId;

    @Column(name = "host_nickname")
    private String hostNickname;

    @Column(name = "driver_nickname")
    private String driverNickname;

    @Column(name = "cur_personnel", nullable = false)
    private Integer curPersonnel;

    @OneToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL)
    private List<UserHasChannel> userList;

    public void setDriverNickname(String driverNickname) {
        this.driverNickname = driverNickname;
    }

    public void setUserList(UserHasChannel userHasChannel) {
        if(this.userList == null) {
            this.userList = new ArrayList<>();
        }

        this.userList.add(userHasChannel);
    }

    public void updateCurPersonnel() {
        this.curPersonnel++;
    }
}
