package com.github.atr0phy.springaopexceptiontranslator.infrastructure.repository

import com.github.atr0phy.springaopexceptiontranslator.domain.repository.UserRepository
import com.github.atr0phy.springaopexceptiontranslator.domain.user.User
import com.github.atr0phy.springaopexceptiontranslator.domain.user.UserId
import org.springframework.stereotype.Repository
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Repository
class UserRepositoryImpl: UserRepository {
    private val user1 = UserEntity(
        id = UUID.fromString("a687b0bc-0887-483c-8851-4604657162c1"),
        name = "", // なんかしらの理由で元々入っていたとする
    )

    private val user2 = UserEntity(
        id = UUID.fromString("bb1304ea-8d78-4b99-b4d1-ce4000c1c9ab"),
        name = "テストユーザ",
    )

    private val map = ConcurrentHashMap(
        mapOf(
            Pair(
                user1.id,
                user1,
            ),
            Pair(
                user2.id,
                user2,
            ),
        )
    )
    override fun findById(id: UserId): User {
        return map.getOrElse(id.value) { throw RuntimeException("ユーザが存在しません") }
            .toUser()
    }

    override fun save(user: User) {
        val entity = UserEntity(
            id = user.id.value,
            name = user.name.toString(),
        )

        if(map.putIfAbsent(user.id.value, entity) != null) {
            throw RuntimeException("ユーザがすでに存在します")
        }
    }
}
