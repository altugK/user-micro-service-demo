package com.kafein.followerservice.model

import org.springframework.data.cassandra.core.mapping.Column
import org.springframework.data.cassandra.core.mapping.PrimaryKey
import org.springframework.data.cassandra.core.mapping.Table
import java.util.UUID

@Table
data class Follower @JvmOverloads constructor(

    @PrimaryKey("user_id")
    val userId: UUID,

    @field:Column("follower_list")
    val followerList: Set<UUID>? = emptySet(),
)