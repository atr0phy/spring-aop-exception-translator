package com.github.atr0phy.springaopexceptiontranslator.domain.user

import com.github.atr0phy.springaopexceptiontranslator.domain.exception.DomainException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.Assertions.assertEquals

class UserNameTest {

    @Nested
    inner class Of {
        
        @Test
        fun `正常な文字列からUserNameを生成できる`() {
            // Given
            val name = "テストユーザー"
            
            // When
            val userName = UserName.of(
                value = name,
            )
            
            // Then
            assertEquals(
                name,
                userName.value,
            )
        }

        @Test
        fun `最小長さ1文字でUserNameを生成できる`() {
            // Given
            val name = "A"
            
            // When
            val userName = UserName.of(
                value = name,
            )
            
            // Then
            assertEquals(
                name,
                userName.value,
            )
        }

        @Test
        fun `最大長さ256文字でUserNameを生成できる`() {
            // Given
            val name = "A".repeat(256)
            
            // When
            val userName = UserName.of(
                value = name,
            )
            
            // Then
            assertEquals(
                name,
                userName.value,
            )
            assertEquals(
                256,
                userName.value.length,
            )
        }

        @Test
        fun `空文字列でDomainExceptionが発生する`() {
            // Given
            val emptyName = ""
            
            // When & Then
            val exception = assertThrows<DomainException> {
                UserName.of(
                value = emptyName,
            )
            }
            assertEquals(
            "ユーザ名は1以上256以下の長さである必要があります",
            exception.message,
        )
        }

        @Test
        fun `257文字でDomainExceptionが発生する`() {
            // Given
            val longName = "A".repeat(257)
            
            // When & Then
            val exception = assertThrows<DomainException> {
                UserName.of(
                value = longName,
            )
            }
            assertEquals(
            "ユーザ名は1以上256以下の長さである必要があります",
            exception.message,
        )
        }

        @Test
        fun `日本語文字列でUserNameを生成できる`() {
            // Given
            val japaneseName = "田中太郎"
            
            // When
            val userName = UserName.of(
                value = japaneseName,
            )
            
            // Then
            assertEquals(
                japaneseName,
                userName.value,
            )
        }

        @Test
        fun `英数字記号でUserNameを生成できる`() {
            // Given
            val alphanumericName = "User123!@#"
            
            // When
            val userName = UserName.of(
                value = alphanumericName,
            )
            
            // Then
            assertEquals(
                alphanumericName,
                userName.value,
            )
        }

        @Test
        fun `スペースを含む文字列でUserNameを生成できる`() {
            // Given
            val nameWithSpace = "テスト ユーザー"
            
            // When
            val userName = UserName.of(
                value = nameWithSpace,
            )
            
            // Then
            assertEquals(
                nameWithSpace,
                userName.value,
            )
        }
    }

    @Nested
    inner class ToString {
        
        @Test
        fun `オーバーライドされたtoString()で文字列値を取得できる`() {
            // Given
            val name = "テストユーザー"
            val userName = UserName.of(
                value = name,
            )
            
            // When
            val result = userName.toString()
            
            // Then
            assertEquals(
                name,
                result,
            )
        }
    }

}