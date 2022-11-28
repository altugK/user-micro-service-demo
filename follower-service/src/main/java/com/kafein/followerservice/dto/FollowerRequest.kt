package com.kafein.followerservice.dto

import com.kafein.followerservice.model.Follower
import java.util.UUID
import javax.validation.constraints.NotNull

data class FollowerRequest @JvmOverloads constructor(
    @NotNull
    val userId: UUID,
    @NotNull
    val followerList: Set<UUID>,
) {
    companion object {
        @JvmStatic
        fun convert(followerRequest: FollowerRequest): Follower {
            return Follower(
                userId = followerRequest.userId,
                followerList = followerRequest.followerList
            )
        }
    }
}