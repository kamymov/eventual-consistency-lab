package com.store.post.domain.shakhs.mapper;

import com.store.post.domain.shakhs.dto.request.UserEvent;
import com.store.post.domain.shakhs.entity.Shakhs;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserEventMapper {

    private final ObjectMapper objectMapper;

    public UserEventMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Shakhs mapper(Map<String, Object> data) {
        UserEvent userEvent = objectMapper.convertValue(data, UserEvent.class);
        if (userEvent == null) return null;

        return new Shakhs(
                userEvent.getId(),
                userEvent.getUsername(),
                userEvent.getMobile()
        );
    }

}
