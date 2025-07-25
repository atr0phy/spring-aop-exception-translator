package com.github.atr0phy.springaopexceptiontranslator.domain.exception

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

class DomainExceptionTest {

    @Nested
    inner class Constructor {
        
        @Test
        fun `メッセージ付きでDomainExceptionを作成できる`() {
            // Given
            val message = "ドメインエラーが発生しました"
            
            // When
            val exception = DomainException(
                message = message,
            )
            
            // Then
            assertEquals(
                message,
                exception.message,
            )
            assertTrue(
                exception is RuntimeException,
            )
        }


        @Test
        fun `空文字列メッセージで作成できる`() {
            // Given
            val emptyMessage = ""
            
            // When
            val exception = DomainException(
                message = emptyMessage,
            )
            
            // Then
            assertEquals(
                emptyMessage,
                exception.message,
            )
        }

        @Test
        fun `日本語メッセージで作成できる`() {
            // Given
            val japaneseMessage = "ビジネスルール違反：無効な操作です"
            
            // When
            val exception = DomainException(
                message = japaneseMessage,
            )
            
            // Then
            assertEquals(
                japaneseMessage,
                exception.message,
            )
        }
    }
}