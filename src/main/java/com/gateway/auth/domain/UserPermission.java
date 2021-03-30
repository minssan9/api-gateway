package com.gateway.auth.domain;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "USER_PERMISSION")
@Getter
@Setter
//@IdClass(UserPermission.class)
public class UserPermission
//    implements Serializable
{
    @Id
    @Column(name = "user_id")
    private long userId;

//    @Id
    @Column(name = "RESPONSIBILITY_ID")
    private String responsibilityId;

    @Column(name = "RESPONSIBILITY_APPLICATION_ID")
    private String responsibilityApplicationId;

    @Column(name = "START_DATE")
    private LocalDate startDate;

    @Column(name = "END_DATE")
    private LocalDate endDate;

//    @Id
    @Column(name = "ACCOUNTID")
    private String accountId;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "SESSION_NUMBER")
    private String sessionNumber;

    @Column(name = "EMPLOYEE_ID")
    private String employeeId;

    @Column(name = "EMAIL_ADDRESS")
    private String emailAddress;

    @Column(name = "WEB_PASSWORD")
    private String webPassword;

    @Column(name = "RESPONSIBILITY_KEY")
    private String responsibilityKey;

    @Column(name = "RESPONSIBILITY_NAME")
    private String responsibilityName;

    @Column(name = "PERMISSION_DESC")
    private String permissionDesc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private Account account;
}
