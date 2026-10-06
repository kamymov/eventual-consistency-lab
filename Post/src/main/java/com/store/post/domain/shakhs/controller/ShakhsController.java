package com.store.post.domain.shakhs.controller;

import com.store.post.domain.shakhs.dto.response.GetShakhsResponse;
import com.store.post.domain.shakhs.service.ShakhsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("shakhs")
public class ShakhsController {

    private final ShakhsService service;

    public ShakhsController(ShakhsService service) {
        this.service = service;
    }

    @GetMapping
    public GetShakhsResponse get() {
        return service.get();
    }
}
