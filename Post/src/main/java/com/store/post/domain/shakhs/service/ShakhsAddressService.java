package com.store.post.domain.shakhs.service;

import com.store.post.domain.shakhs.dto.request.UserAddressEvent;
import com.store.post.domain.shakhs.dto.response.GetShakhsResponse;
import com.store.post.domain.shakhs.entity.Shakhs;
import com.store.post.domain.shakhs.entity.ShakhsAddress;
import com.store.post.domain.shakhs.mapper.UserAddressEventMapper;
import com.store.post.domain.shakhs.mapper.UserEventMapper;
import com.store.post.domain.shakhs.repository.ShakhsAddressRepository;
import com.store.post.domain.shakhs.repository.ShakhsRepository;
import com.store.post.infra.dto.DebeziumEvent;
import com.store.post.infra.enums.DebeziumOperation;
import org.apache.coyote.BadRequestException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Service
public class ShakhsAddressService {

    private final ShakhsAddressRepository shakhsAddressRepository;
    private final ObjectMapper objectMapper;
    private final UserAddressEventMapper userAddressEventMapper;
    private final ShakhsService shakhsService;

    public ShakhsAddressService(ShakhsAddressRepository shakhsAddressRepository, ObjectMapper objectMapper, UserAddressEventMapper userAddressEventMapper, ShakhsService shakhsService) {
        this.shakhsAddressRepository = shakhsAddressRepository;
        this.objectMapper = objectMapper;
        this.userAddressEventMapper = userAddressEventMapper;
        this.shakhsService = shakhsService;
    }

    @Transactional()
    public void saveShakhsAddress(ShakhsAddress shakhsAddress) {
        ShakhsAddress existingShakhsAddress = shakhsAddressRepository.findById(shakhsAddress.getId()).orElse(null);

        if (existingShakhsAddress == null) {
            shakhsAddressRepository.save(shakhsAddress);
        } else {
            existingShakhsAddress.setAddress(shakhsAddress.getAddress());
            existingShakhsAddress.setPostalCode(shakhsAddress.getPostalCode());
        }
    }

    public void deleteShakhsAddress(ShakhsAddress shakhsAddress) {
        shakhsAddressRepository.delete(shakhsAddress);
    }


    @KafkaListener(
            topics = "users-db.USERS_APP.USERS_ADDRESS",
            groupId = "post-service"
    )
    public void usersAddressConsume(String message) {
        try {
            DebeziumEvent event =
                    objectMapper.readValue(message, DebeziumEvent.class);
            UserAddressEvent userAddressEvent = objectMapper.convertValue(event.getPayload().getAfter(), UserAddressEvent.class);
            DebeziumOperation debOperation = DebeziumOperation.getOperation(event.getPayload().getOp());
            ShakhsAddress shakhsAddress = userAddressEventMapper.mapper(userAddressEvent);
            Shakhs shakhs = shakhsService.getShakhsById(userAddressEvent.getUsersId());
            if (shakhs == null) {
                throw new BadRequestException("Cannot find user");
            }
            shakhsAddress.setShakhs(shakhs);
            if (debOperation != null) {
                switch (debOperation) {
                    case CREATE -> saveShakhsAddress(shakhsAddress);
                    case DELETE -> deleteShakhsAddress(shakhsAddress);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
