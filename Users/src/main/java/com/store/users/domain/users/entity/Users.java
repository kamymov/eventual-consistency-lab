package com.store.users.domain.users.entity;

import jakarta.persistence.*;

@Table(name = "USERS")
@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    @SequenceGenerator(
            name = "users_seq",
            sequenceName = "USERS_SEQ",
            allocationSize = 1
    )
    @Column(name = "ID")
    long id;

    @Column(name = "USERNAME")
    String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Column(name = "MOBILE")
    String mobile;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    protected Users() {}

    public Users(String username, String mobile) {
        this.username = username;
        this.mobile = mobile;
    }
}
