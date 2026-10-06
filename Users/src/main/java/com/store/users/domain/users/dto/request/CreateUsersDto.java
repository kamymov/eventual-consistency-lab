package com.store.users.domain.users.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public class CreateUsersDto {
    String username;
    String mobile;
    String address;
    String postalCode;

    public String getMobile() {
        return mobile;
    }

    public String getUsername() {
        return username;
    }

    public String getAddress() {
        return address;
    }

    public String getPostalCode() {
        return postalCode;
    }
}
