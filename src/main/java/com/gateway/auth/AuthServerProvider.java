package com.gateway.auth;

import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthServerProvider implements AuthenticationProvider {

    @Autowired
    private final AuthenticatedServerList authenticatedServerList ;


    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        WebAuthenticationDetails details = (WebAuthenticationDetails) auth.getDetails();
        String userIp = details.getRemoteAddress();
        if (!Arrays.asList(authenticatedServerList.getIpList()).contains(userIp)) {
            throw new BadCredentialsException("Invalid IP Address");
        }
        return auth;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }
}
