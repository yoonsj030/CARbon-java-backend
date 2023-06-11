package yoonsj030.CARbon.service.post;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import yoonsj030.CARbon.dto.post.CreatePostDTO;
import yoonsj030.CARbon.dto.post.UpdatePostDTO;
import yoonsj030.CARbon.entity.channel.Channel;
import yoonsj030.CARbon.entity.post.Post;
import yoonsj030.CARbon.entity.user.User;
import yoonsj030.CARbon.entity.user.UserHasChannel;
import yoonsj030.CARbon.repository.channel.ChannelRepository;
import yoonsj030.CARbon.repository.post.PostRepository;
import yoonsj030.CARbon.repository.user.UserHasChannelRepository;
import yoonsj030.CARbon.repository.user.UserRepository;
import yoonsj030.CARbon.vo.post.PostResponseVO;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class PostServiceImpl implements PostService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ChannelRepository channelRepository;
    private final UserHasChannelRepository userHasChannelRepository;

    @Override
    public PostResponseVO createPost(CreatePostDTO createPostDTO) {
        User user = userRepository
                .findById(createPostDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

        Post post = Post.builder()
                .title(createPostDTO.getTitle())
                .content(createPostDTO.getContent())
                .departures(createPostDTO.getDepartures())
                .departuresLatitude(createPostDTO.getDeparturesLatitude())
                .departuresLongitude(createPostDTO.getDeparturesLongitude())
                .arrivals(createPostDTO.getArrivals())
                .arrivalsLatitude(createPostDTO.getArrivalsLatitude())
                .arrivalsLongitude(createPostDTO.getArrivalsLongitude())
                .personnel(createPostDTO.getPersonnel())
                .regular(createPostDTO.getRegular())
                .carpoolDate(createPostDTO.getCarpoolDate())
                .user(user)
                .build();

        Channel channel = Channel.builder()
                .hostNickname(user.getNickname())
                .curPersonnel(1)
                .post(post)
                .build();

        if(createPostDTO.getDriverId() != null) {
            post.setDriverId(createPostDTO.getDriverId());
            channel.setDriverNickname(user.getNickname());
        }

        user.setPostList(post);

        UserHasChannel userHasChannel = UserHasChannel.builder()
                .departures(createPostDTO.getDepartures())
                .departuresLatitude(createPostDTO.getDeparturesLatitude())
                .departuresLongitude(createPostDTO.getDeparturesLongitude())
                .arrivals(createPostDTO.getArrivals())
                .arrivalsLatitude(createPostDTO.getArrivalsLatitude())
                .arrivalsLongitude(createPostDTO.getArrivalsLongitude())
                .attendance(false)
                .user(user)
                .channel(channel)
                .build();

        channel.setUserList(userHasChannel);
        user.setChannelList(userHasChannel);

        try {
            userRepository.save(user);
        } catch (RuntimeException e) {
            log.error("User 저장 중 오류 발생: " + e.getMessage());
            throw new RuntimeException("User 저장 중 오류 발생");
        }

        try {
            postRepository.save(post);
        } catch (RuntimeException e) {
            log.error("Post 저장 중 오류 발생: " + e.getMessage());
            throw new RuntimeException("Post 저장 중 오류 발생");
        }

        try {
            channelRepository.save(channel);
        } catch (RuntimeException e) {
            log.error("Channel 저장 중 오류 발생: " + e.getMessage());
            throw new RuntimeException("Channel 저장 중 오류 발생");
        }

        try {
            userHasChannelRepository.save(userHasChannel);
        } catch (RuntimeException e) {
            log.error("UserHasChannel 저장 중 오류 발생: " + e.getMessage());
            throw new RuntimeException("UserHasChannel 저장 중 오류 발생");
        }

        PostResponseVO postResponseVO = PostResponseVO.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent())
                .departures(post.getDepartures())
                .departuresLatitude(post.getDeparturesLatitude())
                .departuresLongitude(post.getDeparturesLongitude())
                .arrivals(post.getArrivals())
                .arrivalsLatitude(post.getArrivalsLatitude())
                .arrivalsLongitude(post.getArrivalsLongitude())
                .personnel(post.getPersonnel())
                .curPersonnel(channel.getCurPersonnel())
                .regular(post.getRegular())
                .curPersonnel(channel.getCurPersonnel())
                .carpoolDate(post.getCarpoolDate())
                .driverNickname(channel.getDriverNickname())
                .hostNickname(channel.getHostNickname())
                .hostId(post.getUser().getUserId())
                .channelId(channel.getChannelId())
                .build();

        return postResponseVO;
    }

    @Override
    public List<PostResponseVO> getAllPosts(int pageNumber) {
        Pageable pageable = PageRequest.of(Math.toIntExact(pageNumber - 1), 10,
                Sort.by("postId").ascending());
        Page<Post> postPage = postRepository.findAll(pageable);

        if(postPage == null || postPage.isEmpty()) {
            throw new RuntimeException("Post 전체 조회 중 오류 발생");
        }

        List<PostResponseVO> postResponseVOList = new ArrayList<>();

        for(Post post : postPage) {
            PostResponseVO postResponseVO = PostResponseVO.builder()
                    .postId(post.getPostId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .departures(post.getDepartures())
                    .departuresLatitude(post.getDeparturesLatitude())
                    .departuresLongitude(post.getDeparturesLongitude())
                    .arrivals(post.getArrivals())
                    .arrivalsLatitude(post.getArrivalsLatitude())
                    .arrivalsLongitude(post.getArrivalsLongitude())
                    .personnel(post.getPersonnel())
                    .curPersonnel(post.getChannel().getCurPersonnel())
                    .regular(post.getRegular())
                    .carpoolDate(post.getCarpoolDate())
                    .driverNickname(post.getChannel().getDriverNickname())
                    .hostNickname(post.getChannel().getHostNickname())
                    .hostId(post.getUser().getUserId())
                    .channelId(post.getChannel().getChannelId())
                    .build();

            postResponseVOList.add(postResponseVO);
        }

        Collections.reverse(postResponseVOList);

        return postResponseVOList;
    }

    @Override
    public PostResponseVO getPost(Long postId) {
        Post post = postRepository
                .findById(postId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 게시글입니다."));

        PostResponseVO postResponseVO = PostResponseVO.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent())
                .departures(post.getDepartures())
                .departuresLatitude(post.getDeparturesLatitude())
                .departuresLongitude(post.getDeparturesLongitude())
                .arrivals(post.getArrivals())
                .arrivalsLatitude(post.getArrivalsLatitude())
                .arrivalsLongitude(post.getArrivalsLongitude())
                .personnel(post.getPersonnel())
                .curPersonnel(post.getChannel().getCurPersonnel())
                .regular(post.getRegular())
                .carpoolDate(post.getCarpoolDate())
                .driverNickname(post.getChannel().getDriverNickname())
                .hostNickname(post.getChannel().getHostNickname())
                .hostId(post.getUser().getUserId())
                .channelId(post.getChannel().getChannelId())
                .build();

        return postResponseVO;
    }

    @Override
    public List<PostResponseVO> searchPosts(String keyword) {
        if(keyword == null) {
            throw new RuntimeException("검색어가 없습니다.");
        }

        List<Post> postsByDepartures = postRepository.findByDeparturesContaining(keyword);
        List<Post> postsByArrivals = postRepository.findByArrivalsContaining(keyword);

        if((postsByDepartures == null || postsByDepartures.isEmpty()) &&
                (postsByArrivals == null || postsByArrivals.isEmpty())) {
            throw new RuntimeException("'" + keyword + "'가 포함된 Post가 없습니다.");
        }

        List<PostResponseVO> postResponseVOList = new ArrayList<>();

        for(Post post : postsByDepartures) {
            PostResponseVO postResponseVO = PostResponseVO.builder()
                    .postId(post.getPostId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .departures(post.getDepartures())
                    .departuresLatitude(post.getDeparturesLatitude())
                    .departuresLongitude(post.getDeparturesLongitude())
                    .arrivals(post.getArrivals())
                    .arrivalsLatitude(post.getArrivalsLatitude())
                    .arrivalsLongitude(post.getArrivalsLongitude())
                    .personnel(post.getPersonnel())
                    .regular(post.getRegular())
                    .curPersonnel(post.getChannel().getCurPersonnel())
                    .carpoolDate(post.getCarpoolDate())
                    .driverNickname(post.getChannel().getDriverNickname())
                    .hostNickname(post.getChannel().getHostNickname())
                    .hostId(post.getUser().getUserId())
                    .channelId(post.getChannel().getChannelId())
                    .build();

            postResponseVOList.add(postResponseVO);
        }

        for(Post post : postsByArrivals) {
            if(postResponseVOList.contains(post)) {
                continue;
            }

            PostResponseVO postResponseVO = PostResponseVO.builder()
                    .postId(post.getPostId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .departures(post.getDepartures())
                    .departuresLatitude(post.getDeparturesLatitude())
                    .departuresLongitude(post.getDeparturesLongitude())
                    .arrivals(post.getArrivals())
                    .arrivalsLatitude(post.getArrivalsLatitude())
                    .arrivalsLongitude(post.getArrivalsLongitude())
                    .personnel(post.getPersonnel())
                    .regular(post.getRegular())
                    .curPersonnel(post.getChannel().getCurPersonnel())
                    .carpoolDate(post.getCarpoolDate())
                    .driverNickname(post.getChannel().getDriverNickname())
                    .hostNickname(post.getChannel().getHostNickname())
                    .hostId(post.getUser().getUserId())
                    .channelId(post.getChannel().getChannelId())
                    .build();

            postResponseVOList.add(postResponseVO);
        }

        Collections.reverse(postResponseVOList);

        return postResponseVOList;
    }

    @Override
    public PostResponseVO updatePost(UpdatePostDTO updatePostDTO) {
        Post post = postRepository
                .findById(updatePostDTO.getPostId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 Post입니다."));

        Channel channel = channelRepository.findByPost(post);

        if(updatePostDTO.getTitle() != null) {
            post.updateTitle(updatePostDTO.getTitle());
        }

        if(updatePostDTO.getContent() != null) {
            post.updateContent(updatePostDTO.getContent());
        }

        if(updatePostDTO.getDepartures() != null) {
            post.updateDepartures(updatePostDTO.getDepartures());
        }

        if(updatePostDTO.getDeparturesLatitude() != null) {
            post.updateDeparturesLatitude(updatePostDTO.getDeparturesLatitude());
        }

        if(updatePostDTO.getDeparturesLongitude() != null) {
            post.updateDeparturesLongitude(updatePostDTO.getDeparturesLongitude());
        }

        if(updatePostDTO.getArrivals() != null) {
            post.updateArrivals(updatePostDTO.getArrivals());
        }

        if(updatePostDTO.getArrivalsLatitude() != null) {
            post.updateArrivalsLatitude(updatePostDTO.getArrivalsLatitude());
        }

        if(updatePostDTO.getArrivalsLongitude() != null) {
            post.updateArrivalsLongitude(updatePostDTO.getArrivalsLongitude());
        }

        if(updatePostDTO.getPersonnel() != null) {
            post.updatePersonnel(updatePostDTO.getPersonnel());
        }

        if(updatePostDTO.getRegular() != null) {
            post.updateRegular(updatePostDTO.getRegular());
        }

        if(updatePostDTO.getCarpoolDate() != null) {
            post.updateCarpoolDate(updatePostDTO.getCarpoolDate());
        }

        if(updatePostDTO.getDriverId() != null) {
            User driver = userRepository
                    .findById(updatePostDTO.getDriverId())
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 User입니다."));

            UserHasChannel userHasChannel = userHasChannelRepository.findByChannelAndUser(channel, driver);

            if(userHasChannel == null) {
                throw new RuntimeException("카풀에 참여하지 않은 User입니다.");
            }

            post.setDriverId(driver.getUserId());
            channel.setDriverNickname(driver.getNickname());
        }

        try {
            postRepository.save(post);
        } catch (DataAccessException e) {
            log.error("Post 수정 중 오류 발생: " + e.getMessage());
            throw new ServiceException("Post 수정 중 오류 발생");
        }

        try {
            channelRepository.save(channel);
        } catch (DataAccessException e) {
            log.error("Channel 수정 중 오류 발생: " + e.getMessage());
            throw new ServiceException("Channel 수정 중 오류 발생");
        }

        PostResponseVO postResponseVO = PostResponseVO.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent())
                .departures(post.getDepartures())
                .departuresLatitude(post.getDeparturesLatitude())
                .departuresLongitude(post.getDeparturesLongitude())
                .arrivals(post.getArrivals())
                .arrivalsLatitude(post.getArrivalsLatitude())
                .arrivalsLongitude(post.getArrivalsLongitude())
                .personnel(post.getPersonnel())
                .curPersonnel(post.getChannel().getCurPersonnel())
                .regular(post.getRegular())
                .carpoolDate(post.getCarpoolDate())
                .driverNickname(channel.getDriverNickname())
                .hostNickname(channel.getHostNickname())
                .hostId(post.getUser().getUserId())
                .channelId(channel.getChannelId())
                .build();

        return postResponseVO;
    }

    @Override
    public void deletePost(Long postId) {
        try {
            postRepository.deleteById(postId);
        } catch (DataAccessException e) {
            log.error("Post 삭제 중 오류 발생: " + e.getMessage());
            throw new ServiceException("Post 삭제 중 오류 발생");
        }
    }
}
