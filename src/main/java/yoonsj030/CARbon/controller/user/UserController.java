package yoonsj030.CARbon.controller.user;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yoonsj030.CARbon.dto.user.JoinUserDTO;
import yoonsj030.CARbon.dto.user.UpdateUserDTO;
import yoonsj030.CARbon.service.user.UserService;
import yoonsj030.CARbon.util.BaseResponse;
import yoonsj030.CARbon.vo.user.LoginResponseVO;
import yoonsj030.CARbon.vo.user.TargetProfileResponseVO;
import yoonsj030.CARbon.vo.user.UserRequestVO;
import yoonsj030.CARbon.vo.user.UserProfileResponseVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@RequiredArgsConstructor
@Slf4j
@RestController
public class UserController {
    private final UserService userService;

    @ApiOperation("회원가입")
    @PostMapping("/join")
    public ResponseEntity<BaseResponse> joinUser(@RequestBody UserRequestVO userRequestVO) {
        JoinUserDTO joinUserDTO = JoinUserDTO.builder()
                .realId(userRequestVO.getRealId())
                .password(userRequestVO.getPassword())
                .name(userRequestVO.getName())
                .nickname(userRequestVO.getNickname())
                .sex(userRequestVO.getSex())
                .cellphone(userRequestVO.getCellphone())
                .birthdayDate(userRequestVO.getBirthdayDate())
                .ownCar(userRequestVO.getOwnCar())
                .driving(userRequestVO.getDriving())
                .build();

        int response = userService.existsByJoinUserDTO(joinUserDTO);

        if(response == 1) {
            BaseResponse baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.CONFLICT)
                    .message("이미 사용 중인 ID입니다.")
                    .build();

            return new ResponseEntity<>(baseResponse, HttpStatus.CONFLICT);
        }

        if(response == 2) {
            BaseResponse baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.CONFLICT)
                    .message("이미 사용 중인 닉네임입니다.")
                    .build();

            return new ResponseEntity<>(baseResponse, HttpStatus.CONFLICT);
        }

        if(response == 3) {
            BaseResponse baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.CONFLICT)
                    .message("이미 사용 중인 전화번호입니다.")
                    .build();

            return new ResponseEntity<>(baseResponse, HttpStatus.CONFLICT);
        }

        try {
            userService.joinUser(joinUserDTO);

            BaseResponse baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("회원가입 성공!")
                    .build();

            return new ResponseEntity<>(baseResponse, HttpStatus.OK);
        } catch (Exception e) {
            BaseResponse baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("서버 오류: " + e.getMessage())
                    .build();

            return new ResponseEntity<>(baseResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation("로그인")
    @GetMapping("/")
    public ResponseEntity<LoginResponseVO> login(HttpServletRequest request, HttpServletResponse response,
                                                 Principal principal) {
        try {
            LoginResponseVO loginResponseVO = userService.login(principal.getName());
//            BaseResponse baseResponse = BaseResponse.builder()
//                    .httpStatus(HttpStatus.OK)
//                    .message("로그인 성공!")
//                    .data(userId)
//                    .build();

            return new ResponseEntity<>(loginResponseVO, HttpStatus.OK);
        } catch (Exception e) {
//            BaseResponse baseResponse = BaseResponse.builder()
//                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .message("서버 오류: " + e.getMessage())
//                    .build();

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation("회원 정보 수정")
    @PatchMapping("/api/users")
    public ResponseEntity<BaseResponse<UserProfileResponseVO>> updateUser(@RequestBody UserRequestVO userRequestVO,
                                                                      Principal principal) {
        if(principal == null){
            BaseResponse baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.FORBIDDEN)
                    .message("인가되지 않은 사용자입니다.")
                    .build();

            return new ResponseEntity(baseResponse, HttpStatus.BAD_REQUEST);
        }

        if(!userService.authorizationUser(principal, userRequestVO.getUserId())) {
            BaseResponse baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.FORBIDDEN)
                    .message("잘못된 접근입니다.")
                    .build();

            return new ResponseEntity(baseResponse, HttpStatus.BAD_REQUEST);
        }

        UpdateUserDTO updateUserDTO = UpdateUserDTO.builder()
                .userId(userRequestVO.getUserId())
                .password(userRequestVO.getPassword())
                .name(userRequestVO.getName())
                .nickname(userRequestVO.getNickname())
                .cellphone(userRequestVO.getCellphone())
                .ownCar(userRequestVO.getOwnCar())
                .driving(userRequestVO.getDriving())
                .build();

        int response = userService.existsByUpdateUserDTO(updateUserDTO);

        if(response == 1) {
            BaseResponse baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.CONFLICT)
                    .message("이미 사용 중인 닉네임입니다.")
                    .build();

            return new ResponseEntity<>(baseResponse, HttpStatus.CONFLICT);
        }

        if(response == 2) {
            BaseResponse baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.CONFLICT)
                    .message("이미 사용 중인 전화번호입니다.")
                    .build();

            return new ResponseEntity<>(baseResponse, HttpStatus.CONFLICT);
        }

        try {
            UserProfileResponseVO userProfileResponseVO = userService.updateUser(updateUserDTO);

            BaseResponse baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("회원 정보 수정 성공!")
                    .data(userProfileResponseVO)
                    .build();

            return new ResponseEntity<>(baseResponse, HttpStatus.OK);
        } catch (Exception e) {
            BaseResponse baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("서버 오류: " + e.getMessage())
                    .build();

            return new ResponseEntity<>(baseResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation("회원 프로필 조회")
    @GetMapping("/api/users/{targetUserId}")
    public ResponseEntity<BaseResponse> getProfile(@PathVariable Long targetUserId,
                                                 @RequestParam Long userId, Principal principal) {
        if(principal == null){
            BaseResponse baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.FORBIDDEN)
                    .message("인가되지 않은 사용자입니다.")
                    .build();

            return new ResponseEntity(baseResponse, HttpStatus.BAD_REQUEST);
        }

        if(targetUserId == userId) { // 본인 프로필 조회
            if(!userService.authorizationUser(principal, userId)) { // 다른 경우
                BaseResponse baseResponse = BaseResponse.builder()
                        .httpStatus(HttpStatus.FORBIDDEN)
                        .message("잘못된 접근입니다.")
                        .build();

                return new ResponseEntity(baseResponse, HttpStatus.BAD_REQUEST);
            }

            try {
                UserProfileResponseVO userProfileResponseVO = userService.getMyProfile(userId);

                BaseResponse baseResponse = BaseResponse.builder()
                        .httpStatus(HttpStatus.OK)
                        .message("내 프로필 조회 성공!")
                        .data(userProfileResponseVO)
                        .build();

                return new ResponseEntity<>(baseResponse, HttpStatus.OK);
            } catch (Exception e) {
                BaseResponse baseResponse = BaseResponse.builder()
                        .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                        .message("서버 오류: " + e.getMessage())
                        .build();

                return new ResponseEntity<>(baseResponse, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else { // 타인 프로필 조회
            try {
                TargetProfileResponseVO targetProfileResponseVO = userService.getTargetProfile(targetUserId);

                BaseResponse baseResponse = BaseResponse.builder()
                        .httpStatus(HttpStatus.OK)
                        .message("타인 프로필 조회 성공!")
                        .data(targetProfileResponseVO)
                        .build();

                return new ResponseEntity<>(baseResponse, HttpStatus.OK);
            } catch (Exception e) {
                BaseResponse baseResponse = BaseResponse.builder()
                        .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                        .message("서버 오류: " + e.getMessage())
                        .build();

                return new ResponseEntity<>(baseResponse, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @ApiOperation("회원 탈퇴")
    @DeleteMapping("/api/users")
    public ResponseEntity<BaseResponse> deleteUser(@RequestParam Long userId, Principal principal) {
        if(principal == null){
            BaseResponse baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.FORBIDDEN)
                    .message("인가되지 않은 사용자입니다.")
                    .build();

            return new ResponseEntity(baseResponse, HttpStatus.BAD_REQUEST);
        }

        if(!userService.authorizationUser(principal, userId)) {
            BaseResponse baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.FORBIDDEN)
                    .message("잘못된 접근입니다.")
                    .build();

            return new ResponseEntity(baseResponse, HttpStatus.BAD_REQUEST);
        }

        try {
            userService.deleteUser(userId);

            BaseResponse baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.NO_CONTENT)
                    .message("회원 탈퇴 성공!")
                    .build();

            return new ResponseEntity<>(baseResponse, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            BaseResponse baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("서버 오류: " + e.getMessage())
                    .build();

            return new ResponseEntity<>(baseResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
