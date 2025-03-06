package ktb.hackothon.chatgae.domain.lost.service;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.GpsDirectory;
import ktb.hackothon.chatgae.domain.lost.domain.LostLocationEntity;
import ktb.hackothon.chatgae.domain.lost.dto.Coord;
import ktb.hackothon.chatgae.domain.lost.dto.Location;
import ktb.hackothon.chatgae.domain.lost.dto.LostLocationResponse;
import ktb.hackothon.chatgae.domain.lost.repository.LostRepository;
import ktb.hackothon.chatgae.global.api.ApiException;
import ktb.hackothon.chatgae.global.api.AppHttpStatus;
import ktb.hackothon.chatgae.global.api.PkResponse;
import ktb.hackothon.chatgae.global.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class LostServiceImpl implements LostService {

    private final LostRepository lostRepository;
    private final S3Service s3Service;
    @Override
    @Transactional
    public PkResponse createLostLocation(MultipartFile image, Coord coord) {
        // 이미지 저장 후 URL 받아오기
        String imageUrl = s3Service.uploadImage(image, 0L);
        log.info("createLostLocation/imageUrl : " + imageUrl);
        // 이미지 메타 데이터로 부터 위도 경도 데이터 만들기
//        double[] gps;
//
//        try {
//            gps = extractGpsFromImage(image);
//            log.info("createLostLocation/gps : " + gps);
//        } catch (IOException | ImageProcessingException e) {
//            throw new ApiException(AppHttpStatus.NOT_SUPPORTED_IMAGE);
//        }

        LostLocationEntity lostLocation = lostRepository.save(
                LostLocationEntity.builder()
                        .imageUrl(imageUrl)
                        .latitude(coord.getLatitude())
                        .longitude(coord.getLongitude())
                        .regDt(LocalDateTime.now())
                        .build()
        );
        log.info("createLostLocation/lostLocation : " + lostLocation);

        return PkResponse.of(lostLocation.getId());
    }

    @Override
    public LostLocationResponse getLostLocations(double latitude, double longitude, int distance, int limit) {
        List<Location> locations = lostRepository.findAllByGps(latitude, longitude, distance, limit)
                .stream()
                .map(Location::from)
                .toList();

        return LostLocationResponse.from(
                locations.size(),
                locations
        );
    }

    @Override
    public LostLocationResponse getAllLostLocations() {
        List<Location> locations = lostRepository.findAll()
                .stream()
                .map(Location::from)
                .toList();

        return LostLocationResponse.from(
                locations.size(),
                locations
        );
    }

    @Override
    public void deleteLostLocations(String start, String end) {
        LocalDateTime s = LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime e = LocalDateTime.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        lostRepository.deleteByDate(s, e);
    }

    private double[] extractGpsFromImage(MultipartFile image) throws IOException, ImageProcessingException {
        // IOException 발생
        InputStream inputStream = image.getInputStream();

        // ImageProcessingException 발생
        Metadata metadata = ImageMetadataReader.readMetadata(inputStream);
        GpsDirectory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);

        if (gpsDirectory == null || gpsDirectory.getGeoLocation() == null) {
            throw new ApiException(AppHttpStatus.INVALID_METADATA_IMAGE);
        }

        double latitude = gpsDirectory.getGeoLocation().getLatitude();
        double longitude = gpsDirectory.getGeoLocation().getLongitude();

        return new double[]{latitude, longitude};
    }
}
