package com.github.atr0phy.springaopexceptiontranslator.presentation

import com.github.atr0phy.springaopexceptiontranslator.domain.repository.UserRepository
import com.github.atr0phy.springaopexceptiontranslator.domain.user.User
import com.github.atr0phy.springaopexceptiontranslator.domain.user.UserId
import com.github.atr0phy.springaopexceptiontranslator.domain.user.UserName
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID


// application layerはわざと省略
@RestController
@RequestMapping("/users")
class UserController(
    private val userRepository: UserRepository,
) {

    data class UserResponse(
        val id: String,
        val name: String,
    ) {
        companion object {
            fun of(user: User): UserResponse {
                return with(user) {
                    UserResponse(
                        id = id.value.toString(),
                        name = name.value,
                    )
                }
            }
        }
    }

    @GetMapping("/{userId}")
    fun getUser(
        @PathVariable userId: String,
    ): ResponseEntity<UserResponse> {
        val user = userRepository.findById(UserId(UUID.fromString(userId)))

        return ResponseEntity.ok(
            UserResponse.of(user)
        )
    }

    data class CreateUserRequest(
        val name: String,
    )

    @PostMapping
    fun createUser(
        @RequestBody request: CreateUserRequest,
    ): ResponseEntity<UserResponse> {
        val user = User.of(
            UserName.of(request.name)
        )

        userRepository.save(user)

        return ResponseEntity.ok(
            UserResponse.of(user)
        )
    }
}
