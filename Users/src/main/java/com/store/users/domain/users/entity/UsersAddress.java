package com.store.users.domain.users.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "USERS_ADDRESS")
public class UsersAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_address_seq")
    @SequenceGenerator(
            name = "users_address_seq",
            sequenceName = "USERS_ADDRESS_SEQ",
            allocationSize = 1
    )
    @Column(name = "ID")
    private long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USERS_ID", nullable = false)
    private Users users;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "POSTAL_CODE")
    private String postalCode;

    public UsersAddress(Users users, String address, String postalCode) {
        this.address = address;
        this.postalCode = postalCode;
        this.users = users;
    }
}
