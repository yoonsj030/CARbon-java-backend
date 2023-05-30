package yoonsj030.CARbon.controller.carbonFootprint;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import yoonsj030.CARbon.service.carbonFootprint.CarbonFootprintService;
import yoonsj030.CARbon.service.user.UserService;
import yoonsj030.CARbon.util.BaseResponse;
import yoonsj030.CARbon.vo.carbonFootprint.CarbonFootprintResponseVO;
import yoonsj030.CARbon.vo.channel.ChannelResponseVO;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/carbonFootprints")
@RestController
public class CarbonFoortprintController {
    private final CarbonFootprintService carbonFootprintService;
    private final UserService userService;

    @ApiOperation("탄소 배출량 조회")
    @GetMapping
    public ResponseEntity<BaseResponse<List<CarbonFootprintResponseVO>>> getCarbonFootprint(@RequestParam Long userId,
                                                                                  @RequestParam int year,
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
            List<CarbonFootprintResponseVO> carbonFootprintResponseVOList = carbonFootprintService
                    .getCarbonFootprint(userId, year);

            BaseResponse baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("탄소 배출량 조회 성공!")
                    .data(carbonFootprintResponseVOList)
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
