package com.kafein.userservice.model

import org.springframework.data.cassandra.core.mapping.Column
import org.springframework.data.cassandra.core.mapping.PrimaryKey
import org.springframework.data.cassandra.core.mapping.Table
import java.time.LocalDateTime
import java.util.UUID

@Table
data class User @JvmOverloads constructor(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),

    val name: String,

    val surname: String,

    @field:Column("followed_list")
    val followedList: Set<UUID>? = emptySet(),

    @field:Column("created_date")
    val createdDate: LocalDateTime? = LocalDateTime.now()
) {
    fun isAnyoneFollowed(): Boolean {
        return !this.followedList.isNullOrEmpty();
    }
}