package ktb.hackothon.chatgae.global.api;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * 어플리케이션에서 발생하는 예외를 전역으로 처리하는 클래스
 * - 예외 처리가 필요한 경우 @ExceptionHandler 메서드를 추가해서 사용할 것
 */

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<BaseResponse<Void>> runtimeExceptionHandler(RuntimeException e) {
		log.warn(500 + " " + e.getMessage());

		return ResponseEntity
				.status(500)
				.body( BaseResponse.error());
	}
	@ExceptionHandler(ApiException.class)
	public ResponseEntity<BaseResponse<Void>> apiExceptionHandler(ApiException e) {
		log.warn(e.getStatus().getStatusCode() + " " + e.getStatus().getMessage());

		return ResponseEntity
				.status(e.getStatus().getStatusCode())
				.body(BaseResponse.error());
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<BaseResponse<Void>> constraintViolationExceptionHandler(ConstraintViolationException e) {
		log.warn(400 + " " + e.getMessage());

		return ResponseEntity
				.status(400)
				.body(BaseResponse.error());
	}

	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<BaseResponse<Void>> noHandlerFoundExceptionHandler(NoResourceFoundException e) {
		log.warn(404 + " " + e.getMessage());

		return ResponseEntity
				.status(404)
				.body(BaseResponse.error());
	}
}