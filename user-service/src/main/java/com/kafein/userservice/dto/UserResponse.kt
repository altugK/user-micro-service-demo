package com.kafein.userservice.dto

import com.kafein.userservice.model.User
import java.util.UUID

data class UserResponse @JvmOverloads constructor(
    val id: UUID,
    val name: String,
    val surname: String,
    val followedList: Set<UUID>? = emptySet(),
    val followerList: Set<UUID>? = emptySet(),
) {
    companion object {
        @JvmStatic
        fun convert(user: User, follower: FollowerResponse): UserResponse {
            return UserResponse(
                id = user.id,
                name = user.name,
                surname = user.surname,
                followedList = user.followedList ?: emptySet(),
                followerList = follower.followerList ?: emptySet(),
            )
        }

        @JvmStatic
        fun convert(user: User): UserResponse {
            return UserResponse(
                id = user.id,
                name = user.name,
                surname = user.surname,
                followedList = user.followedList ?: emptySet(),
                followerList = emptySet(),
            )
        }
    }
}