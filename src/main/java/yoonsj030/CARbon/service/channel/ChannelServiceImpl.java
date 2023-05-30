package yoonsj030.CARbon.service.channel;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import yoonsj030.CARbon.dto.carbonFootprint.CreateCarbonFootprintDTO;
import yoonsj030.CARbon.dto.channel.AttendeeInfoDTO;
import yoonsj030.CARbon.dto.channel.JoinCarpoolDTO;
import yoonsj030.CARbon.dto.user.ParticipateUserDTO;
import yoonsj030.CARbon.entity.channel.Channel;
import yoonsj030.CARbon.entity.coordinate.Coordinate;
import yoonsj030.CARbon.entity.post.Post;
import yoonsj030.CARbon.entity.user.User;
import yoonsj030.CARbon.entity.user.UserHasChannel;
import yoonsj030.CARbon.repository.channel.ChannelRepository;
import yoonsj030.CARbon.repository.coordinate.CoordinateRepository;
import yoonsj030.CARbon.repository.user.UserHasChannelRepository;
import yoonsj030.CARbon.repository.user.UserRepository;
import yoonsj030.CARbon.service.carbonFootprint.CarbonFootprintService;
import yoonsj030.CARbon.vo.channel.CarpoolMeasureRequestVO;
import yoonsj030.CARbon.vo.channel.CarpoolRequestVO;
import yoonsj030.CARbon.vo.channel.ChannelResponseVO;
import yoonsj030.CARbon.vo.channel.ParticipantsRating;
import yoonsj030.CARbon.vo.coordinate.AllPoints;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class ChannelServiceImpl implements ChannelService {
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final UserHasChannelRepository userHasChannelRepository;
    private final CoordinateRepository coordinateRepository;
    private final CarbonFootprintService carbonFootprintService;

    @Override
    public boolean existsByJoinCarpoolRequest(JoinCarpoolDTO joinCarpoolDTO) {
        Channel channel = channelRepository
                .findById(joinCarpoolDTO.getChannelId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 Channel입니다."));

        User user = userRepository
                .findById(joinCarpoolDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 User입니다."));

        UserHasChannel userHasChannel = userHasChannelRepository.findByChannelAndUser(channel, user);

        if(userHasChannel != null) {
            return true;
        }
        return false;
    }

    @Override
    public void joinCarpool(JoinCarpoolDTO joinCarpoolDTO) {
        Channel channel = channelRepository
                .findById(joinCarpoolDTO.getChannelId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 채널입니다."));

        User user = userRepository
                .findById(joinCarpoolDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));            // 참가자

        User host = userRepository.findByNickname(channel.getHostNickname());

        User driver = userRepository.findByNickname(channel.getDriverNickname());

        UserHasChannel userHasChannel = UserHasChannel.builder()
                .departures(joinCarpoolDTO.getDepartures())
                .departuresLatitude(joinCarpoolDTO.getDeparturesLatitude())
                .departuresLongitude(joinCarpoolDTO.getDeparturesLongitude())
                .arrivals(joinCarpoolDTO.getArrivals())
                .arrivalsLatitude(joinCarpoolDTO.getArrivalsLatitude())
                .arrivalsLongitude(joinCarpoolDTO.getArrivalsLongitude())
                .attendance(false)
                .user(user)
                .channel(channel)
                .build();

        try {
            userHasChannelRepository.save(userHasChannel);
        } catch (RuntimeException e) {
            log.error("User Has Channel 저장 중 오류 발생: " + e.getMessage());
            throw new RuntimeException("User Has Channel 저장 중 오류 발생");
        }

        channel.setUserList(userHasChannel);
        channel.updateCurPersonnel();
        user.setChannelList(userHasChannel);

        try {
            channelRepository.save(channel);
        } catch (RuntimeException e) {
            log.error("Channel 저장 중 오류 발생: " + e.getMessage());
            throw new RuntimeException("Channel 저장 중 오류 발생");
        }

        try {
            userRepository.save(user);
        } catch (RuntimeException e) {
            log.error("User 저장 중 오류 발생: " + e.getMessage());
            throw new RuntimeException("User 저장 중 오류 발생");
        }
    }

    @Override
    public List<ChannelResponseVO> getAllCarpools(Long userId) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자입니다."));

        List<UserHasChannel> userHasChannelList = user.getChannelList();            // 참여 중인 모든 카풀

        List<ChannelResponseVO> channelResponseVOList = new ArrayList<>();

        for (UserHasChannel userHasChannel : userHasChannelList) {
            Channel channel = userHasChannel.getChannel();          // 참여 중인 카풀 1개

            List<UserHasChannel> participantsList = channel.getUserList();              // 카풀에 참여 중인 모든 유저

            List<ParticipateUserDTO> participateUserDTOList = new ArrayList<>();

            for(UserHasChannel participantHasChannel : participantsList) {
                User participant = participantHasChannel.getUser();             // 참여 중인 유저 1명

                ParticipateUserDTO participateUserDTO = ParticipateUserDTO.builder()
                        .userId(participant.getUserId())
                        .nickname(participant.getNickname())
                        .departures(participantHasChannel.getDepartures())
                        .departuresLatitude(participantHasChannel.getDeparturesLatitude())
                        .departuresLongitude(participantHasChannel.getDeparturesLongitude())
                        .arrivals(participantHasChannel.getArrivals())
                        .arrivalsLatitude(participantHasChannel.getArrivalsLatitude())
                        .arrivalsLongitude(participantHasChannel.getArrivalsLongitude())
                        .rating(participant.getRating())
                        .build();

                participateUserDTOList.add(participateUserDTO);
            }

            User host = userRepository.findByNickname(channel.getHostNickname());

            User driver = userRepository.findByNickname(channel.getDriverNickname());

            ChannelResponseVO channelResponseVO = ChannelResponseVO.builder()
                    .channelId(channel.getChannelId())
                    .hostId(host.getUserId())
                    .hostNickname(host.getNickname())
                    .driverId(driver.getUserId())
                    .driverNickname(driver.getNickname())
                    .departures(userHasChannel.getDepartures())
                    .departuresLatitude(userHasChannel.getDeparturesLatitude())
                    .departuresLongitude(userHasChannel.getDeparturesLongitude())
                    .arrivals(userHasChannel.getArrivals())
                    .arrivalsLatitude(userHasChannel.getArrivalsLatitude())
                    .arrivalsLongitude(userHasChannel.getArrivalsLongitude())
                    .personnel(channel.getPost().getPersonnel())
                    .curPersonnel(channel.getCurPersonnel())
                    .content(channel.getPost().getContent())
                    .regular(channel.getPost().getRegular())
                    .userHasChannelList(participateUserDTOList)
                    .build();

            channelResponseVOList.add(channelResponseVO);
        }

        return channelResponseVOList;
    }

    @Override
    public List<ParticipateUserDTO> getAllParticipants(Long channelId) {
        Channel channel = channelRepository
                .findById(channelId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 Channel입니다."));

        List<UserHasChannel> participantsList = channel.getUserList();              // 카풀에 참여 중인 모든 유저

        List<ParticipateUserDTO> participateUserDTOList = new ArrayList<>();

        for (UserHasChannel userHasChannel : participantsList) {
            User participant = userHasChannel.getUser();

            ParticipateUserDTO participateUserDTO = ParticipateUserDTO.builder()
                    .userId(participant.getUserId())
                    .nickname(participant.getNickname())
                    .departures(userHasChannel.getDepartures())
                    .departuresLatitude(userHasChannel.getDeparturesLatitude())
                    .departuresLongitude(userHasChannel.getDeparturesLongitude())
                    .arrivals(userHasChannel.getArrivals())
                    .arrivalsLatitude(userHasChannel.getArrivalsLatitude())
                    .arrivalsLongitude(userHasChannel.getArrivalsLongitude())
                    .rating(participant.getRating())
                    .build();

            participateUserDTOList.add(participateUserDTO);
        }

        return participateUserDTOList;
    }

    @Override
    public void attendCarpool(CarpoolRequestVO carpoolRequestVO) {
        Channel channel = channelRepository
                .findById(carpoolRequestVO.getCarpoolId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 채널입니다."));

        for (AttendeeInfoDTO attendeeInfoDTO : carpoolRequestVO.getAttendeeInfoDTOList()) {
            User user = userRepository
                    .findById(attendeeInfoDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 User입니다."));

            UserHasChannel userHasChannel = userHasChannelRepository.findByChannelAndUser(channel, user);

            userHasChannel.setAttendance(attendeeInfoDTO.getAttendance());
            userHasChannelRepository.save(userHasChannel);

            try {
                userHasChannelRepository.save(userHasChannel);
            } catch (RuntimeException e) {
                log.error("User Has Channel 저장 중 오류 발생: " + e.getMessage());
                throw new RuntimeException("User Has Channel 저장 중 오류 발생");
            }
        }
    }

    @Override
    public HttpStatus startCarpool(Long carpoolId, Long userId) {
        Channel channel = channelRepository
                .findById(carpoolId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 Channel입니다."));

        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 User입니다."));

        UserHasChannel userHasChannel = userHasChannelRepository.findByChannelAndUser(channel, user);

        if(userHasChannel.getAttendance() == true) {
            return HttpStatus.OK;
        } else {
            return  HttpStatus.BAD_REQUEST;
        }
    }

    @Override
    public void measureCarpool(CarpoolMeasureRequestVO carpoolMeasureRequestVO) {
        Channel channel = channelRepository
                .findById(carpoolMeasureRequestVO.getCarpoolId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 Channel입니다."));

        User user = userRepository
                .findById(carpoolMeasureRequestVO.getUserId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 User입니다."));

        UserHasChannel userHasChannel = userHasChannelRepository.findByChannelAndUser(channel, user);

        userHasChannel.setDistance(carpoolMeasureRequestVO.getDistance());
        user.setPoint(carpoolMeasureRequestVO.getDistance());

        try {
            userHasChannelRepository.save(userHasChannel);
        } catch (RuntimeException e) {
            log.error("User Has Channel 저장 중 오류 발생: " + e.getMessage());
            throw new RuntimeException("User Has Channel 저장 중 오류 발생");
        }

        try {
            userRepository.save(user);
        } catch (RuntimeException e) {
            log.error("User 저장 중 오류 발생: " + e.getMessage());
            throw new RuntimeException("User 저장 중 오류 발생");
        }

        Post post = channel.getPost();
        double emissions = (carpoolMeasureRequestVO.getDistance() * 0.21) / channel.getCurPersonnel();   // 탄소배출량 계산

        CreateCarbonFootprintDTO createCarbonFootprintDTO = CreateCarbonFootprintDTO.builder()
                .user(user)
                .carpoolDate(post.getCarpoolDate())
                .distance(carpoolMeasureRequestVO.getDistance())
                .emissions(emissions)
                .build();

        carbonFootprintService.createCarbonFootprint(createCarbonFootprintDTO);

        /**
         * 이동 경로 저장
         */
        for (AllPoints allPoints : carpoolMeasureRequestVO.getAllPointsList()) {
            Coordinate coordinate = Coordinate.builder()
                    .latitude(allPoints.getLatitude())
                    .longitude(allPoints.getLongitude())
                    .userHasChannel(userHasChannel)
                    .build();

            try {
                coordinateRepository.save(coordinate);
            } catch (RuntimeException e) {
                log.error("Coordinate 저장 중 오류 발생: " + e.getMessage());
                throw new RuntimeException("Coordinate 저장 중 오류 발생");
            }

            userHasChannel.setCoordinateList(coordinate);

            try {
                userHasChannelRepository.save(userHasChannel);
            } catch (RuntimeException e) {
                log.error("User Has Channel 저장 중 오류 발생: " + e.getMessage());
                throw new RuntimeException("User Has Channel 저장 중 오류 발생");
            }
        }

        /**
         * 카풀 함께 한 사람들에 대한 평가
         */
        for (ParticipantsRating participantsRating : carpoolMeasureRequestVO.getParticipantsRatingList()) {
            User participant = userRepository
                    .findById(participantsRating.getUserId())
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

            UserHasChannel participantHasChannel = userHasChannelRepository.findByChannelAndUser(channel, participant);

            double rating = participantsRating.getRating();             // 부여 받은 평가
            int ratingCnt = 0;

            if(participantHasChannel.getRatingCnt() == null) {
                ratingCnt++;
                participantHasChannel.setRating(rating, ratingCnt);
            } else {
                double curRating = participantHasChannel.getRating();

                ratingCnt = participantHasChannel.getRatingCnt();
                rating = (rating + (curRating * ratingCnt)) / (ratingCnt + 1);
                ratingCnt++;

                participantHasChannel.setRating(rating, ratingCnt);
            }

            try {
                userHasChannelRepository.save(participantHasChannel);
            } catch (RuntimeException e) {
                log.error("User Has Channel 저장 중 오류 발생: " + e.getMessage());
                throw new RuntimeException("User Has Channel 저장 중 오류 발생");
            }

            participant.setRating(rating, ratingCnt);

            try {
                userRepository.save(participant);
            } catch (RuntimeException e) {
                log.error("User 저장 중 오류 발생: " + e.getMessage());
                throw new RuntimeException("User 저장 중 오류 발생");
            }
        }
    }
}
