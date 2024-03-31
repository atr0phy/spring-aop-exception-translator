package com.github.atr0phy.springaopexceptiontranslator.domain.user

import java.util.UUID

data class UserId(val value: UUID) {
    companion object {
        fun generate(): UserId {
            return UserId(UUID.randomUUID())
        }
    }
}

data class User(
    val id: UserId,
    val name: UserName,
) {
    companion object {
        fun of(
            name: UserName,
        ): User {
            return User(
                id = UserId.generate(),
                name = name,
            )
        }
    }
}
