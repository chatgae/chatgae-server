package ktb.hackothon.chatgae.domain.pet.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ktb.hackothon.chatgae.domain.pet.entity.Pet;
import ktb.hackothon.chatgae.domain.pet.service.PetService;
import ktb.hackothon.chatgae.global.api.ApiException;
import ktb.hackothon.chatgae.global.api.BaseResponse;
import ktb.hackothon.chatgae.global.api.AppHttpStatus;
import ktb.hackothon.chatgae.global.util.JsonUtil;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/pets")
public class PetController {
    final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponse<?>> addPet(@RequestPart("pet") String petJson,
                                                              @RequestPart("profileImage") MultipartFile profileImage,
                                                              @RequestPart("noseImages") List<MultipartFile> noseImages) throws JsonProcessingException {

        Pet pet = JsonUtil.fromJson(petJson, Pet.class);

        Optional<Pet> savedPet = petService.addPet(pet, profileImage, noseImages);

        if (savedPet.isEmpty()) {
            return ResponseEntity
                    .status(500)
                    .body(BaseResponse.error());
        }

        return ResponseEntity
                .status(201)
                .body(BaseResponse.success(savedPet));
    }

    @GetMapping("/{petId}")
    public ResponseEntity<BaseResponse<?>> getPet(@PathVariable int petId) {
        Optional<Pet> pet = petService.getPetById(petId);

        if (pet.isEmpty()) {
            return ResponseEntity
                    .status(500)
                    .body(BaseResponse.error());
        }

        return ResponseEntity
                .status(201)
                .body(BaseResponse.success(pet));
    }

    @PostMapping("/identify")
    public ResponseEntity<BaseResponse<?>> identifyPet(@RequestParam("file") MultipartFile file) {
        Optional<Pet> pet = petService.identifyPet(file);

        if (pet.isEmpty()) {
            return ResponseEntity
                    .status(500)
                    .body(BaseResponse.error());
        }

        return ResponseEntity
                .status(201)
                .body(BaseResponse.success(pet));

    }
}