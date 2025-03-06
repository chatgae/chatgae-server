package ktb.hackothon.chatgae.domain.pet.dto;

import ktb.hackothon.chatgae.domain.pet.entity.Pet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PetResponse {
    private boolean success;
    private String message;
    private Pet pet;
}