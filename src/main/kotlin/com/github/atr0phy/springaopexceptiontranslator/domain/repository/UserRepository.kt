package com.github.atr0phy.springaopexceptiontranslator.domain.repository

import com.github.atr0phy.springaopexceptiontranslator.domain.user.User
import com.github.atr0phy.springaopexceptiontranslator.domain.user.UserId

interface UserRepository {
    fun findById(id: UserId): User
    fun save(user: User)
}
