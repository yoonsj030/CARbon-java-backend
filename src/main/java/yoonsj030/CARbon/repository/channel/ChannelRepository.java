package yoonsj030.CARbon.repository.channel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yoonsj030.CARbon.entity.channel.Channel;
import yoonsj030.CARbon.entity.post.Post;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, Long> {

    Channel findByPost(Post post);
}
