package ru.practicum.ewm.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import ru.practicum.ewm.dto.ApiErrorDto;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler({NotFoundException.class, NoResourceFoundException.class})
    public ResponseEntity<ApiErrorDto> handleNotFound(Exception exception) {
        return response(HttpStatus.NOT_FOUND, "The required object was not found.", exception);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiErrorDto> handleConflict(ConflictException exception) {
        return response(HttpStatus.CONFLICT, "For the requested operation the conditions are not met.", exception);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorDto> handleIntegrityViolation(DataIntegrityViolationException exception) {
        return response(HttpStatus.CONFLICT, "Integrity constraint has been violated.", exception);
    }

    @ExceptionHandler({BadRequestException.class, MethodArgumentNotValidException.class, BindException.class,
            ConstraintViolationException.class, MethodArgumentTypeMismatchException.class,
            MissingServletRequestParameterException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ApiErrorDto> handleBadRequest(Exception exception) {
        return response(HttpStatus.BAD_REQUEST, "Incorrectly made request.", exception);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorDto> handleUnexpected(Exception exception) {
        return response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error.", exception);
    }

    private ResponseEntity<ApiErrorDto> response(HttpStatus status, String reason, Exception exception) {
        ApiErrorDto error = new ApiErrorDto(List.of(), exception.getMessage(), reason, status, LocalDateTime.now());
        return ResponseEntity.status(status).body(error);
    }
}
