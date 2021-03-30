package com.gateway.auth.service;

import com.gateway.auth.domain.Account;
import com.gateway.auth.domain.UserPermission;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;


public class AccountAdapter extends User {

    private Account account;

    public AccountAdapter(Account account) {
        super(account.getUsername(), account.getPassword(), authorities(account.getUserPermissions()));
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }

    private static Collection<? extends GrantedAuthority> authorities(List<UserPermission> userPermissions) {
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        for (UserPermission accountRoles:userPermissions) {
            authorityList.add(new SimpleGrantedAuthority("ROLE_" + accountRoles.getResponsibilityKey()));
        }
//        roles.stream().forEach(r -> authorityList.add(new SimpleGrantedAuthority("ROLE_" + r.name())));
        return authorityList;
    }
}
