package yoonsj030.CARbon.controller.channel;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yoonsj030.CARbon.dto.channel.JoinCarpoolDTO;
import yoonsj030.CARbon.dto.user.ParticipateUserDTO;
import yoonsj030.CARbon.service.carbonFootprint.CarbonFootprintService;
import yoonsj030.CARbon.service.channel.ChannelService;
import yoonsj030.CARbon.service.user.UserService;
import yoonsj030.CARbon.util.BaseResponse;
import yoonsj030.CARbon.vo.channel.CarpoolMeasureRequestVO;
import yoonsj030.CARbon.vo.channel.CarpoolRequestVO;
import yoonsj030.CARbon.vo.channel.ChannelRequestVO;
import yoonsj030.CARbon.vo.channel.ChannelResponseVO;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/carpools")
@RestController
public class ChannelController {
    private final ChannelService channelService;
    private final UserService userService;

    @ApiOperation("카풀 참여")
    @PostMapping
    public ResponseEntity<BaseResponse> joinCarpool(@RequestBody ChannelRequestVO channelRequestVO,
                                                    Principal principal) {
        if(principal == null){
            BaseResponse baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.FORBIDDEN)
                    .message("인가되지 않은 사용자입니다.")
                    .build();

            return new ResponseEntity(baseResponse, HttpStatus.BAD_REQUEST);
        }

        if(!userService.authorizationUser(principal, channelRequestVO.getUserId())) {
            BaseResponse baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.FORBIDDEN)
                    .message("잘못된 접근입니다.")
                    .build();

            return new ResponseEntity(baseResponse, HttpStatus.BAD_REQUEST);
        }

        JoinCarpoolDTO joinCarpoolDTO = JoinCarpoolDTO.builder()
                .channelId(channelRequestVO.getChannelId())
                .userId(channelRequestVO.getUserId())
                .departures(channelRequestVO.getDepartures())
                .departuresLatitude(channelRequestVO.getDeparturesLatitude())
                .departuresLongitude(channelRequestVO.getDeparturesLongitude())
                .arrivals(channelRequestVO.getArrivals())
                .arrivalsLatitude(channelRequestVO.getArrivalsLatitude())
                .arrivalsLongitude(channelRequestVO.getArrivalsLongitude())
                .build();

        if(channelService.existsByJoinCarpoolRequest(joinCarpoolDTO)) {
            BaseResponse baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("이미 참여 중인 카풀입니다!")
                    .build();

            return new ResponseEntity<>(baseResponse, HttpStatus.OK);
        }

        try {
            channelService.joinCarpool(joinCarpoolDTO);

            BaseResponse baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("카풀 참여 성공!")
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

    @ApiOperation("참여 중인 카풀 조회")
    @GetMapping
    public ResponseEntity<BaseResponse<List<ChannelResponseVO>>> getAllCarpools(@RequestParam Long userId,
                                                                                     Principal principal) {
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
            List<ChannelResponseVO> channelResponseVOList = channelService.getAllCarpools(userId);

            BaseResponse baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("카풀 조회 성공!")
                    .data(channelResponseVOList)
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

    @ApiOperation("카풀 참가자 조회")
    @GetMapping("/participation")
    public ResponseEntity<BaseResponse<List<ParticipateUserDTO>>> getAllParticipants(@RequestParam Long channelId,
                                                                                     Principal principal) {
        if(principal == null){
            BaseResponse baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.FORBIDDEN)
                    .message("인가되지 않은 사용자입니다.")
                    .build();

            return new ResponseEntity(baseResponse, HttpStatus.BAD_REQUEST);
        }

        try {
            List<ParticipateUserDTO> participateUserDTOList = channelService.getAllParticipants(channelId);

            BaseResponse baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("카풀 참가자 조회 성공!")
                    .data(participateUserDTOList)
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

    @ApiOperation("카풀 출석부")
    @PostMapping("/attendee")
    public ResponseEntity<Long> attendCarpool(@RequestBody CarpoolRequestVO carpoolRequestVO,
                                                      Principal principal) {
        if(principal == null){
            BaseResponse baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.FORBIDDEN)
                    .message("인가되지 않은 사용자입니다.")
                    .build();

            return new ResponseEntity(baseResponse, HttpStatus.BAD_REQUEST);
        }

        if(!userService.authorizationUser(principal, carpoolRequestVO.getDriverId())) {
            BaseResponse baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.FORBIDDEN)
                    .message("잘못된 접근입니다.")
                    .build();

            return new ResponseEntity(baseResponse, HttpStatus.BAD_REQUEST);
        }

        try {
            channelService.attendCarpool(carpoolRequestVO);

//            BaseResponse baseResponse = BaseResponse.builder()
//                    .httpStatus(HttpStatus.OK)
//                    .message("카풀 출석 성공!")
//                    .build();

            return new ResponseEntity<>(carpoolRequestVO.getCarpoolId(), HttpStatus.OK);
        } catch (Exception e) {
//            BaseResponse baseResponse = BaseResponse.builder()
//                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .message("서버 오류: " + e.getMessage())
//                    .build();

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation("카풀 시작")
    @GetMapping("/{carpoolId}/{userId}")
    public ResponseEntity<Long> startCarpool(@PathVariable Long carpoolId, @PathVariable Long userId,
                                                     Principal principal) {
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
            HttpStatus httpStatus = channelService.startCarpool(carpoolId, userId);
            BaseResponse baseResponse;

            if(httpStatus == HttpStatus.OK) {
//                baseResponse = BaseResponse.builder()
//                        .httpStatus(httpStatus)
//                        .message("카풀 시작 성공!")
//                        .build();

                return new ResponseEntity<>(carpoolId, httpStatus);
            } else {
//                baseResponse = BaseResponse.builder()
//                        .httpStatus(httpStatus)
//                        .message("카풀 출석 안 된 상태!")
//                        .build();

                return new ResponseEntity<>(carpoolId, httpStatus);
            }

//            return new ResponseEntity<>(baseResponse, httpStatus);
        } catch (ServiceException e) {
//            BaseResponse baseResponse = BaseResponse.builder()
//                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .message("서버 오류: " + e.getMessage())
//                    .build();

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation("카풀 측정")
    @PostMapping("/measurement")
    public ResponseEntity<Long> measureCarpool(@RequestBody CarpoolMeasureRequestVO carpoolMeasureRequestVO) {
        try {
            channelService.measureCarpool(carpoolMeasureRequestVO);

//            BaseResponse baseResponse = BaseResponse.builder()
//                    .httpStatus(HttpStatus.OK)
//                    .message("카풀 측정 성공!")
//                    .build();

            return new ResponseEntity<>(carpoolMeasureRequestVO.getCarpoolId(), HttpStatus.OK);
        } catch (ServiceException e) {
//            BaseResponse baseResponse = BaseResponse.builder()
//                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .message("서버 오류: " + e.getMessage())
//                    .build();

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation("카풀 채널 총 개수")
    @GetMapping("/count")
    public ResponseEntity<BaseResponse<Integer>> getChannelCount(Principal principal) {
        if(principal == null){
            BaseResponse baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.FORBIDDEN)
                    .message("인가되지 않은 사용자입니다.")
                    .build();

            return new ResponseEntity(baseResponse, HttpStatus.BAD_REQUEST);
        }

        try {
            int channelCount = channelService.getChannelCount();

            BaseResponse baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("카풀 전체 개수 조회 성공!")
                    .data(channelCount)
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
