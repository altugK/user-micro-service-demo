package com.kafein.userservice.dto

import java.util.UUID

data class FollowerResponse @JvmOverloads constructor(
    val id: UUID? = null,
    val followerList: Set<UUID>? = emptySet(),
)