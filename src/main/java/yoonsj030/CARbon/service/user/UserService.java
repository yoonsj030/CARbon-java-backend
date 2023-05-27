package yoonsj030.CARbon.service.user;

import org.springframework.security.core.userdetails.UserDetailsService;
import yoonsj030.CARbon.dto.user.JoinUserDTO;
import yoonsj030.CARbon.dto.user.UpdateUserDTO;
import yoonsj030.CARbon.vo.user.TargetProfileResponseVO;
import yoonsj030.CARbon.vo.user.UserProfileResponseVO;

import java.security.Principal;

public interface UserService extends UserDetailsService {
    boolean authorizationUser(Principal principal, Long userId);

    int existsByJoinUserDTO(JoinUserDTO joinUserDTO);

    void joinUser(JoinUserDTO joinUserDTO);

    Long login(String realId);

    int existsByUpdateUserDTO(UpdateUserDTO updateUserDTO);

    UserProfileResponseVO updateUser(UpdateUserDTO updateUserDTO);

    UserProfileResponseVO getMyProfile(Long userId);

    TargetProfileResponseVO getTargetProfile(Long targetUserId);

    void deleteUser(Long userId);
}
