package ktb.hackothon.chatgae.domain.lost.controller;

import ktb.hackothon.chatgae.domain.lost.dto.LostLocationResponse;
import ktb.hackothon.chatgae.domain.lost.service.LostService;
import ktb.hackothon.chatgae.global.api.BaseResponse;
import ktb.hackothon.chatgae.global.api.PkResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/losts")
public class LostController {

    final LostService lostService;

    @PostMapping("")
    public ResponseEntity<BaseResponse<PkResponse>> createLostLocation(
            @RequestPart("image") MultipartFile image
    ) {
        return ResponseEntity
                .status(201)
                .body(BaseResponse.success(lostService.createLostLocation(image)));
    }

    @GetMapping("")
    public ResponseEntity<BaseResponse<LostLocationResponse>> getLostLocations(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam int distance,
            @RequestParam(defaultValue = "10") int limit
    ) {
        return ResponseEntity
                .status(200)
                .body(BaseResponse.success(lostService.getLostLocations(latitude, longitude, distance, limit)));
    }

    @GetMapping("/all")
    public BaseResponse<LostLocationResponse> getAllLostLocations(
    ) {
        return BaseResponse.success(lostService.getAllLostLocations());
    }
}
