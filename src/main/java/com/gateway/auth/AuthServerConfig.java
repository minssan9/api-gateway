package com.gateway.auth;


import com.gateway.auth.service.UserDetailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@RequiredArgsConstructor
@EnableAuthorizationServer
@Configuration
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private final PasswordEncoderCustom passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailServiceImpl userDetailServiceImpl;
    @Autowired
    private final TokenStore tokenStore;
    @Autowired
    private final TokenInfo tokenInfo;

//    @Override
//    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
//        security.passwordEncoder(passwordEncoder);
//
//        // 엔드포인트에 필터를 추가해줘야 토큰 발급시 cors 문제가 발생하지 않는다.
//        CorsFilter filter = new CorsFilter(corsConfigurationSource());
//        security.addTokenEndpointAuthenticationFilter(filter);
//    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
            .withClient(tokenInfo.getClientId())
            .secret(passwordEncoder.encode(tokenInfo.getClientSecret()))
            .authorizedGrantTypes(tokenInfo.getGrantTypes())
            .scopes(tokenInfo.getScopes())
            .accessTokenValiditySeconds(tokenInfo.getAccessTokenValidTime())
            .refreshTokenValiditySeconds(tokenInfo.getRefreshTokenValidTime())
            .autoApprove(true)
        ;
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
            .tokenStore(tokenStore)
            .authenticationManager(authenticationManager)
            .userDetailsService(userDetailServiceImpl);
//            .accessTokenConverter(jwtAccessTokenConverter());
    }

//    @Bean
//    public TokenStore tokenStore() {
//        return new JwtTokenStore(jwtAccessTokenConverter());
//    }

//    @Bean
//    public JwtAccessTokenConverter jwtAccessTokenConverter() {
//        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
//        converter.setSigningKey(tokenInfo.getJwtKey());
//        return converter;
//    }


}

