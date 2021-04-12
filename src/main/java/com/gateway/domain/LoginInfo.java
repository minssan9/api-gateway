package com.gateway.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

@Setter
@Getter
@Builder
public class LoginInfo  {

    private String username;
    private String password;
}
