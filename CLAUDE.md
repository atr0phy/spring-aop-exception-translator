# CLAUDE.md

このファイルは、このリポジトリでコードを扱う際のClaude Code (claude.ai/code)へのガイダンスを提供します。

## プロジェクト概要

これはKotlinで書かれたSpring Boot 3.2.4 REST APIアプリケーションで、Spring AOP（アスペクト指向プログラミング）を使用した例外変換を実演しています。このプロジェクトは、レイヤー境界でドメイン例外をインフラストラクチャ例外に自動的に変換する方法を示しています。

## 開発ルール

このプロジェクトでは、統一された開発スタイルとパターンを維持するため、以下のドキュメントに従って開発してください。

- **[コーディングスタイル](docs/coding-style.md)** - Kotlin、KDoc、ネーミングルール
- **[開発パターン](docs/development-patterns.md)** - VO、Entity、UseCase、Controller実装パターン

## 実行コマンド

ビルド、テスト、開発に必要なコマンドについては **[コマンド一覧](docs/commands.md)** を参照してください。

## アーキテクチャ

このアプリケーションは、レイヤードアーキテクチャによるドメイン駆動設計に従っています：

### レイヤー構造とファイル配置
```
src/main/kotlin/com/github/atr0phy/springaopexceptiontranslator/
├── domain/                    # ドメインレイヤー：純粋なビジネスロジック
│   ├── exception/            # ドメイン固有の例外
│   │   ├── DomainException
│   │   └── UserNotFoundException
│   ├── repository/           # リポジトリインターフェース（ドメイン契約）
│   │   └── UserRepository
│   └── user/                 # ユーザー関連のドメインモデル
│       ├── User              # エンティティ
│       ├── UserId            # 値オブジェクト
│       └── UserName          # 値オブジェクト
│
├── application/              # アプリケーションレイヤー：ユースケース
│   └── usecase/             # 各機能のユースケース
│       ├── GetUserUseCase.kt     # ユーザー取得（Input/Output含む）
│       └── CreateUserUseCase.kt  # ユーザー作成（Input/Output含む）
│
├── infrastructure/           # インフラストラクチャレイヤー：技術的実装
│   ├── exception/           # AOP例外変換
│   │   └── RepositoryExceptionTranslator
│   └── repository/          # リポジトリ実装
│       └── UserRepositoryImpl
│
└── presentation/            # プレゼンテーションレイヤー：HTTP処理
    ├── UserController       # REST APIエンドポイント
    └── ExceptionHandler     # グローバル例外ハンドリング
```

### 主要なアーキテクチャパターン

#### AOP例外変換パターン
Spring AOPの`@AfterThrowing`を使用してリポジトリ境界でドメイン例外を自動変換します。詳細は [開発パターン](docs/development-patterns.md) を参照。

### 技術スタック
- **言語**: Kotlin 1.9.23
- **フレームワーク**: Spring Boot 3.2.4
- **Javaバージョン**: 17
- **ビルドツール**: Gradle 8.7（Kotlin DSL使用）
- **主要な依存関係**: Spring Web、Spring AOP、Jackson Kotlinモジュール

## 開発ガイド

新機能開発、テスト作成、デバッグについては **[開発ガイド](docs/development-guide.md)** を参照してください。

## アーキテクチャ管理

### レイヤー別ルール
各レイヤーの詳細なルールと制約については、以下のファイルを参照してください：

- [ドメイン層ルール](docs/architecture/domain-layer-rules.md)
- [アプリケーション層ルール](docs/architecture/application-layer-rules.md)
- [インフラストラクチャ層ルール](docs/architecture/infrastructure-layer-rules.md)
- [プレゼンテーション層ルール](docs/architecture/presentation-layer-rules.md)

### ArchUnitテスト
アーキテクチャルールの遵守は、ArchUnitを使用して自動テストで検証されます：

```bash
# アーキテクチャテストの実行
./gradlew test --tests "*ArchitectureTest*"

# 全体テストの実行
./gradlew test
```

### レイヤールール違反の検出
- 依存関係の循環参照
- 禁止されたパッケージ間の依存
- 不適切なアノテーション使用
- 命名規約違反
