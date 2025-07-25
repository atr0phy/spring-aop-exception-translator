# プレゼンテーション層ルール

## 概要
プレゼンテーション層はHTTPリクエスト/レスポンスの処理を担当し、外部とのインターフェースを提供します。

## 必須要件

### パッケージ構造
```
presentation/
├── controller/          # REST APIコントローラ
├── dto/                # データ転送オブジェクト（必要な場合）
├── exception/          # プレゼンテーション例外ハンドラ
└── config/             # Web設定
```

### 依存関係ルール
- ✅ **許可される依存関係**
  - アプリケーション層（`..application..`）
  - Spring Web関連
  - Java/Kotlin標準ライブラリ
  - バリデーション関連

- ❌ **禁止される依存関係**
  - ドメイン層への直接アクセス（`..domain..`の具象クラス）
  - インフラストラクチャ層（`..infrastructure..`）
  - リポジトリの直接注入

## Controller実装パターン

### 基本構造
```kotlin
@RestController
@RequestMapping("/users")
class UserController(
    private val getUserUseCase: GetUserUseCase,
    private val createUserUseCase: CreateUserUseCase,
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

### ルール
- `@RestController`でREST APIを定義
- 引数は1行に1つずつ記述、末尾カンマ
- UseCaseのみに依存（Repositoryは直接使用禁止）
- ResponseEntityを使用してHTTPステータスを明示

## Request/Response実装パターン

### Request定義
```kotlin
data class CreateUserRequest(
    @field:NotBlank
    val name: String,
)
```

### Response定義
```kotlin
data class UserResponse(
    val id: String,
    val name: String,
) {
    companion object {
        fun of(
            output: GetUserOutput,
        ): UserResponse {
            return UserResponse(
                id = output.id.value.toString(),
                name = output.name.value,
            )
        }
    }
}
```

### ルール
- RequestはプリミティブIのみ使用
- Responseはプリミティブ型に変換
- UseCaseのInput/Outputと分離
- Companion objectで変換メソッドを提供（`of`メソッド）

## バリデーション

### パターン
```kotlin
data class CreateUserRequest(
    @field:NotBlank(message = "ユーザー名は必須です")
    @field:Size(min = 1, max = 100, message = "ユーザー名は1-100文字で入力してください")
    val name: String,
)

@PostMapping
fun createUser(
    @Valid @RequestBody request: CreateUserRequest,
): ResponseEntity<UserResponse> {
    // 実装
}
```

### ルール
- Bean Validationを使用
- `@Valid`でバリデーション実行
- エラーメッセージは日本語で提供
- ビジネスルールバリデーションはUseCaseで実行

## 例外ハンドリング

### グローバル例外ハンドラ
```kotlin
@RestControllerAdvice
class GlobalExceptionHandler {
    
    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFound(
        ex: UserNotFoundException,
    ): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(
                ErrorResponse(
                    message = ex.message ?: "ユーザーが見つかりません",
                    code = "USER_NOT_FOUND",
                ),
            )
    }
    
    @ExceptionHandler(ValidationException::class)
    fun handleValidation(
        ex: ValidationException,
    ): ResponseEntity<ErrorResponse> {
        return ResponseEntity.badRequest()
            .body(
                ErrorResponse(
                    message = ex.message ?: "入力値に誤りがあります",
                    code = "VALIDATION_ERROR",
                ),
            )
    }
}
```

### ルール
- `@RestControllerAdvice`でグローバル例外処理
- 適切なHTTPステータスコードを設定
- 統一されたエラーレスポンス形式
- セキュリティ上、内部エラー詳細は隠蔽

## エラーレスポンス設計

### 基本形式
```kotlin
data class ErrorResponse(
    val message: String,
    val code: String,
    val timestamp: String = LocalDateTime.now().toString(),
)

data class ValidationErrorResponse(
    val message: String,
    val code: String,
    val errors: List<FieldError>,
    val timestamp: String = LocalDateTime.now().toString(),
)

data class FieldError(
    val field: String,
    val message: String,
)
```

### ルール
- 一貫したエラーレスポンス形式
- エラーコードで詳細分類
- タイムスタンプを含める
- バリデーションエラーは詳細情報を含める

## セキュリティ考慮事項

### 基本原則
- センシティブ情報の漏洩防止
- 適切な入力値検証
- SQL/XSSインジェクション対策
- レート制限の実装

### パターン
```kotlin
@RestController
@RequestMapping("/api/v1/users")
class UserController {
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(
        @Valid @RequestBody request: CreateUserRequest,
        authentication: Authentication, // 認証情報
    ): UserResponse {
        // セキュリティチェック
        // ビジネスロジック実行
    }
}
```

## 使用可能なアノテーション

### Spring Web
- `@RestController`, `@Controller`
- `@RequestMapping`, `@GetMapping`, `@PostMapping`
- `@PathVariable`, `@RequestParam`, `@RequestBody`
- `@ResponseStatus`, `@ResponseBody`

### Validation
- `@Valid`, `@Validated`
- `@NotNull`, `@NotBlank`, `@Size`
- `@Pattern`, `@Email`, `@Min`, `@Max`

### Exception Handling
- `@ExceptionHandler`, `@RestControllerAdvice`

## 禁止事項

### アーキテクチャ違反
- ビジネスロジックの実装
- データベースアクセス
- ドメインオブジェクトの直接使用（Response以外）
- トランザクション管理

### セキュリティリスク
- 認証なしでの機密操作
- SQLインジェクション可能な実装
- XSS脆弱性のある実装

### パフォーマンス
- N+1クエリ問題
- 不要な重い処理

## KDoc除外ルール
Controllerクラスのメソッドには原則KDocを書かない（CLAUDE.md参照）

## チェック方法
ArchUnitテストで自動検証されます。
```bash
./gradlew test --tests "*ArchitectureTest*"
```