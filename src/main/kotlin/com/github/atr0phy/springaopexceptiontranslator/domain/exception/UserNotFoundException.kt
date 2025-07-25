package com.github.atr0phy.springaopexceptiontranslator.domain.exception

import com.github.atr0phy.springaopexceptiontranslator.domain.user.UserId

class UserNotFoundException(
    userId: UserId,
) : RuntimeException(
    "ユーザが存在しません: ${userId.value}",
)