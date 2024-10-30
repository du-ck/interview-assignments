package com.labs44.interview.support.exception;

import com.labs44.interview.interfaces.api.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException e) {
        log.error(e.getMessage());
        //조회결과가 없는 exception 의 경우 success = true 처리.
        return ResponseEntity.status(404).body(new ErrorResponse(true, "404", e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // 첫 번째 필드 에러의 메시지만 사용 (여러 필드 검증이 실패한 경우 필요에 따라 변경 가능)
        FieldError fieldError = ex.getBindingResult().getFieldError();

        String errorMessage = (fieldError != null) ? fieldError.getDefaultMessage() : "Invalid input";

        return ResponseEntity.status(400).body(new ErrorResponse(false, "400", errorMessage));
    }

    @ExceptionHandler(value = AlreadyExistEmailException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyExistEmailException(AlreadyExistEmailException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(409).body(new ErrorResponse(false, "409", e.getMessage()));
    }

    @ExceptionHandler(value = AlreadyExistBoardException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyExistBoardException(AlreadyExistBoardException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(409).body(new ErrorResponse(false, "409", e.getMessage()));
    }

    @ExceptionHandler(value = TokenVerificationException.class)
    public ResponseEntity<ErrorResponse> handleTokenVerificationException(TokenVerificationException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(400).body(new ErrorResponse(false, "400", e.getMessage()));
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(401).body(new ErrorResponse(false, "401", e.getMessage()));
    }

    @ExceptionHandler(value = IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(500).body(new ErrorResponse(false, "500", e.getMessage()));
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(400).body(new ErrorResponse(false, "400", e.getMessage()));
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error(e.getMessage());
        return ResponseEntity.status(500).body(new ErrorResponse(false, "500", "에러가 발생했습니다."));
    }
}
