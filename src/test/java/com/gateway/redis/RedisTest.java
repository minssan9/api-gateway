package com.gateway.redis;

import static org.junit.Assert.assertEquals;

import com.gateway.account.domain.Account;
import com.gateway.account.redis.AccountRepository;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RedisTest {

    @Autowired
    private AccountRepository accountRepository;

    @After
    public void tearDown() throws Exception {
        accountRepository.deleteAll();
    }

    @Test
    public void 기본_등록_조회기능() {
        // given
        String username = "nayoon";

        Account account= (Account) Account.builder()
            .username(username)
            .password("1234")
            .build();

        // when
        accountRepository.save(account);

        // then
        Account savedAccount = accountRepository.findByUsername(username).get();
        assertEquals(savedAccount.getPassword(), "1234");
        assertEquals(savedAccount.getUsername(), username);
    }

    @Test
    public void 수정기능() {
        // given
        String username = "nayoon";
        Account account= (Account) Account.builder()
            .username(username)
            .password("1234")
            .build();

        accountRepository.save(account);


        // then
        Account savedAccount = accountRepository.findByUsername(username).get();
        assertEquals(account.getUsername(), savedAccount.getUsername());
    }
}

