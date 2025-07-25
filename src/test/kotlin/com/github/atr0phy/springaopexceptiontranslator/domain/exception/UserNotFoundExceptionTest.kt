package com.github.atr0phy.springaopexceptiontranslator.domain.exception

import com.github.atr0phy.springaopexceptiontranslator.domain.user.UserId
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

class UserNotFoundExceptionTest {

    @Nested
    inner class Constructor {
        
        @Test
        fun `UserIdからUserNotFoundExceptionを作成できる`() {
            // Given
            val userId = UserId.of(
                value = "550e8400-e29b-41d4-a716-446655440000",
            )
            
            // When
            val exception = UserNotFoundException(
                userId = userId,
            )
            
            // Then
            val expectedMessage = "ユーザが存在しません: ${userId.value}"
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
            val exception1 = UserNotFoundException(
                userId = userId1,
            )
            val exception2 = UserNotFoundException(
                userId = userId2,
            )
            
            // Then
            val expectedMessage1 = "ユーザが存在しません: ${userId1.value}"
            val expectedMessage2 = "ユーザが存在しません: ${userId2.value}"
            
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
            val exception = UserNotFoundException(
                userId = userId,
            )
            
            // Then
            assertTrue(
                exception.message!!.contains(uuidString),
            )
            assertTrue(
                exception.message!!.contains("ユーザが存在しません"),
            )
        }
    }
}