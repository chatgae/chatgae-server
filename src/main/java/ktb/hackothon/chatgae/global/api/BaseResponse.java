package ktb.hackothon.chatgae.global.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

/**
 * 어플리케이션 기본 응답 포맷 클래스
 * - 성공 응답 시 success 메서드 사용할 것
 * - 실패 응답 시 error 메서드 사용할 것
 */

@Getter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseResponse<T> {

    String status;
    T data;
    ErrorResponse error;

    public static <T> BaseResponse<T> success(T data) {
        return BaseResponse.<T>builder()
                .status("success")
                .data(data)
                .build();
    }

    public static BaseResponse<Void> error(ErrorResponse e) {
        return BaseResponse.<Void>builder()
                .status("error")
                .error(e)
                .build();
    }
}