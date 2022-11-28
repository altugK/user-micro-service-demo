package com.kafein.userservice.client;

import com.kafein.userservice.dto.FollowerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "follower-service", path = "/follower")
public interface FollowerServiceClient {

    @GetMapping("/{userId}")
    ResponseEntity<FollowerResponse> findFollowerById(@PathVariable UUID userId);

    @GetMapping
    ResponseEntity<List<FollowerResponse>> findAllFollowers();

}
