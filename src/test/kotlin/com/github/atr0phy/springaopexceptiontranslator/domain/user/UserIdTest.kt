package com.github.atr0phy.springaopexceptiontranslator.domain.user

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import java.util.UUID

class UserIdTest {

    @Nested
    inner class Of {
        
        @Test
        fun `正常なUUID文字列からUserIdを生成できる`() {
            // Given
            val uuidString = "550e8400-e29b-41d4-a716-446655440000"
            
            // When
            val userId = UserId.of(
                value = uuidString,
            )
            
            // Then
            assertEquals(
                UUID.fromString(uuidString),
                userId.value,
            )
        }

        @Test
        fun `不正なUUID文字列でIllegalArgumentExceptionが発生する`() {
            // Given
            val invalidUuidString = "invalid-uuid-string"
            
            // When & Then
            assertThrows<IllegalArgumentException> {
                UserId.of(
                value = invalidUuidString,
            )
            }
        }

        @Test
        fun `空文字列でIllegalArgumentExceptionが発生する`() {
            // Given
            val emptyString = ""
            
            // When & Then
            assertThrows<IllegalArgumentException> {
                UserId.of(
                value = emptyString,
            )
            }
        }
    }

    @Nested
    inner class Generate {
        
        @Test
        fun `ランダムなUserIdを生成できる`() {
            // When
            val userId1 = UserId.generate()
            val userId2 = UserId.generate()
            
            // Then
            // 異なるUUIDが生成されることを確認（統計的にほぼ確実）
            assertTrue(
                userId1.value != userId2.value,
            )
        }
    }

    @Nested
    inner class ToString {
        
        @Test
        fun `オーバーライドされたtoString()でUUID文字列を取得できる`() {
            // Given
            val uuidString = "550e8400-e29b-41d4-a716-446655440000"
            val userId = UserId.of(
                value = uuidString,
            )
            
            // When
            val result = userId.toString()
            
            // Then
            assertEquals(
                uuidString,
                result,
            )
        }
    }

}