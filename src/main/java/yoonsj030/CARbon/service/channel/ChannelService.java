package yoonsj030.CARbon.service.channel;

import org.springframework.http.HttpStatus;
import yoonsj030.CARbon.dto.channel.JoinCarpoolDTO;
import yoonsj030.CARbon.dto.user.ParticipateUserDTO;
import yoonsj030.CARbon.vo.channel.CarpoolMeasureRequestVO;
import yoonsj030.CARbon.vo.channel.CarpoolRequestVO;
import yoonsj030.CARbon.vo.channel.ChannelResponseVO;

import java.util.List;

public interface ChannelService {
    boolean existsByJoinCarpoolRequest(JoinCarpoolDTO joinCarpoolDTO);

    void joinCarpool(JoinCarpoolDTO joinCarpoolDTO);

    List<ChannelResponseVO> getAllCarpools(Long userId);

    List<ParticipateUserDTO> getAllParticipants(Long channelId);

    void attendCarpool(CarpoolRequestVO carpoolRequestVO);

    HttpStatus startCarpool(Long carpoolId, Long userId);

    void measureCarpool(CarpoolMeasureRequestVO carpoolMeasureRequestVO);

    int getChannelCount();
}
