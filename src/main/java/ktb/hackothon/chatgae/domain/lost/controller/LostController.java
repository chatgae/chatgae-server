package ktb.hackothon.chatgae.domain.lost.controller;

import ktb.hackothon.chatgae.domain.lost.dto.Coord;
import ktb.hackothon.chatgae.domain.lost.dto.LostLocationResponse;
import ktb.hackothon.chatgae.domain.lost.service.LostService;
import ktb.hackothon.chatgae.global.api.BaseResponse;
import ktb.hackothon.chatgae.global.api.PkResponse;
import ktb.hackothon.chatgae.global.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/losts")
public class LostController {

    final LostService lostService;

    @PostMapping("")
    public ResponseEntity<BaseResponse<PkResponse>> createLostLocation(
            @RequestPart("image") MultipartFile image,
            @RequestPart("coord") String coordJson
    ) {

        Coord coord = JsonUtil.fromJson(coordJson, Coord.class);
        log.info("createLostLocation/coord : " + coord);
        return ResponseEntity
                .status(201)
                .body(BaseResponse.success(lostService.createLostLocation(image, coord)));
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

    @DeleteMapping("")
    public BaseResponse<Void> deleteLostLocation(
            String start,
            String end
    ) {
        lostService.deleteLostLocations(start, end);
        return BaseResponse.success();
    }
}
