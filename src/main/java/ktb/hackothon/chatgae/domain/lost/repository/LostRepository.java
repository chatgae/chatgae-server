package ktb.hackothon.chatgae.domain.lost.repository;

import ktb.hackothon.chatgae.domain.lost.domain.LostLocationEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface LostRepository {
    LostLocationEntity save(LostLocationEntity lostLocation);
    List<LostLocationEntity> findAllByGps(double latitude, double longitude, int distance, int limit);
    List<LostLocationEntity> findAll();
    void deleteByDate(LocalDateTime s, LocalDateTime e);
}
