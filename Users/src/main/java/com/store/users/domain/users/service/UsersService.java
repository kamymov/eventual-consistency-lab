package com.store.users.domain.users.service;

import com.store.users.domain.users.dto.request.CreateUsersDto;
import com.store.users.domain.users.dto.response.GetUsersResponse;
import com.store.users.domain.users.entity.Users;
import com.store.users.domain.users.entity.UsersAddress;
import com.store.users.domain.users.repository.UsersAddressRepository;
import com.store.users.domain.users.repository.UsersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsersService {

    private final UsersRepository repository;
    private final UsersAddressService usersAddressService;

    public UsersService(UsersRepository repository, UsersAddressService usersAddressService) {
        this.repository = repository;
        this.usersAddressService = usersAddressService;
    }

    @Transactional(readOnly = true)
    public GetUsersResponse getUsers() {
        List<Users> users = repository.findAll();
        return new GetUsersResponse(users, users.size());
    }


    @Transactional
    public void create(CreateUsersDto body) {

        Users user = repository.save(new Users(
                body.getUsername(),
                body.getMobile()
        ));

        usersAddressService.createAddress(new UsersAddress(
                user,
                body.getAddress(),
                body.getPostalCode()
        ));
    }
}
