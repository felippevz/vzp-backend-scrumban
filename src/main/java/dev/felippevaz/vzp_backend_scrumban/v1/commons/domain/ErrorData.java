package dev.felippevaz.vzp_backend_scrumban.v1.commons.domain;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.net.URI;

@Getter
public enum ErrorData {

    INVALID_JSON_FORMAT(HttpStatus.BAD_REQUEST, "error:invalid_json_format", "Error of json format", "One or more missing fields"),
    INVALID_JSON_DATA(HttpStatus.UNPROCESSABLE_CONTENT, "error:invalid_json_data", "Error of validation", "One or more invalid fields"),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "error:internal_error", "Internal server error", "Internal server error"),
    BAD_CREDENTIALS(HttpStatus.CONFLICT, "error:bad_credentials", "Bad credentials", "Invalid username or password"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "error:user_not_found", "User not found", "User not found"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "error:invalid_token", "Invalid token", "Invalid token"),
    INVALID_UPDATE_EMAIL(HttpStatus.BAD_REQUEST, "error:invalid_update_data", "Invalid update data", "Email already in use"),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "error:invalid_refresh_token", "Invalid refresh token", "Token is expired"),
    DATA_INTEGRITY_VIOLATION(HttpStatus.CONFLICT, "error:data_inegrity_violation", "Data violation", "Data integrity compromised");

    private final HttpStatus status;
    private final URI uri;
    private final String title;
    private final String detail;

    ErrorData(HttpStatus status, String uri, String title, String detail) {
        this.status = status;
        this.uri = URI.create(uri);
        this.title = title;
        this.detail = detail;
    }
}
