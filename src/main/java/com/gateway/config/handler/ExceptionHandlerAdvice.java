package com.gateway.config.handler;

import com.gateway.config.exception.CommonException;
import com.gateway.config.exception.CommonExceptionType;
import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

// TODO : For the admin, we need to add a controller advice with a corresponded exception.
@Slf4j
@RestControllerAdvice
public class ExceptionHandlerAdvice {
//    @ExceptionHandler(DataIntegrityViolationException.class)
//    public Map<String, Object> duplicateEx(Exception e) {
//        log.warn("DataIntegrityViolationException" + e.getClass());
//        Map<String, Object> map = new HashMap<>();
//        map.put("errorCode", 53);
//        return map;
//    }
//
//    @ExceptionHandler(BadCredentialsException.class)
//    public Map<String, Object> badCredentialEx(Exception e) {
//        log.warn("BadCredentialsException");
//        Map<String, Object> map = new HashMap<>();
//        map.put("errorCode", 63);
//        return map;
//    }

    @ExceptionHandler({
        IllegalArgumentException.class, MissingServletRequestParameterException.class})
    public Map<String, Object> paramsEx(Exception e) {
        log.warn("params ex: "+ e);
        Map<String, Object> map = new HashMap<>();
        map.put("errorCode", 51);
        return map;
    }

    @ExceptionHandler(NullPointerException.class)
    public Map<String, Object> nullEx(Exception e) {
        log.warn("null ex" + e.getClass());
        Map<String, Object> map = new HashMap<>();
        map.put("errorCode", 61);
        return map;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception e) {
        log.error(e.getMessage(), e);
        final ExceptionResponse response = new ExceptionResponse(
            CommonExceptionType.INTERNAL_SERVER_ERROR, e);

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponse> handleRuntimeException(RuntimeException e) {
        log.error(e.getMessage(), e);
        final ExceptionResponse response = new ExceptionResponse(
            CommonExceptionType.INTERNAL_SERVER_ERROR, e);

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CommonException.class)
    public ResponseEntity<ExceptionResponse> handleCustomInternalException(
        CommonException e) {
        log.error(e.getMessage(), e);

        return new ResponseEntity<>(new ExceptionResponse(e), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // When @RequestBody (HttpMessageConverter) cannot bind
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException", e);
        final ExceptionResponse response = new ExceptionResponse(
            CommonExceptionType.INTERNAL_SERVER_ERROR, e);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // When @RequestParam cannot bind an enum type
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ExceptionResponse> handleMethodArgumentTypeMismatchException(
        MethodArgumentTypeMismatchException e) {
        log.error("MethodArgumentTypeMismatchException", e);
        final ExceptionResponse response = new ExceptionResponse(
            CommonExceptionType.INTERNAL_SERVER_ERROR, e);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // There is no handler
    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<ExceptionResponse> handleNoHandlerFoundException(
        NoHandlerFoundException e) {
        log.error("NoHandlerFoundException", e);
        final ExceptionResponse response = new ExceptionResponse(
            CommonExceptionType.INTERNAL_SERVER_ERROR, e);

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // There is no http method
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ExceptionResponse> handleHttpRequestMethodNotSupportedException(
        HttpRequestMethodNotSupportedException e) {
        log.error("HttpRequestMethodNotSupportedException", e);
        final ExceptionResponse response = new ExceptionResponse(
            CommonExceptionType.INTERNAL_SERVER_ERROR, e);

        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }

    // Authentication object doesn't have any authority
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ExceptionResponse> handleAccessDeniedException(
        AccessDeniedException e) {
        log.error("AccessDeniedException", e);
        final ExceptionResponse response = new ExceptionResponse(
            CommonExceptionType.INTERNAL_SERVER_ERROR, e);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
