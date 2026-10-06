package com.store.users.domain.users.dto.response;


import com.store.users.domain.users.entity.Users;

import java.util.List;

public class GetUsersResponse {

    private List<Users> users;
    private Integer count;

    public GetUsersResponse(List<Users> users, Integer count) {
        this.users = users;
        this.count = count;
    }

    public List<Users> getUsers() {
        return users;
    }

    public void setUsers(List<Users> users) {
        this.users = users;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
