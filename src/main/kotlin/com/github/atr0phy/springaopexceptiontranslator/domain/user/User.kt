package com.github.atr0phy.springaopexceptiontranslator.domain.user

import java.util.UUID

data class UserId(val value: UUID) {
    companion object {
        fun of(
            value: String,
        ): UserId {
            return UserId(value = UUID.fromString(value))
        }

        fun generate(): UserId {
            return UserId(value = UUID.randomUUID())
        }
    }

    override fun toString(): String {
        return value.toString()
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
