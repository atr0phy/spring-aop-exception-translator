# ドメイン層ルール

## 概要
ドメイン層は純粋なビジネスロジックを表現し、技術的な関心事から完全に独立していなければなりません。

## 必須要件

### パッケージ構造
```
domain/
├── exception/     # ドメイン例外
├── repository/    # リポジトリインターフェース
└── [entity]/      # エンティティと値オブジェクト
```

### 依存関係ルール
- ✅ **許可される依存関係**
  - Java標準ライブラリ（`java.util.*`, `java.time.*`）
  - Kotlin標準ライブラリ
  - 他のドメインパッケージ

- ❌ **禁止される依存関係**
  - Springフレームワーク（`org.springframework.*`）
  - インフラストラクチャ層（`..infrastructure..`）
  - アプリケーション層（`..application..`）
  - プレゼンテーション層（`..presentation..`）
  - JPA/Hibernate（`javax.persistence.*`, `org.hibernate.*`）
  - Jackson（`com.fasterxml.jackson.*`）

## Entity実装パターン

### 基本構造
```kotlin
data class User(
    val id: UserId,
    val name: UserName,
) {
    companion object {
        fun of(
            name: UserName,
        ): User {
            return User(
                id = UserId.generate(),
                name = name,
            )
        }
    }
}
```

### ルール
- VOとEnum及びBooleanの型のみでプロパティを定義
- ファクトリメソッドは`of`を使用
- ビジネスルールの検証はエンティティ内で実装

## VO（値オブジェクト）実装パターン

### 基本構造
```kotlin
data class UserName(val value: String) {
    companion object {
        fun of(
            value: String,
        ): UserName {
            return UserName(value = value)
        }
    }
    
    init {
        // バリデーションロジック
        require(value.isNotBlank()) { "UserName cannot be blank" }
    }
}
```

### ルール
- 必ず`of`メソッドを持つ
  - `UUID` の場合は、 `String` を受け取る
- 不変オブジェクトとして設計
- バリデーションは`init`ブロックで実行
- プリミティブ型の単純なラッパーとして設計
- プリミティブな型にする `toXXX` メソッドを持つ(例: `toInt()`)
  - `UUID` の場合は、 `toString` メソッドを持つ(`override` する)

## Repository実装パターン

### インターフェース定義
```kotlin
interface UserRepository {
    fun findById(
        id: UserId,
    ): User

    fun save(
        user: User,
    )
}
```

### ルール
- インフラ層の実装詳細を含まない
- ドメインオブジェクトのみを使用
- 技術的な例外ではなく、ドメイン例外をスロー

## 例外設計

### ドメイン例外
```kotlin
open class DomainException(
    message: String,
) : RuntimeException(message)

// 使用例
class InvalidUserNameException(
    value: String,
) : DomainException(
    "Invalid user name: $value",
)
```

### ルール
- ビジネスルール違反に対してのみ使用
- インフラ層で発生する技術的な例外は含めない
- 継承可能な基底例外クラスを提供

## 禁止事項

### アノテーション
- `@Entity`, `@Table`, `@Column` (JPA)
- `@Component`, `@Service`, `@Repository` (Spring)
- `@JsonProperty`, `@JsonIgnore` (Jackson)

### 外部ライブラリへの依存
- データベース関連ライブラリ
- Webフレームワーク
- シリアライゼーションライブラリ
- ログライブラリ（ドメインロジックでのログ出力）

## チェック方法
ArchUnitテストで自動検証されます。
```bash
./gradlew test --tests "*ArchitectureTest*"
```
