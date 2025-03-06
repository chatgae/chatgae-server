package ktb.hackothon.chatgae.domain.pet.dto;

import ktb.hackothon.chatgae.domain.pet.entity.Pet;
import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PetResponse {
    private int code;
    private String message;
    private Pet pet;
}