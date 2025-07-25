package com.github.atr0phy.springaopexceptiontranslator.domain.exception

import com.github.atr0phy.springaopexceptiontranslator.domain.user.UserId
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

class UserAlreadyExistsExceptionTest {

    @Nested
    inner class Constructor {
        
        @Test
        fun `UserIdからUserAlreadyExistsExceptionを作成できる`() {
            // Given
            val userId = UserId.of(
                value = "550e8400-e29b-41d4-a716-446655440000",
            )
            
            // When
            val exception = UserAlreadyExistsException(
                userId = userId,
            )
            
            // Then
            val expectedMessage = "ユーザがすでに存在します: ${userId.value}"
            assertEquals(
                expectedMessage,
                exception.message,
            )
            assertTrue(
                exception is RuntimeException,
            )
        }


        @Test
        fun `異なるUserIdで異なるメッセージが生成される`() {
            // Given
            val userId1 = UserId.of(
                value = "550e8400-e29b-41d4-a716-446655440000",
            )
            val userId2 = UserId.of(
                value = "550e8400-e29b-41d4-a716-446655440001",
            )
            
            // When
            val exception1 = UserAlreadyExistsException(
                userId = userId1,
            )
            val exception2 = UserAlreadyExistsException(
                userId = userId2,
            )
            
            // Then
            val expectedMessage1 = "ユーザがすでに存在します: ${userId1.value}"
            val expectedMessage2 = "ユーザがすでに存在します: ${userId2.value}"
            
            assertEquals(
                expectedMessage1,
                exception1.message,
            )
            assertEquals(
                expectedMessage2,
                exception2.message,
            )
            // メッセージが異なることを確認
            assertTrue(
                exception1.message != exception2.message,
            )
        }

        @Test
        fun `メッセージにUUID文字列が含まれる`() {
            // Given
            val uuidString = "550e8400-e29b-41d4-a716-446655440000"
            val userId = UserId.of(
                value = uuidString,
            )
            
            // When
            val exception = UserAlreadyExistsException(
                userId = userId,
            )
            
            // Then
            assertTrue(
                exception.message!!.contains(uuidString),
            )
            assertTrue(
                exception.message!!.contains("ユーザがすでに存在します"),
            )
        }
    }
}