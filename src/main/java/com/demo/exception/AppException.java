package com.demo.exception;

import com.demo.enums.ErrorCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public class AppException extends RuntimeException {

    private static final long serialVersionUID = -3332292346834265371L;
    private final String errorCode;
    private final String errorDesc;
    private final HttpStatus statusCode;

    public AppException(ErrorCode errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode.name();
        this.errorDesc = errorCode.getErrorDesc();
        this.statusCode = errorCode.getStatusCode();
    }

    public AppException(ErrorCode errorCode) {
        super(errorCode.getErrorDesc());
        this.errorCode = errorCode.name();
        this.errorDesc = errorCode.getErrorDesc();
        this.statusCode = errorCode.getStatusCode();
    }
}
