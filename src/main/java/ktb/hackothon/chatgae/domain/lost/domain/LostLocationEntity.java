package ktb.hackothon.chatgae.domain.lost.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name="Lost_Locations")
@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LostLocationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    Long id;

    @Column(name = "latitude")
    Double latitude;

    @Column(name = "longitude")
    Double longitude;

    @Column(name = "image_url")
    String imageUrl;

    @Column(name = "reg_dt")
    LocalDateTime regDt;

    @Builder
    public LostLocationEntity(Long id, Double latitude, Double longitude, String imageUrl, LocalDateTime regDt) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imageUrl = imageUrl;
        this.regDt = regDt;
    }
}
