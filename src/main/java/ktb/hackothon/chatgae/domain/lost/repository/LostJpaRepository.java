package ktb.hackothon.chatgae.domain.lost.repository;

import ktb.hackothon.chatgae.domain.lost.domain.LostLocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LostJpaRepository extends JpaRepository<LostLocationEntity, Long> {

}
