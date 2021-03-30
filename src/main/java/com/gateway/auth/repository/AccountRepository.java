package com.gateway.auth.repository;

import com.gateway.auth.domain.Account;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findByUserId(long userId);

//    @Query(value = "select * from FND_USER a where a.user_name = :userName ", nativeQuery = true)
     Optional<Account> findByUsername(String accountId);

//    @Query(value = "select * from FND_USER a where a.description like '%' || :userName || '%' ", nativeQuery = true)
    List<Account> findByDescription(String accountName);

    List<Account> findByDescriptionLike(String userName);

//    @Query("from UserPermission where responsibility_name like %:name%")
//    List<Account> findByQuery( @Param(value = "name") String name);
}
