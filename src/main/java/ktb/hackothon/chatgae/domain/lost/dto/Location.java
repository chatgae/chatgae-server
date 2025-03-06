package ktb.hackothon.chatgae.domain.lost.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ktb.hackothon.chatgae.domain.lost.domain.LostLocationEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Location {
    final double latitude;
    final double longitude;
    final String imageUrl;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    final LocalDateTime registeredAt;

    public static Location from(LostLocationEntity lostLocation) {
        return Location.builder()
                .latitude(lostLocation.getLatitude())
                .longitude(lostLocation.getLongitude())
                .imageUrl(lostLocation.getImageUrl())
                .registeredAt(lostLocation.getRegDt())
                .build();
    }

}