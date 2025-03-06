package ktb.hackothon.chatgae.domain.lost.service;

import ktb.hackothon.chatgae.domain.lost.dto.LostLocationResponse;
import ktb.hackothon.chatgae.global.api.PkResponse;
import org.springframework.web.multipart.MultipartFile;

public interface LostService {
    PkResponse createLostLocation(MultipartFile image);
    LostLocationResponse getLostLocations(double latitude, double longitude, int distance, int limit);
    LostLocationResponse getAllLostLocations();
}
