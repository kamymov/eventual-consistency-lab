package com.store.post.domain.shakhs.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserAddressEvent {

    @JsonProperty("ID")
    private Long id;
    @JsonProperty("ADDRESS")
    private String address;
    @JsonProperty("USERS_ID")
    private Long usersId;
    @JsonProperty("POSTAL_CODE")
    private String postalCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getUsersId() {
        return usersId;
    }

    public void setUsersId(Long usersId) {
        this.usersId = usersId;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
