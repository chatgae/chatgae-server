package ktb.hackothon.chatgae.domain.lost.service;

import ktb.hackothon.chatgae.domain.lost.dto.Coord;
import ktb.hackothon.chatgae.domain.lost.dto.LostLocationResponse;
import ktb.hackothon.chatgae.global.api.PkResponse;
import org.springframework.web.multipart.MultipartFile;

public interface LostService {
    PkResponse createLostLocation(MultipartFile image, Coord coord);
    LostLocationResponse getLostLocations(double latitude, double longitude, int distance, int limit);
    LostLocationResponse getAllLostLocations();

    void deleteLostLocations(String start, String end);
}
