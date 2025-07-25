# コーディングスタイル

## Kotlinスタイル

### 基本ルール
- `var`は使用禁止、すべて`val`を使用
- `!!`演算子は極力使用しない、適切なnullチェックを行う
- 未使用のインポートは削除する

### クラス・メソッド定義
- data classのプロパティは1行に1つずつ記述し、末尾にカンマを付けて常に改行する
- 引数は1行に1つずつ記述し、末尾にカンマを付けて常に改行する
- メソッドの呼び出し時に引数が1行しかない場合、必ずカンマを付けて改行すること

```kotlin
// Good
data class User(
    val id: UserId,
    val name: UserName,
)

fun createUser(
    name: String,
): User {
    // 実装
}

// Bad
data class User(val id: UserId, val name: UserName)
fun createUser(name: String): User { /* 実装 */ }
```

### Named Arguments
- Kotlinで書かれているメソッドやクラスに関しては、`named arguments`を使用する

```kotlin
// Good
User(
    id = UserId.of(value = uuid),
    name = UserName.of(value = "test"),
)

// Bad
User(UserId.of(uuid), UserName.of("test"))
```

## KDocスタイル

### 記述対象
- Public/Protectedなメソッドには必ずKDocを書く
- `of`メソッドには書かなくて良い
- `Controller`クラスのメソッドには書かなくて良い
- `Entity`のtoXXXメソッドには書かなくて良い

### 記述ルール
- `@param`, `@return`, `@exception`を使用する
- `@exception`はそのメソッドがスローし得る例外を昇順で列挙する
- `@exception`を書いたらその例外を`import`すること
- パラメータの型が自明である場合、`@param`の説明は書かなくて良い

### テンプレート
```kotlin
/**
 * メソッドの説明
 *
 * @param パラメータ名 [パラメータの型]
 * @return [戻り値の型]
 * @exception 例外の型 説明
 */
fun exampleMethod(
    param: String,
): String {
    // 実装
}
```

### 実例
```kotlin
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
    // 実装
}
```

## ネーミング規約

### UseCase
- クラス名: `{動詞}{対象}UseCase`（例: `GetUserUseCase`, `CreateUserUseCase`）
- メソッド名: 常に`handle`を使用
- Input/Outputクラス: `{UseCase名}Input`, `{UseCase名}Output`

### Factory Method
- Companion objectで自分自身のクラスを返す場合のメソッド名は`of`にする

```kotlin
data class UserId(val value: UUID) {
    companion object {
        fun of(value: UUID): UserId {
            return UserId(value = value)
        }
    }
}
```

### テスト
- data class自体で定義されているメソッドのテストは不要
  - `override` している場合は、テストを書く
