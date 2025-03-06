package ktb.hackothon.chatgae.domain.lost.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
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
    public List<LostLocationEntity> findAllByGps(double latitude, double longitude, int distance, int limit) {

        double latDiff = distance / 111320.0;
        double lonDiff = distance / 111320.0 * Math.cos(Math.toRadians(latitude));

        return query
                .select(lostLocationEntity)
                .from(lostLocationEntity)
                .where(
                        latitudeBetween(latitude, latDiff),
                        longitudeBetween(longitude, lonDiff)
                )
                .orderBy(lostLocationEntity.regDt.desc())
                .limit(limit)
                .stream()
                .toList();
    }

    @Override
    public List<LostLocationEntity> findAll() {
        return lostJpaRepository.findAll();
    }

    private BooleanExpression latitudeBetween(double latitude, double diff) {
        return lostLocationEntity.latitude.between(latitude - diff, latitude + diff);
    }

    private BooleanExpression longitudeBetween(double longitude, double diff) {
        return lostLocationEntity.longitude.between(longitude - diff, longitude + diff);
    }

}
