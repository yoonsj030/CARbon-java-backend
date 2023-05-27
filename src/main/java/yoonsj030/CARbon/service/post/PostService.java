package yoonsj030.CARbon.service.post;

import yoonsj030.CARbon.dto.post.CreatePostDTO;
import yoonsj030.CARbon.dto.post.UpdatePostDTO;
import yoonsj030.CARbon.vo.post.PostResponseVO;

import java.util.List;

public interface PostService {
    PostResponseVO createPost(CreatePostDTO createPostDTO);

    List<PostResponseVO> getAllPosts(int pageNumber);

    PostResponseVO getPost(Long postId);

    List<PostResponseVO> searchPosts(String keyword);

    PostResponseVO updatePost(UpdatePostDTO updatePostDTO);

    void deletePost(Long postId);
}
