package com.store.post.domain.shakhs.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "SHAKHS_ADDRESS")
public class ShakhsAddress {
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SHAKHS_ID", nullable = false)
    private Shakhs shakhs;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "POSTAL_CODE")
    private String postalCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Shakhs getShakhs() {
        return shakhs;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public ShakhsAddress() {}

    public ShakhsAddress(Long id, String address, String postalCode) {
        this.id = id;
        this.address = address;
        this.postalCode = postalCode;
    }

    public void setShakhs(Shakhs shakhs) {
        this.shakhs = shakhs;
    }
}
