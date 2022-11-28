package com.kafein.userservice.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import com.kafein.userservice.model.User
import java.util.UUID
import javax.validation.constraints.NotNull

data class UserRequest @JvmOverloads constructor(
    @NotNull
    val name: String,
    @NotNull
    val surname: String,

    val followedList: ArrayList<UUID>,
) {
    companion object {
        @JvmStatic
        fun convert(userRequest: UserRequest): User {
            return User(
                name = userRequest.name,
                surname = userRequest.surname,
                followedList = userRequest.followedList.toHashSet()
            )
        }
    }

    @JsonIgnore
    fun isAnyoneFollowed(): Boolean {
        return !this.followedList.isNullOrEmpty();
    }

}