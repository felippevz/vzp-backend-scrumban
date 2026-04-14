package dev.felippevaz.vzp_backend_scrumban.v1.commons.handler;

import dev.felippevaz.vzp_backend_scrumban.v1.commons.domain.ErrorData;
import dev.felippevaz.vzp_backend_scrumban.v1.commons.domain.FieldErrorDetail;
import dev.felippevaz.vzp_backend_scrumban.v1.commons.exceptions.RequestException;
import org.jspecify.annotations.Nullable;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RequestException.class)
    public ResponseEntity<Object> handleRequestException(RequestException exception) {
        return createProblemDetail(exception.getErrorData(), null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUncaught() {
        return createProblemDetail(ErrorData.INTERNAL_ERROR, null);
    }

    @Override
    protected @Nullable ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        return createProblemDetail(ErrorData.INVALID_JSON_FORMAT, null);
    }

    @Override
    protected @Nullable ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        var errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> new FieldErrorDetail(error.getField(), error.getDefaultMessage()))
                .toList();

        return createProblemDetail(ErrorData.INVALID_JSON_DATA, errors);
    }

    private ResponseEntity<Object> createProblemDetail(ErrorData errorData, List<FieldErrorDetail> errorFields) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(errorData.getStatus(), errorData.getDetail());
        problemDetail.setTitle(errorData.getTitle());
        problemDetail.setType(errorData.getUri());

        if (errorFields != null && !errorFields.isEmpty()) {
            problemDetail.setProperty("errors", errorFields);
        }

        problemDetail.setProperty("timestamp", Instant.now());

        return ResponseEntity.status(errorData.getStatus()).body(problemDetail);
    }
}
