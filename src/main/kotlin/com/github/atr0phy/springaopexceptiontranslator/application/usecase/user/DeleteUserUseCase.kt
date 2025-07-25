package com.github.atr0phy.springaopexceptiontranslator.application.usecase.user

import com.github.atr0phy.springaopexceptiontranslator.domain.exception.UserNotFoundException
import com.github.atr0phy.springaopexceptiontranslator.domain.repository.UserRepository
import com.github.atr0phy.springaopexceptiontranslator.domain.user.User
import com.github.atr0phy.springaopexceptiontranslator.domain.user.UserId
import com.github.atr0phy.springaopexceptiontranslator.domain.user.UserName
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DeleteUserUseCase(
    private val userRepository: UserRepository,
) {
    /**
     * ユーザーを削除する
     *
     * @param input [DeleteUserInput]
     * @return [DeleteUserOutput]
     * @exception UserNotFoundException ユーザーが見つからない場合
     */
    @Transactional
    fun handle(
        input: DeleteUserInput,
    ): DeleteUserOutput {
        val userId = UserId.of(
            value = input.userId,
        )
        
        val user = userRepository.findByIdForUpdate(
            id = userId,
        )
        
        userRepository.delete(
            id = userId,
        )
        
        return DeleteUserOutput(
            id = user.id,
            name = user.name,
        )
    }
}

data class DeleteUserInput(
    val userId: String,
)

data class DeleteUserOutput(
    val id: UserId,
    val name: UserName,
)