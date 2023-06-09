package yoonsj030.CARbon.repository.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yoonsj030.CARbon.entity.post.Post;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByDeparturesContaining(String keyword);

    List<Post> findByArrivalsContaining(String keyword);
}
