package com.store.post.domain.shakhs.repository;

import com.store.post.domain.shakhs.entity.ShakhsAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShakhsAddressRepository extends JpaRepository<ShakhsAddress, Long> {
}
