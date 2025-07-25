# アプリケーション層ルール

## 概要
アプリケーション層はユースケースを実装し、ドメインオブジェクトのオーケストレーションとトランザクション境界を管理します。

## 必須要件

### パッケージ構造
```
application/
└── usecase/
    └── [entity]/          # エンティティ別のユースケース
        ├── GetEntityUseCase.kt
        ├── CreateEntityUseCase.kt
        └── ...
```

### 依存関係ルール
- ✅ **許可される依存関係**
  - ドメイン層（`..domain..`）
  - Spring Core（`@Service`, `@Transactional`）
  - Java/Kotlin標準ライブラリ

- ❌ **禁止される依存関係**
  - インフラストラクチャ層の具象クラス（`..infrastructure..`の実装）
  - プレゼンテーション層（`..presentation..`）
  - Web関連（`@Controller`, `@RestController`）
  - JPA Entityクラス

## UseCase実装パターン

### 基本構造
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
     * @exception DomainMappingException リポジトリ層でドメイン例外が発生した場合
     * @exception UserNotFoundException ユーザーが見つからない場合
     */
    @Transactional(readOnly = true)
    fun handle(
        input: GetUserInput,
    ): GetUserOutput {
        // ビジネスロジックの実装
    }
}
```

### ルール
- クラス名: `{動詞}{対象}UseCase`
- メソッド名: 常に`handle`を使用
- `@Service`アノテーションを付与
- トランザクション境界の管理（`@Transactional`）
- ReadOnlyな場合は`@Transactional(readOnly = true)`を使用

## Input/Output実装パターン

### Input定義
```kotlin
data class GetUserInput(
    val userId: String,
)
```

### Output定義
```kotlin
data class GetUserOutput(
    val id: UserId,
    val name: UserName,
)
```

### ルール
- InputはPrimitiveまたはEnum classの型のみ使用
- Outputのプロパティにはドメイン層の型を使用OK
- VOのconstructor呼び出しはせず、`of`を使用
- Input/Outputクラス名: `{UseCase名}Input`, `{UseCase名}Output`

## トランザクション管理

### 基本パターン
```kotlin
@Transactional  // 書き込み処理
fun handle(input: CreateUserInput): CreateUserOutput

@Transactional(readOnly = true)  // 読み取り専用
fun handle(input: GetUserInput): GetUserOutput
```

### ルール
- すべてのUseCaseメソッドに`@Transactional`を付与
- 読み取り専用操作には`readOnly = true`を設定
- トランザクション境界はUseCaseレベルで管理

## 例外処理

### パターン
```kotlin
@exception DomainException ビジネスルール違反の場合
@exception UserNotFoundException エンティティが見つからない場合  
@exception DomainMappingException リポジトリ層でドメイン例外が発生した場合
```

### ルール
- ドメイン例外はそのまま伝播
- インフラ例外はAOPで変換される前提
- KDocで発生し得る例外を明記

## 依存性注入

### パターン
```kotlin
@Service
class CreateUserUseCase(
    private val userRepository: UserRepository,  // インターフェースに依存
    private val someService: SomeService,        // 他のアプリケーションサービス
) {
    // 実装
}
```

### ルール
- コンストラクタ注入を使用
- インターフェースに依存（DIP原則）
- 具象クラスへの直接依存は避ける

## 禁止事項

### アーキテクチャ違反
- ドメインロジックをUseCaseに記述
- インフラ層の実装詳細への依存
- プレゼンテーション層の型（DTOなど）の使用

### アノテーション使用制限
- `@Controller`, `@RestController` (Presentation層)
- `@Entity`, `@Repository` (Infrastructure層)
- `@Component` (具体的な役割を示すアノテーションを使用)

## チェック方法
ArchUnitテストで自動検証されます。
```bash
./gradlew test --tests "*ArchitectureTest*"
```