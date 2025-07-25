# コマンド

## ビルドと実行

### テスト実行
```bash
# 全テストの実行
./gradlew test

# 特定のテストクラスの実行
./gradlew test --tests "com.github.atr0phy.springaopexceptiontranslator.*TestClassName"

# アーキテクチャテストの実行
./gradlew test --tests "*ArchitectureTest*"
```

### ビルド
```bash
# クリーンビルド
./gradlew clean build

# ビルドの問題をチェック
./gradlew check

# テストを実行せずにコンパイル
./gradlew compileKotlin
```

### 開発用コマンド
```bash
# アプリケーション起動
./gradlew bootRun

# テストクラスパスで実行（テスト時に便利）
./gradlew bootTestRun

# 依存関係の確認
./gradlew dependencies

# タスク一覧の表示
./gradlew tasks
```

## 品質チェック

### アーキテクチャ検証
```bash
# アーキテクチャルールの検証
./gradlew test --tests "*ArchitectureTest*"
```

### コンパイルエラーチェック
```bash
# Kotlinコンパイル
./gradlew compileKotlin

# テストコンパイル
./gradlew compileTestKotlin
```

## トラブルシューティング

### よくある問題と解決方法

#### ビルドが失敗する場合
```bash
# キャッシュをクリアして再ビルド
./gradlew clean build --refresh-dependencies

# Gradleデーモンを停止して再実行
./gradlew --stop
./gradlew build
```

#### テストが失敗する場合
```bash
# 詳細なテスト結果を表示
./gradlew test --info

# 特定のテストのみ実行
./gradlew test --tests "*TestClassName*"
```

#### 依存関係の問題
```bash
# 依存関係ツリーを表示
./gradlew dependencies --configuration compileClasspath

# 依存関係の競合を確認
./gradlew dependencyInsight --dependency [dependency-name]
```