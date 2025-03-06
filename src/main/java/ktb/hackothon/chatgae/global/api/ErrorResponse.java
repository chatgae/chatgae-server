package ktb.hackothon.chatgae.global.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 어플리케이션 예외 응답 포맷 클래스
 * - 예외 처리가 필요한 경우 of 메서드를 오버로딩해서 사용할 것
 */

@Getter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorResponse {
    int code;
        String message;
        Map<String, String> messages;

        public static ErrorResponse of(ApiException e) {
            return ErrorResponse.builder()
                    .code(e.getStatus().getStatusCode())
                    .message(e.getStatus().getMessage())
                .build();
    }
    public static ErrorResponse of(RuntimeException e) {
        return ErrorResponse.builder()
                .code(500)
                .message(e.getMessage())
                .build();
    }
    public static ErrorResponse of(NoResourceFoundException e) {
        String errorMessage = e.getResourcePath() + " : " + AppHttpStatus.NOT_FOUND_ENDPOINT.getMessage();
        return ErrorResponse.builder()
                .code(404)
                .message(errorMessage)
                .build();
    }

    public static ErrorResponse of(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();

        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation<?> v : violations) {
            String field = v.getPropertyPath().toString().split("\\.")[1]; // e.g. method.field
            String message = v.getMessageTemplate();
            errors.put(field, message);
        }

        return ErrorResponse.builder()
                .code(400)
                .messages(errors)
                .build();
    }
}