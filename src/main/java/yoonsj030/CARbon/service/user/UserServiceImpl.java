package yoonsj030.CARbon.service.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yoonsj030.CARbon.dto.user.JoinUserDTO;
import yoonsj030.CARbon.dto.user.UpdateUserDTO;
import yoonsj030.CARbon.entity.user.User;
import yoonsj030.CARbon.repository.user.UserRepository;
import yoonsj030.CARbon.util.UserRole;
import yoonsj030.CARbon.vo.user.TargetProfileResponseVO;
import yoonsj030.CARbon.vo.user.UserProfileResponseVO;

import java.security.Principal;

@AllArgsConstructor
@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String realId) throws UsernameNotFoundException {           // 로그인 시점 호출
        User user = userRepository.findByRealId(realId);

        if(user == null) {
            throw new RuntimeException("존재하지 않는 사용자입니다.");
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getRealId())
                .password(user.getPassword())
                .roles(user.getRole().toString())
                .build();
    }

    @Override
    public boolean authorizationUser(Principal principal, Long userId) {
        User principalUser = userRepository.findByRealId(principal.getName());

        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

        if(principalUser == user) {
            return true;
        }
        return false;
    }

    @Override
    public int existsByJoinUserDTO(JoinUserDTO joinUserDTO) {
        if(userRepository.existsByRealId(joinUserDTO.getRealId())) {
            return 1;
        } else if(userRepository.existsByNickname(joinUserDTO.getNickname())) {
            return 2;
        } else if(userRepository.existsByCellphone(joinUserDTO.getCellphone())) {
            return 3;
        } else {
            return 0;
        }
    }

    @Override
    public void joinUser(JoinUserDTO joinUserDTO) {
        String password = bCryptPasswordEncoder.encode(joinUserDTO.getPassword());

        User user = User.builder()
                .realId(joinUserDTO.getRealId())
                .password(password)
                .name(joinUserDTO.getName())
                .nickname(joinUserDTO.getNickname())
                .sex(joinUserDTO.getSex())
                .cellphone(joinUserDTO.getCellphone())
                .birthdayDate(joinUserDTO.getBirthdayDate())
                .ownCar(joinUserDTO.getOwnCar())
                .driving(joinUserDTO.getDriving())
                .rating(0.0)
                .ratingCnt(0)
                .point(0)
                .carpoolCount(0)
                .level(1)
                .totalCo2(0.0)
                .role(UserRole.USER)
                .build();

        try {
            userRepository.save(user);
        } catch (RuntimeException e) {
            log.error("User 저장 중 오류 발생: " + e.getMessage());
            throw new RuntimeException("User 저장 중 오류 발생");
        }
    }

    @Override
    public Long login(String realId) {
        User user = userRepository.findByRealId(realId);

        if(user == null) {
            throw new RuntimeException("존재하지 않는 사용자입니다.");
        }

        return user.getUserId();
    }

    @Override
    public int existsByUpdateUserDTO(UpdateUserDTO updateUserDTO) {
        if(updateUserDTO.getNickname() != null) {
            if(userRepository.existsByNickname(updateUserDTO.getNickname())) {
                return 1;
            } else {
                return 0;
            }
        }

        if(updateUserDTO.getCellphone() != null) {
            if(userRepository.existsByCellphone(updateUserDTO.getCellphone())) {
                return 2;
            } else {
                return 0;
            }
        }

        return 0;
    }

    @Override
    public UserProfileResponseVO updateUser(UpdateUserDTO updateUserDTO) {
        User user = userRepository
                .findById(updateUserDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

        if(updateUserDTO.getPassword() != null) {
            String password = bCryptPasswordEncoder.encode(updateUserDTO.getPassword());
            user.updatePassword(password);
        }

        if(updateUserDTO.getName() != null) {
            user.updateName(updateUserDTO.getName());
        }

        if(updateUserDTO.getNickname() != null) {
            user.updateNickname(updateUserDTO.getNickname());
        }

        if(updateUserDTO.getCellphone() != null) {
            user.updateCellphone(updateUserDTO.getCellphone());
        }

        if(updateUserDTO.getOwnCar() != null) {
            user.updateOwnCar(updateUserDTO.getOwnCar());
        }

        if(updateUserDTO.getDriving() != null) {
            user.updateDriving(updateUserDTO.getDriving());
        }

        try {
            userRepository.save(user);
        } catch (RuntimeException e) {
            log.error("User 정보 수정 중 오류 발생: " + e.getMessage());
            throw new RuntimeException("User 정부 수정 중 오류 발생");
        }

        UserProfileResponseVO userProfileResponseVO = UserProfileResponseVO.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .nickname(user.getNickname())
                .sex(user.getSex())
                .cellphone(user.getCellphone())
                .birthdayDate(user.getBirthdayDate())
                .ownCar(user.getOwnCar())
                .driving(user.getDriving())
                .rating(user.getRating())
                .ratingCnt(user.getRatingCnt())
                .point(user.getPoint())
                .carpoolCount(user.getCarpoolCount())
                .level(user.getLevel())
                .totalCo2(user.getTotalCo2())
                .build();

        return userProfileResponseVO;
    }

    @Override
    public UserProfileResponseVO getMyProfile(Long userId) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

        UserProfileResponseVO userProfileResponseVO = UserProfileResponseVO.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .nickname(user.getNickname())
                .sex(user.getSex())
                .cellphone(user.getCellphone())
                .birthdayDate(user.getBirthdayDate())
                .ownCar(user.getOwnCar())
                .driving(user.getDriving())
                .rating(user.getRating())
                .ratingCnt(user.getRatingCnt())
                .point(user.getPoint())
                .carpoolCount(user.getCarpoolCount())
                .level(user.getLevel())
                .totalCo2(user.getTotalCo2())
                .build();

        return userProfileResponseVO;
    }

    @Override
    public TargetProfileResponseVO getTargetProfile(Long targetUserId) {
        User user = userRepository
                .findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

        TargetProfileResponseVO targetProfileResponseVO = TargetProfileResponseVO.builder()
                .nickname(user.getNickname())
                .sex(user.getSex())
                .ownCar(user.getOwnCar())
                .driving(user.getDriving())
                .rating(user.getRating())
                .carpoolCount(user.getCarpoolCount())
                .totalCo2(user.getTotalCo2())
                .build();

        return targetProfileResponseVO;
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

        try {
            userRepository.deleteById(userId);
        } catch (RuntimeException e) {
            log.error("회원 탈퇴 중 오류 발생: " + e.getMessage());
            throw new RuntimeException("회원 탈퇴 중 오류 발생");
        }
    }
}
