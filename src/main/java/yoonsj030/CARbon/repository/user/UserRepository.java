package yoonsj030.CARbon.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yoonsj030.CARbon.entity.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByRealId(String realId);

    boolean existsByNickname(String nickname);

    boolean existsByCellphone(String cellphone);

    User findByRealId(String realId);

    User findByNickname(String hostNickname);
}
