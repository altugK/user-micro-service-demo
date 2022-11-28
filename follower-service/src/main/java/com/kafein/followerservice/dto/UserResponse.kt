package com.kafein.followerservice.dto

import java.util.UUID

data class UserResponse @JvmOverloads constructor(
    val id: UUID,
    val name: String,

    val surname: String,
    val followedList: Set<UUID>? = emptySet(),
    val followerList: Set<UUID>? = emptySet(),
) {
    fun isAnyoneFollowed(): Boolean {
        return !this.followedList.isNullOrEmpty();
    }
}