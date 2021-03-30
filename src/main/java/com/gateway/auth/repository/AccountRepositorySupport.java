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
//public class AccountRepositorySupport  extends QuerydslRepositorySupport {
//    private EntityManager em;
//    private JPAQueryFactory queryFactory;
//
//    public AccountRepositorySupport(JPAQueryFactory queryFactory) {
//        super(Account.class);
//        this.queryFactory = queryFactory;
//    }
//
//    @Override
//    @PersistenceContext(unitName="oracleEntityManager")
//    public void setEntityManager(EntityManager entityManager) {
//        super.setEntityManager(entityManager);
//        em = entityManager;
//        this.queryFactory = new JPAQueryFactory(entityManager);
//    }
//
//    QAccount account = QAccount.account;
//    QUserPermission userPermission = QUserPermission.userPermission;
//
//
//    public Account findByAccountId(String accountId) {
//        Account accounts = from(account)
//            .leftJoin(account.userPermissions, userPermission)
//            .fetchJoin()
//            .where(account.username.eq(accountId))
//            .fetchFirst();
//        return accounts;
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
////    public List<Account> findByAccountId(String accountId) {
////        List<Account> accounts = from(account)
////            .leftJoin(account.userPermissions, userPermission)
////            .on(account.userId.eq(userPermission.userId))
////            .fetchJoin()
////            .limit(2)
////            .where(account.userName.eq(accountId))
////            .fetch();
////
////        return accounts;
////    }
////
////
//    public List<UserPermission> findByPermissionName(String description) {
//        return from(userPermission)
//            .where(userPermission.responsibilityName.like(description))
//            .fetch();
//    }
//
//}
