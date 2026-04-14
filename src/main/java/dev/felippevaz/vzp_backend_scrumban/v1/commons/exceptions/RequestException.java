package dev.felippevaz.vzp_backend_scrumban.v1.commons.exceptions;

import dev.felippevaz.vzp_backend_scrumban.v1.commons.domain.ErrorData;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class RequestException extends RuntimeException {

    private final ErrorData errorData;

    public RequestException(ErrorData errorData) {
        this.errorData = errorData;
        super(errorData.getDetail());
    }
}
