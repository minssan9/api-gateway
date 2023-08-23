package com.gateway.config.handler;

import com.gateway.config.exception.CommonException;
import com.gateway.config.exception.CommonExceptionType;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
@Component
public class GatewayExceptionHandler implements ErrorWebExceptionHandler {
    private String errorCodeMaker(int errorCode) {
        return "{\"errorCode\":" + errorCode +"}";
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        log.warn("in GATEWAY Exeptionhandler : " + ex);
        CommonExceptionType commonExceptionType = CommonExceptionType.INTERNAL_SERVER_ERROR;

        if (ex.getClass() == NullPointerException.class) {
            commonExceptionType = CommonExceptionType.NULL_POINTER_EXCEPTION;
        } else if (ex.getClass() == ExpiredJwtException.class) {
            commonExceptionType =  CommonExceptionType.TOKEN_EXPIRED;
        } else if (ex.getClass() == MalformedJwtException.class || ex.getClass() == SignatureException.class || ex.getClass() == UnsupportedJwtException.class) {
            commonExceptionType =  CommonExceptionType.TOKEN_MALFORMED;
        } else if (ex.getClass() == IllegalArgumentException.class) {
            commonExceptionType =   CommonExceptionType.INVALID_PARAMS;
        } else if (ex.getClass() == CommonException.class) {
            commonExceptionType = ((CommonException) ex).getType();
        }

        byte[] bytes = (commonExceptionType.getErrorCode() + " / " + commonExceptionType.getErrorMessage())
                .getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        return exchange.getResponse().writeWith(Flux.just(buffer));
    }
}
