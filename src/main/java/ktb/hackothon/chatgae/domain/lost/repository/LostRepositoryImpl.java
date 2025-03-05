package ktb.hackothon.chatgae.domain.lost.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import ktb.hackothon.chatgae.domain.lost.domain.LostLocationEntity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;

import java.util.List;

import static ktb.hackothon.chatgae.domain.lost.domain.QLostLocationEntity.lostLocationEntity;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LostRepositoryImpl implements LostRepository {
    final JPAQueryFactory query;
    final LostJpaRepository lostJpaRepository;
    @Override
    public LostLocationEntity save(LostLocationEntity lostLocation) {
        return lostJpaRepository.save(lostLocation);
    }

    @Override
    public List<LostLocationEntity> findAllByGps(double latitude, double longitude, int limit) {
        return query
                .select(lostLocationEntity)
                .from(lostLocationEntity)
                // TODO : 반경 N KM 좌표 추출해야됨
                .orderBy(lostLocationEntity.regDt.desc())
                .limit(limit)
                .stream()
                .toList();
    }

}
