package com.gateway.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash("account")
public class Account implements Serializable {

    @Id
    private String id;
    private Long amount;
    private LocalDateTime refreshTime;
}
