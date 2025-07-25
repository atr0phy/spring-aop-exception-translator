# 開発ガイド

## 新機能開発フロー

### 新しいUseCaseを追加する場合

1. **パッケージ作成**
   - `application/usecase/`に`{対象}`の意味合いに近いパッケージがなければ作成

2. **UseCaseクラス作成**
   - `application/usecase/{対象の意味合いに近いパッケージ}`に`{動詞}{対象}UseCase.kt`ファイルを作成
   - Input/Outputデータクラスを同じファイルの外側に定義

3. **実装**
   - `@Service`アノテーションを付けてUseCaseクラスを実装
   - `handle`メソッドでビジネスロジックを実装
   - 適切な`@Transactional`アノテーションを付与

4. **Controller連携**
   - Controllerに新しいUseCaseを注入して使用
   - ResponseクラスでOutput→Primitiveな型の変換を実装

### 実装例
```kotlin
// 1. UseCase実装
@Service
class UpdateUserUseCase(
    private val userRepository: UserRepository,
) {
    @Transactional
    fun handle(input: UpdateUserInput): UpdateUserOutput {
        // 実装
    }
}

// 2. Input/Output定義
data class UpdateUserInput(
    val userId: String,
    val name: String,
)

data class UpdateUserOutput(
    val id: UserId,
    val name: UserName,
)

// 3. Controller連携
@RestController
class UserController(
    private val updateUserUseCase: UpdateUserUseCase,
) {
    @PutMapping("/{userId}")
    fun updateUser(
        @PathVariable userId: String,
        @RequestBody request: UpdateUserRequest,
    ): ResponseEntity<UserResponse> {
        // 実装
    }
}
```

## テスト作成ガイド

### テスト戦略
- **Unit Test**: 各レイヤーの単体テスト
- **Integration Test**: レイヤー間の連携テスト
- **Architecture Test**: アーキテクチャルールの検証

### UseCaseテスト
```kotlin
@ExtendWith(MockitoExtension::class)
class GetUserUseCaseTest {
    
    @Mock
    private lateinit var userRepository: UserRepository
    
    @InjectMocks
    private lateinit var getUserUseCase: GetUserUseCase
    
    @Test
    fun `正常にユーザーを取得できる`() {
        // Given
        val userId = UserId.of(UUID.randomUUID())
        val user = User(id = userId, name = UserName.of("test"))
        `when`(userRepository.findById(userId)).thenReturn(user)
        
        // When
        val result = getUserUseCase.handle(
            GetUserInput(userId = userId.value.toString())
        )
        
        // Then
        assertEquals(userId, result.id)
        assertEquals(user.name, result.name)
    }
}
```

### テスト作成時の注意点
- UseCaseのテストではモックを使用してRepositoryをモック化
- Input/Outputの変換ロジックもテスト対象とする
- 例外ケースも必ずテストする
- ArchUnitテストでアーキテクチャルールを検証

## アーキテクチャ準拠チェック

### 事前チェック項目
新しいコードを追加する前に以下を確認：

1. **適切なレイヤーに配置されているか**
   - ドメインロジック → Domain層
   - ユースケース → Application層
   - 技術的実装 → Infrastructure層
   - HTTP処理 → Presentation層

2. **依存関係が正しいか**
   - 上位レイヤーから下位レイヤーへの依存のみ
   - ドメイン層は他のレイヤーに依存しない

3. **命名規約に従っているか**
   - UseCase: `{動詞}{対象}UseCase`
   - Input/Output: `{UseCase名}Input/Output`

### 自動検証
```bash
# アーキテクチャテストで自動検証
./gradlew test --tests "*ArchitectureTest*"
```

## デバッグとトラブルシューティング

### よくある問題

#### 1. アーキテクチャテストが失敗する
```bash
# 詳細なエラー情報を確認
./gradlew test --tests "*ArchitectureTest*" --info
```

**対処法:**
- 依存関係を確認し、適切なレイヤーに移動
- 禁止されているアノテーションを削除
- 命名規約を修正

#### 2. トランザクションが効かない
**対処法:**
- `@Transactional`アノテーションが付いているか確認
- `@Service`アノテーションが付いているか確認
- ProxyAOPが有効になっているか確認

#### 3. 例外が適切に変換されない
**対処法:**
- RepositoryExceptionTranslatorが動作しているか確認
- `@Repository`アノテーションが付いているか確認
- AOP設定が有効になっているか確認

### ログ出力とデバッグ
```kotlin
// ログ出力例（アプリケーション層以上で使用）
private val logger = LoggerFactory.getLogger(this::class.java)

@Transactional
fun handle(input: CreateUserInput): CreateUserOutput {
    logger.debug("Creating user: ${input.name}")
    // 実装
    logger.info("User created successfully: ${result.id}")
    return result
}
```