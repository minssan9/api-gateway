package com.gateway.auth.service;

import com.gateway.auth.domain.Account;
import org.springframework.stereotype.Service;

@Service
public interface AccountService {
    boolean isExist(String email);

    Account signin(String accountId, String password) throws Exception;

    void updateInfo(String nickname, String password, int userId);
}
