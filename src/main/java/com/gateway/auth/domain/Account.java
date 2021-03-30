package com.gateway.auth.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "fnd_user")
public class Account {
    @Id
    @Column(name = "user_id")
    private long userId;
    @Column(name = "user_name")
    private String username;
    @Column(name = "last_update_date")
    private String last_update_date;
    @Column(name = "last_updated_by")
    private String last_updated_by;
    @Column(name = "creation_date")
    private String creation_date;
    @Column(name = "created_by")
    private String created_by;
    @Column(name = "last_update_login")
    private String last_update_login;
    @Column(name = "encrypted_foundation_password")
    private String encrypted_foundation_password;
    @Column(name = "encrypted_user_password")
    private String encrypted_user_password;
    @Column(name = "session_number")
    private String session_number;
    @Column(name = "start_date")
    private String start_date;
    @Column(name = "end_date")
    private String end_date;
    @Column(name = "description")
    private String description;
    @Column(name = "last_logon_date")
    private String last_logon_date;
    @Column(name = "password_date")
    private String password_date;
    @Column(name = "password_accesses_left")
    private String password_accesses_left;
    @Column(name = "password_lifespan_accesses")
    private String password_lifespan_accesses;
    @Column(name = "password_lifespan_days")
    private String password_lifespan_days;
    @Column(name = "employee_id")
    private String employee_id;
    @Column(name = "email_address")
    private String email_address;
    @Column(name = "fax")
    private String fax;
    @Column(name = "customer_id")
    private String customer_id;
    @Column(name = "supplier_id")
    private String supplier_id;
    @Column(name = "web_password")
    private String password;
    @Column(name = "user_guid")
    private String user_guid;
    @Column(name = "gcn_code_combination_id")
    private String gcn_code_combination_id;
    @Column(name = "person_party_id")
    private String person_party_id;

    @OneToMany(mappedBy = "account" )
    @JsonIgnore
    private List<UserPermission> userPermissions = new ArrayList<>();

//    public enum AccountRoles{
//
//    }
}
