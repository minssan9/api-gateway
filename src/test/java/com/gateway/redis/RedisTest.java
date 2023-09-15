package com.gateway.redis;

import org.junit.After;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("develop")
public class RedisTest {

//    @Autowired
//    private final TokenInfoRepository tokenInfoRepository;
//
//    @Autowired
//    private final RedisTemplate redisTemplate;

//    public RedisTest(TokenInfoRepository tokenInfoRepository, RedisTemplate redisTemplate) {
//        this.tokenInfoRepository = tokenInfoRepository;
//        this.redisTemplate = redisTemplate;
//    }

    @After
    public void tearDown() throws Exception {
//        accountRepository.deleteAll();
    }

//    @Test
//    public void 기본_등록_조회기능() {
//        //시작일자
//        TokenInfo tokenInfo = new TokenInfo("P14394", "test_minssan9");
//        // when
//        tokenInfoRepository.save(tokenInfo);
//        final ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
//        valueOperations.set(tokenInfo.getId(), tokenInfo.getToken());
//
//        // then
//        TokenInfo savedToken = tokenInfoRepository.findById(tokenInfo.getId()).get();
//
//    }
//
//    @Test
//    public void 수정기능() {
//        // given
//        String username = "nayoon";
//        Account account= (Account) Account.builder()
//            .username(username)
//            .password("1234")
//            .build();
//
////        accountRepository.save(account);
////
////        // then
////        Account savedAccount = accountRepository.findByUsername(username).get();
////        assertEquals(account.getUsername(), savedAccount.getUsername());
//    }
}

