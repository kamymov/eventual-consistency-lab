package com.store.users.domain.users.repository;

import com.store.users.domain.users.entity.UsersAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersAddressRepository extends JpaRepository<UsersAddress, Long> {
}
