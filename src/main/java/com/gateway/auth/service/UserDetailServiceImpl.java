package com.gateway.auth.service;

import com.gateway.auth.domain.Account;
import com.gateway.auth.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by vivie on 2017-06-08.
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {

	@Autowired
	private AccountRepository accountRepository;

	@Override
	public UserDetails loadUserByUsername(String accountId) throws UsernameNotFoundException {
		Account account = accountRepository.findByUsername(accountId).orElseThrow(()->new UsernameNotFoundException("사용자 정보가 없습니다."));;
		AccountAdapter accountAdapter = new AccountAdapter(account);
//		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
//		User.UserBuilder builder =	User.builder()
//						.passwordEncoder(encoder::encode);

		return accountAdapter;
	}
}
