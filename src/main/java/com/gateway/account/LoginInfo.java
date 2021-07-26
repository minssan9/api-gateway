package com.gateway.account;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class LoginInfo  {

    private String username;
    private String password;
}
