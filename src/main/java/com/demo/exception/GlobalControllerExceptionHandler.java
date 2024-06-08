package com.demo.exception;

import com.demo.enums.ErrorCode;
import com.demo.response.ApiError;
import com.demo.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.logging.LogLevel;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientResponseException;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalControllerExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    @ExceptionHandler(AppException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<?>> customExceptionHandling(AppException ex, HttpServletRequest request) {
        List<ApiError> errors = new ArrayList<>();
        ApiError error = new ApiError(ex.getErrorCode(), ex.getErrorDesc());
        errors.add(error);
        printErrorLogMessage(error, LogLevel.WARN, ex.getMessage(), getFullURL(request), null);
        ApiResponse<?> apiResponse = new ApiResponse<>(null, errors, null);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(apiResponse, headers, ex.getStatusCode());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<?>> handleAnyOtherException(Exception ex, HttpServletRequest request) {
        ApiResponse<?> apiResponse = handleAnyOtherExceptionHelper(ex, getFullURL(request));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(apiResponse, headers, HttpStatus.BAD_REQUEST);
    }

    public static ApiResponse<?> handleAnyOtherExceptionHelper(Throwable throwable, String fullUrl) {
        List<ApiError> errors = new ArrayList<>();
        ApiError error =
                new ApiError(ErrorCode.UNSPECIFIED_EXCEPTION.name(), ErrorCode.UNSPECIFIED_EXCEPTION.getErrorDesc());
        errors.add(error);
        if (throwable
                instanceof
                RestClientResponseException
                restClientResponseException) { // for errors in calling out using RestTemplate, include the call
            // response in the logs for easier debugging
            String apiResponse = restClientResponseException.getResponseBodyAsString();
            apiResponse =
                    apiResponse.replaceAll("\n", " "); // remove line breaks so it appears as one log message in Kibana
            printErrorLogMessage(
                    error,
                    LogLevel.ERROR,
                    throwable.getMessage() + " |  The API response was: " + apiResponse,
                    fullUrl,
                    throwable); // LogLevel.ERROR since these are unexpected, and should get sent to Sentry, and over
            // time we should catch more and more of them with specific handlers up top
        } else {
            printErrorLogMessage(
                    error,
                    LogLevel.ERROR,
                    throwable.getMessage(),
                    fullUrl,
                    throwable); // LogLevel.ERROR since these are unexpected, and should get sent to Sentry, and over
            // time we should catch more and more of them with specific handlers up top
        }
        return new ApiResponse<>(null, errors, null);
    }

    private static void printErrorLogMessage(
            ApiError error, LogLevel logLevel, String exceptionMessage, String requestUrl, Throwable throwable) {
        String message = "Returning {}, description: '{}', at url {}, internal message: '{}'";

        if (logLevel == LogLevel.ERROR) {
            LOGGER.error(message, error.getCode(), error.getDescription(), requestUrl, exceptionMessage, throwable);
        } else if (logLevel == LogLevel.WARN) {
            LOGGER.warn(message, error.getCode(), error.getDescription(), requestUrl, exceptionMessage, throwable);
        } else {
            throw new UnsupportedOperationException("Can only log at ERROR or WARN levels");
        }
    }

    private String getFullURL(HttpServletRequest request) {
        StringBuilder url = new StringBuilder();
        url.append(request.getMethod()).append(' ').append(request.getRequestURL());
        if (request.getQueryString() != null) {
            url.append('?').append(request.getQueryString());
        }
        return url.toString();
    }
}
