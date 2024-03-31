package com.github.atr0phy.springaopexceptiontranslator.domain.user

import com.github.atr0phy.springaopexceptiontranslator.domain.exception.DomainException

data class UserName(val value: String) {
    companion object {
        private const val MIN_LENGTH = 1
        private const val MAX_LENGTH = 256

        fun of(value: String): UserName {
            return UserName(value)
        }
    }

    init {
        if (MIN_LENGTH > value.length ||
            MAX_LENGTH < value.length) {
            throw DomainException(
                message = "ユーザ名は${MIN_LENGTH}以上${MAX_LENGTH}以下の長さである必要があります",
            )
        }
    }
}
