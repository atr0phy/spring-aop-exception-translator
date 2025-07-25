package com.github.atr0phy.springaopexceptiontranslator.application.usecase.user

import com.github.atr0phy.springaopexceptiontranslator.domain.exception.DomainMappingException
import com.github.atr0phy.springaopexceptiontranslator.domain.exception.UserNotFoundException
import com.github.atr0phy.springaopexceptiontranslator.domain.repository.UserRepository
import com.github.atr0phy.springaopexceptiontranslator.domain.user.UserId
import com.github.atr0phy.springaopexceptiontranslator.domain.user.UserName
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

data class GetUserInput(
    val userId: String,
)

data class GetUserOutput(
    val id: UserId,
    val name: UserName,
)

@Service
class GetUserUseCase(
    private val userRepository: UserRepository,
) {
    /**
     * ユーザー情報を取得する
     *
     * @param input [GetUserInput]
     * @return [GetUserOutput]
     * @exception DomainMappingException リポジトリ層でドメイン例外が発生した場合
     * @exception UserNotFoundException ユーザーが見つからない場合
     */
    @Transactional(readOnly = true)
    fun handle(
        input: GetUserInput,
    ): GetUserOutput {
        val user = userRepository.findById(
            id = UserId.of(
                value = input.userId,
            ),
        )
        return GetUserOutput(
            id = user.id,
            name = user.name,
        )
    }
}