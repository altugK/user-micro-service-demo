package com.kafein.followerservice.dto

import com.kafein.followerservice.model.Follower
import java.util.UUID

data class FollowerResponse @JvmOverloads constructor(
    val id: UUID,
    val followerList: Set<UUID>,
) {
    companion object {
        @JvmStatic
        fun convert(follower: Follower): FollowerResponse {
            return FollowerResponse(
                id = follower.userId,
                followerList = follower.followerList ?: emptySet()
            )
        }
    }
}