package com.kafein.userservice.service;

import com.kafein.userservice.client.FollowerServiceClient;
import com.kafein.userservice.dto.FollowerResponse;
import com.kafein.userservice.dto.UserRequest;
import com.kafein.userservice.dto.UserResponse;
import com.kafein.userservice.errors.CustomException;
import com.kafein.userservice.model.User;
import com.kafein.userservice.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private static final String USER_TOPIC = "user";

    private final KafkaTemplate<String, UserResponse> kafkaTemplate;

    private final UserRepository userRepository;
    private final FollowerServiceClient followerServiceClient;

    public UserService(KafkaTemplate<String, UserResponse> kafkaTemplate,
                       UserRepository userRepository,
                       FollowerServiceClient followerServiceClient) {
        this.kafkaTemplate = kafkaTemplate;
        this.userRepository = userRepository;
        this.followerServiceClient = followerServiceClient;
    }

    /**
     * {@link User}'ın Kafkaya gönderildiği method. Consumer service karşılar.
     */
    public void send(UserResponse userResponse) {
        kafkaTemplate.send(USER_TOPIC, userResponse.getId().toString(), userResponse);
    }

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<FollowerResponse> userFollowers = followerServiceClient.findAllFollowers().getBody();

        return users.stream().map(user -> UserResponse
                        .convert(user, Objects.requireNonNull(userFollowers)
                                .stream()
                                .filter(userFollower -> Objects.equals(userFollower.getId(), user.getId()))
                                .findFirst()
                                .orElse(new FollowerResponse(user.getId()))))
                .collect(Collectors.toList());

    }

    public UserResponse findById(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));
        FollowerResponse userFollower = followerServiceClient.findFollowerById(userId).getBody();
        return UserResponse.convert(user, userFollower == null ? new FollowerResponse(userId) : userFollower);
    }

    public UserResponse create(UserRequest userRequest) {
        UserResponse userResponse = UserResponse.convert(userRepository.save(UserRequest.convert(userRequest)));
        send(userResponse);
        return userResponse;
    }

    /**
     * Kullanıcı güncelleme methodu.
     */
    public UserResponse update(UUID userId, UserRequest userRequest) {
        var findUser = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));
        User tempUser = new User(findUser.getId(),
                userRequest.getName(), userRequest.getSurname(),
                findUser.getFollowedList(), findUser.getCreatedDate());

        tempUser = mergeUserFollowedListAndRequestFollowedList(tempUser, userRequest);
        var findUserFollower = followerServiceClient.findFollowerById(userId).getBody();

        UserResponse userResponse = UserResponse.convert(userRepository.save(tempUser), findUserFollower);
        send(userResponse);
        return userResponse;
    }

    /**
     * Güncelleme için mevcut takip edilen ve istek listeleri birleştirilir.
     */
    public User mergeUserFollowedListAndRequestFollowedList(User tempUser, UserRequest userRequest) {
        if (!userRequest.isAnyoneFollowed()) return tempUser;

        Set<UUID> followedList = new HashSet<>(tempUser.isAnyoneFollowed() ?
                Objects.requireNonNull(tempUser.getFollowedList()) : new ArrayList<>());

        followedList.addAll(userRequest.getFollowedList());

        return new User(tempUser.getId(),
                userRequest.getName(), userRequest.getSurname(),
                followedList, tempUser.getCreatedDate());
    }
}
