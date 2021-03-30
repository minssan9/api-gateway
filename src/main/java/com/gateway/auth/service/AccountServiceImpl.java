package com.gateway.auth.service;


import com.gateway.auth.domain.Account;
import com.gateway.auth.repository.AccountRepository;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;


    @Override
    public boolean isExist(String userName) {
        return accountRepository.findByUsername(userName)
            .isPresent();
    }


    public Account signin(String accountId, String password) throws Exception {
//        Account account = accountRepositorySupport.findByAccountId(accountId);
        Account account = accountRepository.findByUsername(accountId).get();
        Objects.requireNonNull(account);

        if( ! this.isAccordPassword(account, password)){
            throw new Exception("SIGNIN_EXCEPTION_MSG");
        }

        return account;
    }

    @Override
    public void updateInfo(String nickname, String password, int userId) {
        Account account = accountRepository.findByUserId(userId);
        this.updatePassword(account, password);
        accountRepository.save(account);
    }

    private boolean isAccordPassword(Account account, String password) {
        String encodedPassword = account.getPassword().trim();
//		return BCrypt.checkpw(password, encodedPassword);
        return password.equals(encodedPassword);
    }

    private void updatePassword(Account memberMaster, String password){
        if(!password.equals("")){
            String encodePassword = password;
//            encodePassword = BCrypt.hashpw(password, BCrypt.gensalt());
//            암호화는 나중에
            memberMaster.setPassword(encodePassword);
        }
    }
}
