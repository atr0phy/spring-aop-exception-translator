package com.github.atr0phy.springaopexceptiontranslator.domain.exception

import com.github.atr0phy.springaopexceptiontranslator.domain.user.UserId

class UserAlreadyExistsException(
    userId: UserId,
) : RuntimeException(
    "ユーザがすでに存在します: ${userId.value}",
)