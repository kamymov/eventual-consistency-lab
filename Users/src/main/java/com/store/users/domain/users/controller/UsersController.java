package com.store.users.domain.users.controller;

import com.store.users.domain.users.dto.request.CreateUsersDto;
import com.store.users.domain.users.dto.response.GetUsersResponse;
import com.store.users.domain.users.service.UsersService;
import org.springframework.web.bind.annotation.*;

@RequestMapping("users")
@RestController
public class UsersController {

    private final UsersService service;

    public UsersController(UsersService service) {
        this.service = service;
    }

    @PostMapping
    public void create(@RequestBody CreateUsersDto body) {
        service.create(body);
    }

    @GetMapping
    public GetUsersResponse getUsers() {
        return service.getUsers();
    }
}
