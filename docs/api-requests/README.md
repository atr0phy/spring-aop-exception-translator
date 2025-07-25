# API リクエスト管理

このディレクトリは新しいAPI追加リクエストを管理するためのものです。

## ファイル配置ルール

### 命名規約
```
{HTTPメソッド}-{リソース名}-{操作名}.md
```

### 例
- `DELETE-users-delete.md` - ユーザー削除API
- `PUT-users-update.md` - ユーザー情報更新API
- `GET-users-search.md` - ユーザー検索API
- `POST-orders-create.md` - 注文作成API

## 使用方法

1. **テンプレート使用**
   - [../api-request-template.md](../api-request-template.md) をコピーして使用

2. **ファイル作成**
   - このディレクトリに命名規約に従ってファイル作成
   - 例: `docs/api-requests/DELETE-users-delete.md`

3. **実装依頼**
   - 作成したファイルを Claude に提示して実装を依頼

## ステータス管理

ファイル名の先頭にステータスを付けることで進捗管理が可能です：

- `[TODO] DELETE-users-delete.md` - 未実装
- `[WIP] DELETE-users-delete.md` - 実装中  
- `[DONE] DELETE-users-delete.md` - 実装完了

## 簡易リクエスト

急ぎの場合は、チャットで直接以下の形式で依頼も可能：

```
**API**: DELETE /users/{userId}
**機能**: ユーザー削除
**入力**: userId (パスパラメータ)
**出力**: 成功時204、エラー時404
**処理**: 1)存在確認 2)削除実行 3)レスポンス返却
```