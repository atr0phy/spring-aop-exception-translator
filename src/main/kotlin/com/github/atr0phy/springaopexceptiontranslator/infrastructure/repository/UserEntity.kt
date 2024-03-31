package com.github.atr0phy.springaopexceptiontranslator.infrastructure.repository

import com.github.atr0phy.springaopexceptiontranslator.domain.user.User
import com.github.atr0phy.springaopexceptiontranslator.domain.user.UserId
import com.github.atr0phy.springaopexceptiontranslator.domain.user.UserName
import java.util.UUID

data class UserEntity (
    val id: UUID,
    val name: String,
) {
    fun toUser(): User {
        return User(
            id = UserId(value = id),
            name = UserName(value = name),
        )
    }
}
