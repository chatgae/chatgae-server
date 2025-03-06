package ktb.hackothon.chatgae.domain.lost.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LostLocationResponse {
    final int total;
    final List<Location> locations;

    public static LostLocationResponse from(int total, List<Location> locations) {
        return LostLocationResponse.builder()
                .total(total)
                .locations(locations)
                .build();
    }
}
