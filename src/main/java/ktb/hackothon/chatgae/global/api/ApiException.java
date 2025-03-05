package ktb.hackothon.chatgae.global.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 어플리케이션에서 발생할 수 있는 커스텀 예외 클래스
 */

@Getter
@RequiredArgsConstructor
public class ApiException extends RuntimeException {
    private final AppHttpStatus status;
}