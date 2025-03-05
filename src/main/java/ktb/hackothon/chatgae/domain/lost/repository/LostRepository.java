package ktb.hackothon.chatgae.domain.lost.repository;

import ktb.hackothon.chatgae.domain.lost.domain.LostLocationEntity;

import java.util.List;

public interface LostRepository {
    LostLocationEntity save(LostLocationEntity lostLocation);
    List<LostLocationEntity> findAllByGps(double latitude, double longitude, int limit);
}
