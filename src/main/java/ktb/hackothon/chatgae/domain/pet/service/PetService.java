package ktb.hackothon.chatgae.domain.pet.service;

import ktb.hackothon.chatgae.domain.pet.entity.Pet;
import ktb.hackothon.chatgae.global.api.BaseResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import ktb.hackothon.chatgae.domain.pet.repository.PetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import ktb.hackothon.chatgae.global.service.S3Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PetService {
    private final PetRepository petRepository;
    private final RestTemplate restTemplate;
    private final S3Service s3Service;
    public PetService(PetRepository petRepository, S3Service s3Service) {
        this.petRepository = petRepository;
        this.s3Service = s3Service;
        this.restTemplate = new RestTemplate();
    }

    @Transactional
    public Optional<Pet> addPet(Pet pet, MultipartFile profileImage, List<MultipartFile> noseImages) {
        Optional<String> unique_number = getDogNose(noseImages);

        // ADD : 존재하지 않는 경우에 넣을 코드 추가
        pet.setUniqueNumber(unique_number.get());

        Pet savePet = petRepository.save(pet);

        String profile_url = s3Service.uploadImage(profileImage, savePet.getPetId());
//        String profile_url = "Test";
        savePet.setProfile(profile_url);

        Pet savePetAgain = petRepository.save(savePet);
        return Optional.of(savePetAgain);
    }

    public Optional<Pet> getPetById(int petId) {
        return petRepository.findById(petId);
    }

    public Optional<Pet> identifyPet(MultipartFile file) {
        String serverUrl = "https://widely-select-polliwog.ngrok-free.app/lookup"; // 대상 서버 URL

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // 단일 파일만 보내도록 수정
        LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("dogNose", file.getResource());

        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Map<String, Object>> response =
                restTemplate.exchange(serverUrl, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<Map<String, Object>>() {});

        Map<String, Object> responseData = response.getBody();

        if (responseData == null || !(boolean) responseData.get("isSuccess")) {
            return Optional.empty(); // 반려견을 찾지 못한 경우
        }
        String uniqueNumber = (String) responseData.get("id");

        Optional<Pet> petOptional = petRepository.findByUniqueNumber(uniqueNumber);
        if (petOptional.isEmpty()) {
            return Optional.empty();
        }
        Pet pet = petOptional.get();

        return Optional.of(pet);
    }

    public Optional<String> getDogNose(List<MultipartFile> list){
        String serverUrl = "https://widely-select-polliwog.ngrok-free.app/register"; // 대상 서버 URL

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // 단일 파일만 보내도록 수정
        LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        for(int i=0; i<list.size(); i++){
            body.add("dogNose"+(i+1), list.get(i).getResource());
        }

        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.exchange(serverUrl, HttpMethod.POST, requestEntity, Map.class);

        Map<String, Object> responseData = response.getBody();
        
        // ADD : 반려견이 이미 등록되어 있는 경우면 반환 값을 다르게 보내줘야 됨

        return Optional.ofNullable((String) responseData.get("id"));
    }
}