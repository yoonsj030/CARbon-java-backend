package yoonsj030.CARbon.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yoonsj030.CARbon.entity.channel.Channel;
import yoonsj030.CARbon.entity.user.User;
import yoonsj030.CARbon.entity.user.UserHasChannel;

import java.util.List;

@Repository
public interface UserHasChannelRepository extends JpaRepository<UserHasChannel, Long> {

    UserHasChannel findByChannelAndUser(Channel channel, User driver);
}
