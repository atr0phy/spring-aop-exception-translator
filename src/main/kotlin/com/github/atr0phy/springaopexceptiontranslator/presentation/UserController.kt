package com.github.atr0phy.springaopexceptiontranslator.presentation

import com.github.atr0phy.springaopexceptiontranslator.application.usecase.user.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(
    private val getUserUseCase: GetUserUseCase,
    private val createUserUseCase: CreateUserUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
) {

    data class UserResponse(
        val id: String,
        val name: String,
    ) {
        companion object {
            fun of(
                output: GetUserOutput,
            ): UserResponse {
                return UserResponse(
                    id = output.id.value.toString(),
                    name = output.name.value,
                )
            }

            fun of(
                output: CreateUserOutput,
            ): UserResponse {
                return UserResponse(
                    id = output.id.value.toString(),
                    name = output.name.value,
                )
            }

            fun of(
                output: DeleteUserOutput,
            ): UserResponse {
                return UserResponse(
                    id = output.id.value.toString(),
                    name = output.name.value,
                )
            }
        }
    }

    @GetMapping("/{userId}")
    fun getUser(
        @PathVariable userId: String,
    ): ResponseEntity<UserResponse> {
        val output = getUserUseCase.handle(
            input = GetUserInput(
                userId = userId,
            ),
        )

        return ResponseEntity.ok(
            UserResponse.of(
                output = output,
            ),
        )
    }

    data class CreateUserRequest(
        val name: String,
    )

    @PostMapping
    fun createUser(
        @RequestBody request: CreateUserRequest,
    ): ResponseEntity<UserResponse> {
        val output = createUserUseCase.handle(
            input = CreateUserInput(
                name = request.name,
            ),
        )

        return ResponseEntity.ok(
            UserResponse.of(
                output = output,
            ),
        )
    }

    @DeleteMapping("/{userId}")
    fun deleteUser(
        @PathVariable userId: String,
    ): ResponseEntity<UserResponse> {
        val output = deleteUserUseCase.handle(
            input = DeleteUserInput(
                userId = userId,
            ),
        )

        return ResponseEntity.ok(
            UserResponse.of(
                output = output,
            ),
        )
    }
}
