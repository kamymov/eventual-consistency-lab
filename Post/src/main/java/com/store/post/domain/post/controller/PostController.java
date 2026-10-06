package com.store.post.domain.post.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("post")
public class PostController {

    @PostMapping
    public String create() {
        return "create successfully";
    }

    @GetMapping
    public String get() {
        return "get";
    }
}
