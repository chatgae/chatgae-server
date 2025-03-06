package ktb.hackothon.chatgae.domain.pet.repository;

import ktb.hackothon.chatgae.domain.pet.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PetRepository extends JpaRepository<Pet,Integer> {
    Optional<Pet> findByUniqueNumber(String uniqueNumber);
    void deleteByUniqueNumber(String uniqueNumber);

}
