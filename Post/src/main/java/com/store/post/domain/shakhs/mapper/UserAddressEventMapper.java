package com.store.post.domain.shakhs.mapper;

import com.store.post.domain.shakhs.dto.request.UserAddressEvent;
import com.store.post.domain.shakhs.entity.ShakhsAddress;
import com.store.post.domain.shakhs.service.ShakhsService;
import org.springframework.stereotype.Component;

@Component
public class UserAddressEventMapper {


    public ShakhsAddress mapper(UserAddressEvent userAddressEvent) {
        return new ShakhsAddress(
            userAddressEvent.getId(),
                userAddressEvent.getAddress(),
                userAddressEvent.getPostalCode()
        );
    }

}
