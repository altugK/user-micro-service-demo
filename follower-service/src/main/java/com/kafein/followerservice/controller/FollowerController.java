package com.kafein.followerservice.controller;

import com.kafein.followerservice.dto.FollowerRequest;
import com.kafein.followerservice.dto.FollowerResponse;
import com.kafein.followerservice.service.FollowerService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;


import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/follower")
@Validated
public class FollowerController {
    Logger logger = LoggerFactory.getLogger(FollowerController.class);
    private final FollowerService followerService;

    public FollowerController(FollowerService followerService) {
        this.followerService = followerService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<FollowerResponse> findById(@PathVariable UUID userId) {
        return ResponseEntity.ok(followerService.findById(userId));
    }

    @GetMapping
    public ResponseEntity<List<FollowerResponse>> findAll() {
        return ResponseEntity.ok(this.followerService.getAllUsers());
    }

    @PutMapping("/{userId}")
    public ResponseEntity<FollowerResponse> update(@RequestBody @Valid FollowerRequest followerRequest) {
        return ResponseEntity.ok(this.followerService.update(followerRequest));
    }

}
