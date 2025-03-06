package ktb.hackothon.chatgae.domain.pet.service;

import ktb.hackothon.chatgae.domain.pet.dto.PetResponse;
import ktb.hackothon.chatgae.domain.pet.entity.Pet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import ktb.hackothon.chatgae.domain.pet.repository.PetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import ktb.hackothon.chatgae.global.service.S3Service;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

@Service
@Slf4j
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
    public PetResponse addPet(Pet pet, MultipartFile profileImage, List<MultipartFile> noseImages) {
        Optional<String> unique_number = getDogNose(noseImages);
        log.info("addPet/unique_number : " + unique_number);

        // 이미 등록된 강아지
        if (!unique_number.get().equals("-1")) {
            return new PetResponse(false, "반려견 등록 실패 : 이미 등록된 강아지", null);
        }
        
        // ADD : 존재하지 않는 경우에 넣을 코드 추가
        pet.setUniqueNumber(unique_number.get());

        Pet savePet = petRepository.save(pet);

        String profile_url = s3Service.uploadImage(profileImage, 1L);
//        String profile_url = "Test";
        savePet.setProfile(profile_url);

        Pet savePetAgain = petRepository.save(savePet);
        return new PetResponse(true, "반려견 등록 성공!", savePetAgain);
    }

    public Optional<Pet> getPetById(int petId) {
        return petRepository.findById(petId);
    }
    public Optional<List<Pet>> getPetList(){
        return Optional.of(petRepository.findAll());
    }
    public PetResponse identifyPet(MultipartFile file) {
        String serverUrl = "https://widely-select-polliwog.ngrok-free.app/lookup"; // 대상 서버 URL

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // 단일 파일만 보내도록 수정
        LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("dogNose", file.getResource());

        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(serverUrl, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<Map<String, Object>>() {});

        Map<String, Object> responseData = response.getBody();
        log.info("identifyPet/responseData : " + responseData);
        if (!(boolean) responseData.get("isSuccess")) {
            return new PetResponse(false, "인공지능에서 비슷한 반려견을 찾지 못했습니다.", null);
            //return Optional.empty(); // 반려견을 찾지 못한 경우
        }
        String uniqueNumber = (String) responseData.get("id");

        Optional<Pet> petOptional = petRepository.findByUniqueNumber(uniqueNumber);
        log.info("identifyPet/petOptional : " + petOptional);

        if (petOptional.isEmpty()) {
            return new PetResponse(false, "서버에서 반려견의 데이터를 찾지 못했습니다.", null);
            //return Optional.empty();
        }
        Pet pet = petOptional.get();

        return new PetResponse(true, "성공", pet);
        //return Optional.of(pet);
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

        // ADD : 반려견이 이미 등록되어 있는 경우
        log.info("getDogNose/responseData : " + responseData.get("id") + ", " + responseData.get("status"));
        if((int) responseData.get("status") == 0){
            return Optional.ofNullable((String) responseData.get("id"));
        }

        return Optional.ofNullable("-1");
    }

}