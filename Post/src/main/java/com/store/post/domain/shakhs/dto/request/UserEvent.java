package com.store.post.domain.shakhs.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserEvent {

    @JsonProperty("ID")
    private Long id;

    @JsonProperty("USERNAME")
    private String username;

    @JsonProperty("MOBILE")
    private String mobile;

    public UserEvent() {
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getMobile() {
        return mobile;
    }
}