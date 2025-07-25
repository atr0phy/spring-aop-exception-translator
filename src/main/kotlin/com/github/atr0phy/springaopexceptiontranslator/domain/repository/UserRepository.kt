package com.github.atr0phy.springaopexceptiontranslator.domain.repository

import com.github.atr0phy.springaopexceptiontranslator.domain.exception.UserAlreadyExistsException
import com.github.atr0phy.springaopexceptiontranslator.domain.exception.UserNotFoundException
import com.github.atr0phy.springaopexceptiontranslator.domain.user.User
import com.github.atr0phy.springaopexceptiontranslator.domain.user.UserId

interface UserRepository {
    /**
     * ユーザーIDでユーザーを検索する
     *
     * @param id [UserId]
     * @return [User]
     * @exception UserNotFoundException ユーザーが見つからない場合
     */
    fun findById(
        id: UserId,
    ): User

    /**
     * ユーザーを保存する
     *
     * @param user [User]
     * @exception UserAlreadyExistsException ユーザーがすでに存在する場合
     */
    fun save(
        user: User,
    )

    /**
     * ユーザーIDでユーザーを排他ロックで検索する
     *
     * @param id [UserId]
     * @return [User]
     * @exception UserNotFoundException ユーザーが見つからない場合
     */
    fun findByIdForUpdate(
        id: UserId,
    ): User

    /**
     * ユーザーを削除する
     *
     * @param id [UserId]
     * @exception UserNotFoundException ユーザーが見つからない場合
     */
    fun delete(
        id: UserId,
    )
}
