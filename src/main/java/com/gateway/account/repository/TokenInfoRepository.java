package com.gateway.account.repository;

import com.gateway.account.domain.Account;
import com.gateway.account.domain.TokenInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenInfoRepository extends CrudRepository<TokenInfo, String> {
    TokenInfo findByToken(String token);

    void deleteByToken(String token);
}
