package com.demo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNSPECIFIED_EXCEPTION(
            "An exception has occurred", HttpStatus.INTERNAL_SERVER_ERROR), // for catching generic exceptions
    ENTITY_NOT_FOUND("Entity does not exist in the database", HttpStatus.NOT_FOUND);

    private final String errorDesc;
    private final HttpStatus statusCode;
}
