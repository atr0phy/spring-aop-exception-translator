package com.github.atr0phy.springaopexceptiontranslator.domain.user

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertNotNull

class UserTest {

    @Nested
    inner class Of {
        
        @Test
        fun `UserNameからUserを生成できる`() {
            // Given
            val userName = UserName.of(
                value = "テストユーザー",
            )
            
            // When
            val user = User.of(
                name = userName,
            )
            
            // Then
            assertNotNull(
                user.id,
            )
            assertEquals(
                userName,
                user.name,
            )
        }

        @Test
        fun `生成されるUserは自動的にIDが割り振られる`() {
            // Given
            val userName = UserName.of(
                value = "テストユーザー",
            )
            
            // When
            val user1 = User.of(name = userName)
            val user2 = User.of(name = userName)
            
            // Then
            assertNotNull(
                user1.id,
            )
            assertNotNull(
                user2.id,
            )
            // 異なるIDが生成されることを確認（統計的にほぼ確実）
            assertTrue(
                user1.id.value != user2.id.value,
            )
        }

        @Test
        fun `様々な文字種の名前でUserを作成できる`() {
            // Given
            val testCases = listOf(
                "田中太郎",
                "John Doe",
                "ユーザー123",
                "User@example.com",
                "Test User!"
            )
            
            testCases.forEach { nameValue ->
                // When
                val userName = UserName.of(
                    value = nameValue,
                )
                val user = User.of(
                    name = userName,
                )
                
                // Then
                assertEquals(
                nameValue,
                user.name.value,
            )
                assertNotNull(
                    user.id,
                )
            }
        }
    }

    @Nested
    inner class Constructor {
        
        @Test
        fun `直接Userを作成できる`() {
            // Given
            val userId = UserId.of(
                value = "550e8400-e29b-41d4-a716-446655440000",
            )
            val userName = UserName.of(
                value = "テストユーザー",
            )
            
            // When
            val user = User(
                id = userId,
                name = userName,
            )
            
            // Then
            assertEquals(
                userId,
                user.id,
            )
            assertEquals(
                userName,
                user.name,
            )
        }

    }

}