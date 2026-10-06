package com.store.users.domain.users.service;

import com.store.users.domain.users.entity.UsersAddress;
import com.store.users.domain.users.repository.UsersAddressRepository;
import org.springframework.stereotype.Service;

@Service
public class UsersAddressService {

    private UsersAddressRepository usersAddressRepository;

    public UsersAddressService(UsersAddressRepository usersAddressRepository) {
        this.usersAddressRepository = usersAddressRepository;
    }

    public void createAddress(UsersAddress usersAddress) {
        usersAddressRepository.save(usersAddress);
    }


}
