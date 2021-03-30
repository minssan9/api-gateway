package com.gateway.auth.repository;

import com.gateway.auth.domain.UserPermission;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPermissionRepository extends JpaRepository<UserPermission, Long> {

    List<UserPermission> findByResponsibilityName(String respName);

    List<UserPermission> findByUsername(String userName);

    List<UserPermission> findByAccountId(String accountId);

    List<UserPermission> findByUserId(long userId);

//    List<UserPermission> findByFndResponsibility_Responsibility_name(String respName);

//    @Query("from UserPermission where responsibility_name like %:name%")
//    List<UserPermission> findByQuery(@Param("name") String name);
}
