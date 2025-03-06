package ktb.hackothon.chatgae.domain.pet.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ktb.hackothon.chatgae.domain.pet.dto.PetResponse;
import ktb.hackothon.chatgae.domain.pet.dto.PetResponseCode;
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

        PetResponse savedPet = petService.addPet(pet, profileImage, noseImages);

        if (savedPet.getCode() < 0) {
            return ResponseEntity
                    .status(404)
                    .body(BaseResponse.success(pet));
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
                    .status(404)
                    .body(BaseResponse.success(pet));
        }

        return ResponseEntity
                .status(201)
                .body(BaseResponse.success(pet));
    }

    @GetMapping("/")
    // ADD : 나중에는 현재 사용자의 userId를 넣어줘야 함
    public ResponseEntity<BaseResponse<?>> getPetList() {
        Optional<List<Pet>> pet = petService.getPetList();

        if (pet.isEmpty()) {
            return ResponseEntity
                    .status(404)
                    .body(BaseResponse.success(pet));
        }

        return ResponseEntity
                .status(201)
                .body(BaseResponse.success(pet));
    }

    @PostMapping(value = "/identify", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponse<?>> identifyPet(@RequestParam("file") MultipartFile file) {
        PetResponse pet = petService.identifyPet(file);

        if (pet.getCode() == PetResponseCode.AI_PROCESSING_FAILED || pet.getCode() == PetResponseCode.SERVER_ERROR) {
            return ResponseEntity
                    .status(404)
                    .body(BaseResponse.success(pet));
        }

        return ResponseEntity
                .status(201)
                .body(BaseResponse.success(pet));

    }

    @DeleteMapping(value = "/")
    public ResponseEntity<BaseResponse<?>> deletePet(@RequestParam("uniqueNumber") String uniqueNumber) {
        PetResponse response = petService.deletePet(uniqueNumber);

        return ResponseEntity
                .status(201)
                .body(BaseResponse.success(response.getMessage()));
    }
}