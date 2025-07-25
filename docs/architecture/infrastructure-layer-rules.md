# インフラストラクチャ層ルール

## 概要
インフラストラクチャ層は技術的な実装詳細を担当し、ドメイン層のインターフェースを具象実装で満たします。

## 必須要件

### パッケージ構造
```
infrastructure/
├── exception/           # AOP例外変換
├── repository/          # リポジトリ実装
├── external/           # 外部API連携
└── config/             # 設定関連
```

### 依存関係ルール
- ✅ **許可される依存関係**
  - ドメイン層（`..domain..`）
  - アプリケーション層（`..application..`）
  - Spring Framework全般
  - JPA/Hibernate
  - 外部ライブラリ
  - データベースドライバ

- ❌ **禁止される依存関係**
  - プレゼンテーション層（`..presentation..`）

## Repository実装パターン

### 基本構造
```kotlin
@Repository
class UserRepositoryImpl : UserRepository {
    
    override fun findById(
        id: UserId,
    ): User {
        return map.getOrElse(id.value) { 
            throw UserNotFoundException(userId = id) 
        }.toUser()
    }

    override fun save(
        user: User,
    ) {
        // 実装詳細
        if(exists(user.id)) {
            throw UserAlreadyExistsException(userId = user.id)
        }
        // 保存処理
    }
}
```

### ルール
- `@Repository`アノテーションを付与
- ドメインインターフェースを実装
- 技術的な例外をスロー（`RuntimeException`継承）
- ドメインオブジェクトとの変換ロジックを含む

## Entity変換パターン

### ドメイン → インフラ変換
```kotlin
data class UserEntity(
    val id: UUID,
    val name: String,
) {
    fun toUser(): User {
        return User(
            id = UserId.of(value = id),
            name = UserName.of(value = name),
        )
    }
    
    companion object {
        fun from(
            user: User,
        ): UserEntity {
            return UserEntity(
                id = user.id.value,
                name = user.name.value,
            )
        }
    }
}
```

### ルール
- インフラエンティティは技術的な詳細を含む
- ドメインオブジェクトとの相互変換メソッドを提供
- JPA/データベース固有のアノテーションを使用可能

## 例外変換（AOP）パターン

### 基本構造
```kotlin
@Aspect
@Component
class RepositoryExceptionTranslator {
    @AfterThrowing(
        value = "@within(org.springframework.stereotype.Repository)", 
        throwing = "e"
    )
    fun translate(
        e: Throwable,
    ) {
        throw when(e) {
            is DomainException -> DomainMappingException(e)
            else -> e
        }
    }
}
```

### ルール
- `@Aspect`, `@Component`アノテーションを使用
- リポジトリからの例外を適切に変換
- ドメイン例外とインフラ例外を区別

## 設定クラスパターン

### データベース設定
```kotlin
@Configuration
@EnableJpaRepositories
class DatabaseConfig {
    // JPA設定
}
```

### ルール
- `@Configuration`でSpring設定を定義
- 技術的な設定のみを含む
- ビジネスロジックは含めない

## 外部API連携パターン

### 基本構造
```kotlin
@Component
class ExternalUserApiClient {
    
    fun fetchUserInfo(
        userId: String,
    ): ExternalUserInfo {
        // 外部API呼び出し
        // エラーハンドリング
        // レスポンス変換
    }
}
```

### ルール
- `@Component`でSpring管理
- 外部システムとの詳細な連携ロジック
- 技術的な例外をスロー

## 使用可能な技術

### アノテーション
- `@Repository`, `@Component`, `@Configuration`
- `@Entity`, `@Table`, `@Column` (JPA)
- `@Transactional` (トランザクション管理)
- `@Aspect`, `@AfterThrowing` (AOP)

### ライブラリ
- Spring Data JPA
- Hibernate
- Jackson (シリアライゼーション)
- データベースドライバ
- 外部ライブラリ

## 例外設計

### インフラ固有例外
```kotlin
class UserNotFoundException(
    userId: UserId,
) : RuntimeException(
    "ユーザが存在しません: ${userId.value}",
)

class DatabaseConnectionException(
    cause: Throwable,
) : RuntimeException(
    "データベース接続エラー",
    cause,
)
```

### ルール
- `RuntimeException`を直接継承
- 技術的な詳細を含む
- インフラの責務に関する例外のみ

## 禁止事項

### アーキテクチャ違反
- ビジネスロジックの実装
- プレゼンテーション層への依存
- ドメインオブジェクトの変更

### セキュリティ
- 認証・認可ロジック（別レイヤーで実装）
- セキュリティ設定の混在

## チェック方法
ArchUnitテストで自動検証されます。
```bash
./gradlew test --tests "*ArchitectureTest*"
```