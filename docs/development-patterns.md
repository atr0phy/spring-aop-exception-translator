# 開発パターン

## VO（値オブジェクト）パターン

### 基本実装
- 必ず`of`メソッドを持つ
- 不変オブジェクトとして設計
- バリデーションは`init`ブロックで実行

```kotlin
data class UserName(val value: String) {
    companion object {
        fun of(value: String): UserName {
            return UserName(value = value)
        }
    }
    
    init {
        require(value.isNotBlank()) { "UserName cannot be blank" }
    }
}
```

## Entity（エンティティ）パターン

### 基本実装
- VOとEnum及びBooleanの型のみでクラスのプロパティを定義
- ファクトリメソッドは`of`を使用

```kotlin
data class User(
    val id: UserId,
    val name: UserName,
) {
    companion object {
        fun of(name: UserName): User {
            return User(
                id = UserId.generate(),
                name = name,
            )
        }
    }
}
```

## UseCase（ユースケース）パターン

### 基本実装
- `@Service`アノテーションを付与
- メソッド名は常に`handle`を使用
- トランザクション境界を管理

```kotlin
@Service
class GetUserUseCase(
    private val userRepository: UserRepository,
) {
    /**
     * ユーザー情報を取得する
     *
     * @param input [GetUserInput]
     * @return [GetUserOutput]
     * @exception UserNotFoundException ユーザーが見つからない場合
     */
    @Transactional(readOnly = true)
    fun handle(
        input: GetUserInput,
    ): GetUserOutput {
        val user = userRepository.findById(
            id = UserId.of(
                value = UUID.fromString(input.userId),
            ),
        )
        return GetUserOutput(
            id = user.id,
            name = user.name,
        )
    }
}
```

### トランザクション管理
- 読み取り専用の場合: `@Transactional(readOnly = true)`
- 書き込み処理の場合: `@Transactional`

### Input/Output設計
- **Input**: PrimitiveまたはEnum classの型のみ使用
- **Output**: ドメイン層の型を使用してOK
- VOのconstructor呼び出しはせず、`of`を使用

```kotlin
// Input - Primitiveな型のみ
data class GetUserInput(
    val userId: String,
)

// Output - ドメイン型を使用
data class GetUserOutput(
    val id: UserId,
    val name: UserName,
)
```

### UseCaseテンプレート
```kotlin
@Service
class [Action][Entity]UseCase(
    private val repository: [Entity]Repository,
) {
    @Transactional
    fun handle(input: [Action][Entity]Input): [Action][Entity]Output {
        // ビジネスロジック実装
        // Outputは直接コンストラクタで生成
        return [Action][Entity]Output(
            // プロパティマッピング
        )
    }
}

// Input/Outputは外部に定義
data class [Action][Entity]Input(
    val property: Type,
)

data class [Action][Entity]Output(
    val property: Type,
)
```

## Controller（コントローラ）パターン

### 基本実装
- UseCaseのOutputをResponseクラスにマッピング
- ResponseクラスはCompanion objectで変換メソッドを提供

```kotlin
@RestController
@RequestMapping("/users")
class UserController(
    private val getUserUseCase: GetUserUseCase,
) {
    
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
}
```

### Response設計
- ユーザー定義の型からPrimitiveな型に変換
- Companion objectで変換メソッドを提供

```kotlin
data class UserResponse(
    val id: String,
    val name: String,
) {
    companion object {
        fun of(output: GetUserOutput): UserResponse {
            return UserResponse(
                id = output.id.value.toString(),
                name = output.name.value,
            )
        }
    }
}
```

## Repository（リポジトリ）パターン

### インターフェース定義（ドメイン層）
```kotlin
interface UserRepository {
    fun findById(id: UserId): User
    fun save(user: User)
}
```

### 実装（インフラ層）
```kotlin
@Repository
class UserRepositoryImpl : UserRepository {
    
    override fun findById(id: UserId): User {
        return map.getOrElse(id.value) { 
            throw UserNotFoundException(userId = id) 
        }.toUser()
    }

    override fun save(user: User) {
        if(exists(user.id)) {
            throw UserAlreadyExistsException(userId = user.id)
        }
        // 保存処理
    }
}
```