//package com.hallahds.gateway.auth.repository;
//
//import com.hallahds.gateway.auth.domain.UserPermission;
//import java.util.List;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public class UserPermissionRepositorySupport extends QuerydslRepositorySupport {
//    private EntityManager em;
//    private JPAQueryFactory queryFactory;
//
//    QAccount account = QAccount.account;
//    QUserPermission userPermission = QUserPermission.userPermission;
//
//    @Override
//    public void setEntityManager(EntityManager entityManager) {
//        super.setEntityManager(entityManager);
//        em = entityManager;
//        this.queryFactory = new JPAQueryFactory(entityManager);
//    }
//
//    public UserPermissionRepositorySupport() {
//        super(UserPermission.class);
//    }
//
//    public List<UserPermission> findByAccountId(String accountId) {
//        List<UserPermission> permissions = from(userPermission)
//            .where(userPermission.username.eq(accountId))
//            .fetch();
//
//        return permissions;
//    }
//
//    public List<UserPermission> findByName(String description) {
//        List<UserPermission> userPermissions = from(userPermission)
//            .where(account.description.eq(description))
//            .fetch();
//
//        return userPermissions;
//    }
//
//    public List<UserPermission> findByPermissionName(String description) {
//        return from(userPermission)
//            .where(userPermission.responsibilityName.like(description))
//            .fetch();
//    }
//}
