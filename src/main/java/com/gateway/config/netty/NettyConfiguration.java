package com.gateway.config.netty;

import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class NettyConfiguration
        implements WebServerFactoryCustomizer<NettyReactiveWebServerFactory> {

    private final int maxInBytes = 1048576; // 1M

//    디폴트 설정 정보 확인가능. ServerProperties
//    public NettyConfiguration(ServerProperties serverProperties) {
//        maxInBytes = (int) serverProperties.getMaxHttpHeaderSize().toBytes(); -default 8192
//    }

    @Override
    public void customize(NettyReactiveWebServerFactory factory) {
    	log.info("maxHttpHeaderSize : " + maxInBytes);
//        factory.addServerCustomizers();
    }
}
