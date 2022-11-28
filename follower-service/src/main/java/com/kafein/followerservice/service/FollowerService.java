package com.kafein.followerservice.service;

import com.kafein.followerservice.dto.FollowerRequest;
import com.kafein.followerservice.dto.FollowerResponse;
import com.kafein.followerservice.dto.UserResponse;
import com.kafein.followerservice.errors.CustomException;
import com.kafein.followerservice.model.Follower;
import com.kafein.followerservice.repository.FollowerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FollowerService {
    Logger logger = LoggerFactory.getLogger(FollowerService.class);
    private final FollowerRepository followerRepository;

    public FollowerService(FollowerRepository followerRepository) {
        this.followerRepository = followerRepository;
    }

    public FollowerResponse findById(UUID userId) {
        return FollowerResponse.convert(followerRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User Follower not found", HttpStatus.NOT_FOUND)));
    }

    public List<FollowerResponse> getAllUsers() {
        return followerRepository.findAll().stream().map(FollowerResponse::convert).collect(Collectors.toList());
    }

    public FollowerResponse update(FollowerRequest followerRequest) {
        return FollowerResponse.convert(followerRepository.save(FollowerRequest.convert(followerRequest)));
    }

    /**
     * Takip edilenlen kullanıcıların takip edenden kayıt edildiği method. Kafka'dan gelen {@link UserResponse} ile işlem yapılır.
     */
    public void createOrUpdate(UserResponse userResponse) {
        if (!userResponse.isAnyoneFollowed()) {
            logger.info("User is not following anyone. userId: {}", userResponse.getId());
            return;
        }

        Set<UUID> followedList = new HashSet<>(userResponse.getFollowedList());

        followedList.forEach(followedId -> {
            Follower findFollower = followerRepository.findById(followedId)
                    .orElse(new Follower(followedId));
            Set<UUID> newList = new HashSet<>(Objects.requireNonNull(findFollower.getFollowerList()));
            newList.add(userResponse.getId());
            Follower tempFollower = new Follower(findFollower.getUserId(), newList);

            this.followerRepository.save(tempFollower);

            logger.info("User: " + followedId + " is followed by: " + userResponse.getId());
        });

    }
}
