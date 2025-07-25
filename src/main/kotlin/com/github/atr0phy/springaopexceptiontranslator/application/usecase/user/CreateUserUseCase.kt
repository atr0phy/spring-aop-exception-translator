package com.github.atr0phy.springaopexceptiontranslator.application.usecase.user

import com.github.atr0phy.springaopexceptiontranslator.domain.exception.DomainException
import com.github.atr0phy.springaopexceptiontranslator.domain.exception.DomainMappingException
import com.github.atr0phy.springaopexceptiontranslator.domain.exception.UserAlreadyExistsException
import com.github.atr0phy.springaopexceptiontranslator.domain.repository.UserRepository
import com.github.atr0phy.springaopexceptiontranslator.domain.user.User
import com.github.atr0phy.springaopexceptiontranslator.domain.user.UserId
import com.github.atr0phy.springaopexceptiontranslator.domain.user.UserName
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

data class CreateUserInput(
    val name: String,
)

data class CreateUserOutput(
    val id: UserId,
    val name: UserName,
)

@Service
class CreateUserUseCase(
    private val userRepository: UserRepository,
) {
    /**
     * ユーザーを作成する
     *
     * @param input [CreateUserInput]
     * @return [CreateUserOutput]
     * @exception DomainException ユーザー名の長さが規定範囲外の場合
     * @exception DomainMappingException リポジトリ層でドメイン例外が発生した場合
     * @exception UserAlreadyExistsException ユーザーがすでに存在する場合
     */
    @Transactional
    fun handle(
        input: CreateUserInput,
    ): CreateUserOutput {
        val user = User.of(
            name = UserName.of(
                value = input.name,
            ),
        )
        userRepository.save(
            user = user,
        )
        return CreateUserOutput(
            id = user.id,
            name = user.name,
        )
    }
}