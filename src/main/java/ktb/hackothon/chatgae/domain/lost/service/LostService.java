package ktb.hackothon.chatgae.domain.lost.service;

import ktb.hackothon.chatgae.global.api.PkResponse;
import org.springframework.web.multipart.MultipartFile;

public interface LostService {
    PkResponse createLostLocation(MultipartFile image);
}
