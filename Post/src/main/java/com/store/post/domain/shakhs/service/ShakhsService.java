package com.store.post.domain.shakhs.service;

import com.store.post.domain.shakhs.dto.response.GetShakhsResponse;
import com.store.post.domain.shakhs.entity.Shakhs;
import com.store.post.domain.shakhs.mapper.UserEventMapper;
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
public class ShakhsService {

    private final ShakhsRepository repository;
    private final ObjectMapper objectMapper;
    private final UserEventMapper userEventMapper;

    public ShakhsService(ShakhsRepository repository,ObjectMapper objectMapper, UserEventMapper userEventMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
        this.userEventMapper = userEventMapper;
    }

    public GetShakhsResponse get() {
        List<Shakhs> shakhs = repository.findAll();

        return new GetShakhsResponse(
                shakhs,
                shakhs.size()
        );
    }

    // Queries

    @Transactional(readOnly = true)
    public Shakhs getShakhsById(Long id) {
        return repository.findById(id).orElse(null);
    }

    // Commands

    @Transactional()
    public Shakhs saveShakhs(Shakhs shakhs) {
        Shakhs oldShakhs = repository.findById(shakhs.getId()).orElse(null);

        if (oldShakhs == null) {
            return repository.save(shakhs);
        }
        oldShakhs.setId(shakhs.getId());
        oldShakhs.setNaam(shakhs.getNaam());
        oldShakhs.setNaamKhanevadegi(shakhs.getNaamKhanevadegi());
        return oldShakhs;
    }

    public void deleteShakhs(Shakhs shakhs) {
        repository.delete(shakhs);
    }

    // Kafka Consumers

    @KafkaListener(
            topics = "users-db.USERS_APP.USERS",
            groupId = "post-service"
    )
    public void usersConsume(String message) {
        try {
            DebeziumEvent event =
                    objectMapper.readValue(message, DebeziumEvent.class);
            Shakhs shakhs = userEventMapper.mapper(event.getPayload().getAfter());
            DebeziumOperation debOperation = DebeziumOperation.getOperation(event.getPayload().getOp());
            if (debOperation != null) {
                switch (debOperation) {
                    case CREATE -> saveShakhs(shakhs);
                    case DELETE -> deleteShakhs(shakhs);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
