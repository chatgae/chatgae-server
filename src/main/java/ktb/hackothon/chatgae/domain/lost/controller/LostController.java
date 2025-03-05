package ktb.hackothon.chatgae.domain.lost.controller;

import ktb.hackothon.chatgae.domain.lost.service.LostService;
import ktb.hackothon.chatgae.global.api.BaseResponse;
import ktb.hackothon.chatgae.global.api.PkResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/losts")
public class LostController {

    final LostService lostService;

    @PostMapping("")
    public BaseResponse<PkResponse> createLostLocation(
            @RequestPart("image") MultipartFile image
    ) {
        return BaseResponse.success(lostService.createLostLocation(image));
    }
}
