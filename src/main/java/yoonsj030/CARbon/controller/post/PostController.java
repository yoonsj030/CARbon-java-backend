package yoonsj030.CARbon.controller.post;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yoonsj030.CARbon.dto.post.CreatePostDTO;
import yoonsj030.CARbon.dto.post.UpdatePostDTO;
import yoonsj030.CARbon.service.post.PostService;
import yoonsj030.CARbon.service.user.UserService;
import yoonsj030.CARbon.util.BaseResponse;
import yoonsj030.CARbon.vo.post.PostRequestVO;
import yoonsj030.CARbon.vo.post.PostResponseVO;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/posts")
@RestController
public class PostController {
    private final PostService postService;
    private final UserService userService;

    @ApiOperation("게시글 작성")
    @PostMapping
    public ResponseEntity<BaseResponse<PostResponseVO>> createPost(@RequestBody PostRequestVO postRequestVO,
                                                                   Principal principal) {
        if(principal == null){
            BaseResponse baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.FORBIDDEN)
                    .message("인가되지 않은 사용자입니다.")
                    .build();

            return new ResponseEntity(baseResponse, HttpStatus.BAD_REQUEST);
        }

        if(!userService.authorizationUser(principal, postRequestVO.getUserId())) {
            BaseResponse baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.FORBIDDEN)
                    .message("잘못된 접근입니다.")
                    .build();

            return new ResponseEntity(baseResponse, HttpStatus.BAD_REQUEST);
        }

        CreatePostDTO createPostDTO = CreatePostDTO.builder()
                .title(postRequestVO.getTitle())
                .content(postRequestVO.getContent())
                .departures(postRequestVO.getDepartures())
                .departuresLatitude(postRequestVO.getDeparturesLatitude())
                .departuresLongitude(postRequestVO.getDeparturesLongitude())
                .arrivals(postRequestVO.getArrivals())
                .arrivalsLatitude(postRequestVO.getArrivalsLatitude())
                .arrivalsLongitude(postRequestVO.getArrivalsLongitude())
                .personnel(postRequestVO.getPersonnel())
                .regular(postRequestVO.getRegular())
                .carpoolDate(postRequestVO.getCarpoolDate())
                .driverId(postRequestVO.getDriverId())
                .userId(postRequestVO.getUserId())
                .build();

        try {
            PostResponseVO postResponseVO = postService.createPost(createPostDTO);

            BaseResponse baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.CREATED)
                    .message("게시글 작성 성공!")
                    .data(postResponseVO)
                    .build();

            return new ResponseEntity<>(baseResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            BaseResponse baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("서버 오류: " + e.getMessage())
                    .build();

            return new ResponseEntity<>(baseResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation("게시글 전체 조회")
    @GetMapping("/lists/{pageNumber}")
    public ResponseEntity<BaseResponse<List<PostResponseVO>>> getAllPosts(@PathVariable int pageNumber,
                                                                          Principal principal) {
        if(principal == null){
            BaseResponse baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.FORBIDDEN)
                    .message("인가되지 않은 사용자입니다.")
                    .build();

            return new ResponseEntity(baseResponse, HttpStatus.BAD_REQUEST);
        }

        try {
            List<PostResponseVO> postResponseVOList = postService.getAllPosts(pageNumber);

            BaseResponse baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("게시글 전체 조회 성공!")
                    .data(postResponseVOList)
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

    @ApiOperation("게시글 단건 조회")
    @GetMapping("/{postId}")
    public ResponseEntity<BaseResponse<PostResponseVO>> getPost(@PathVariable Long postId, Principal principal) {
        if(principal == null){
            BaseResponse baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.FORBIDDEN)
                    .message("인가되지 않은 사용자입니다.")
                    .build();

            return new ResponseEntity(baseResponse, HttpStatus.BAD_REQUEST);
        }

        try {
            PostResponseVO postResponseVO = postService.getPost(postId);

            BaseResponse baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("게시글 단건 조회 성공!")
                    .data(postResponseVO)
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

    @ApiOperation("게시글 키워드 검색 조회")
    @GetMapping
    public ResponseEntity<BaseResponse<List<PostResponseVO>>> searchPosts(@RequestParam String keyword,
                                                                          Principal principal) {
        if(principal == null){
            BaseResponse baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.FORBIDDEN)
                    .message("인가되지 않은 사용자입니다.")
                    .build();

            return new ResponseEntity(baseResponse, HttpStatus.BAD_REQUEST);
        }

        try {
            List<PostResponseVO> postResponseVOList = postService.searchPosts(keyword);

            BaseResponse baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("게시글 키워드 검색 조회 성공!")
                    .data(postResponseVOList)
                    .build();

            return new ResponseEntity<>(baseResponse, HttpStatus.OK);
        }  catch (Exception e) {
            BaseResponse baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("서버 오류: " + e.getMessage())
                    .build();

            return new ResponseEntity<>(baseResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation("게시글 수정")
    @PatchMapping
    public ResponseEntity<BaseResponse<PostResponseVO>> updatePost (@RequestBody PostRequestVO postRequestVO,
                                                                    Principal principal) {
        if(principal == null){
            BaseResponse baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.FORBIDDEN)
                    .message("인가되지 않은 사용자입니다.")
                    .build();

            return new ResponseEntity(baseResponse, HttpStatus.BAD_REQUEST);
        }

        if(!userService.authorizationUser(principal, postRequestVO.getUserId())) {
            BaseResponse baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.FORBIDDEN)
                    .message("잘못된 접근입니다.")
                    .build();

            return new ResponseEntity(baseResponse, HttpStatus.BAD_REQUEST);
        }

        UpdatePostDTO updatePostDTO = UpdatePostDTO.builder()
                .postId(postRequestVO.getPostId())
                .title(postRequestVO.getTitle())
                .content(postRequestVO.getContent())
                .departures(postRequestVO.getDepartures())
                .departuresLatitude(postRequestVO.getDeparturesLatitude())
                .departuresLongitude(postRequestVO.getDeparturesLongitude())
                .arrivals(postRequestVO.getArrivals())
                .arrivalsLatitude(postRequestVO.getArrivalsLatitude())
                .arrivalsLongitude(postRequestVO.getArrivalsLongitude())
                .personnel(postRequestVO.getPersonnel())
                .regular(postRequestVO.getRegular())
                .carpoolDate(postRequestVO.getCarpoolDate())
                .driverId(postRequestVO.getDriverId())
                .userId(postRequestVO.getUserId())
                .build();

        try {
            PostResponseVO postResponseVO = postService.updatePost(updatePostDTO);

            BaseResponse baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("게시글 수정 성공!")
                    .data(postResponseVO)
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

    @ApiOperation("게시글 삭제")
    @DeleteMapping
    public ResponseEntity<BaseResponse> deletePost(@RequestParam Long postId, @RequestParam Long userId,
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
            postService.deletePost(postId);

            BaseResponse baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.NO_CONTENT)
                    .message("게시글 삭제 성공!")
                    .build();

            return new ResponseEntity<>(baseResponse, HttpStatus.NO_CONTENT);
        } catch (ServiceException e) {
            BaseResponse baseResponse = BaseResponse.builder()
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("서버 오류: " + e.getMessage())
                    .build();

            return new ResponseEntity<>(baseResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
