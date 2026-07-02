# CareMate Final v5（畢業專題正式版）

CareMate 長照照護管理系統，依 Figma App 風格、MVC + DAO Pattern、Maven Project 製作。

## 開發環境

- JDK 11
- MySQL 8.0
- Eclipse Java SE + WindowBuilder
- Maven Project
- MVC + DAO Pattern + Service Layer

## 專案架構

```text
src/main/java
├── controller       Swing JFrame / WindowBuilder 畫面
├── model            資料模型
├── dao              DAO 介面
├── dao.impl         DAO MySQL 實作
├── service          Service 介面
├── service.impl     商業邏輯與驗證
├── util             DBUtil、UIUtil、DateTimeUtil
└── exception        AppException、ValidationException
```

## Final v5 修正重點

- 修正 JDBC URL 造成 `Malformed database URL` 的問題。
- DBUtil 會自動修正常見錯誤 URL，並提供中文錯誤訊息。
- 程式啟動時會自動建立 `caremate_db` 與核心資料表。
- 註冊、登入、CRUD、照顧安排、聯絡人、SOS 頁面整合。
- 保留 Figma 手機版卡片式 UI、快捷功能 icon、Bottom Navigation。

## Eclipse 匯入方式

1. Eclipse → File → Import
2. Maven → Existing Maven Projects
3. Root Directory 選擇 `CareMate_Final_v5`
4. Finish
5. 右鍵專案 → Maven → Update Project
6. 執行 `controller.Main`

## MySQL 設定

請確認 MySQL Server 已啟動。

設定檔位置：

```text
src/main/resources/db.properties
```

預設內容：

```properties
db.url=jdbc:mysql://localhost:3306/caremate_db?useSSL=false&serverTimezone=Asia/Taipei&allowPublicKeyRetrieval=true&characterEncoding=UTF-8
db.user=root
db.password=1234
```

如果你的 MySQL 密碼不是 `1234`，請修改 `db.password`。

## 測試帳號

| 角色 | 帳號 | 密碼 |
|---|---|---|
| 家屬 | family | 1234 |
| 被照顧者 | elder | 1234 |
| 外籍照顧者 | caregiver | 1234 |
| 管理者 | admin | 1234 |

## 手動建立資料庫

若不想使用自動建立，可在 MySQL Workbench 執行：

```text
src/main/sql/schema.sql
```

## 常見錯誤

### Access denied

MySQL 密碼錯誤，請修改 `db.password`。

### Communications link failure

MySQL Server 尚未啟動。

### Malformed database URL

請使用下列格式：

```properties
db.url=jdbc:mysql://localhost:3306/caremate_db?useSSL=false&serverTimezone=Asia/Taipei&allowPublicKeyRetrieval=true&characterEncoding=UTF-8
```

## Dashboard 2.0 動態首頁

本版首頁不再使用固定的 `75%`、`健康分數 85` 假資料，而是由 MySQL 即時計算：

- 今日完成率：依健康、用藥、飲食、情緒、今日任務完成狀態計算。
- 健康分數：依血壓、體溫、用藥、飲食、情緒加權計算。
- 進度條：使用 `JProgressBar.setValue()` 依資料庫統計更新。
- 即時照護數據：顯示最近血壓、體溫、用藥、飲食、情緒。
- 7 日趨勢：首頁右下角顯示最近 7 天簡易趨勢線。

### 如何測試 Dashboard 會變動

1. 登入 `family / 1234`。
2. 到「健康」、「用藥」、「飲食」、「情緒」、「任務」頁新增或修改資料。
3. 回首頁按「重新整理」。
4. 完成率、健康分數、進度條與即時資料會依 MySQL 資料更新。

用藥與任務若要被計算為完成，狀態欄請填入：`已完成`、`完成`、`COMPLETED` 或 `DONE`。

## v5.3 更新：照顧安排 Calendar 2.0

照顧安排頁已升級成類 Apple 行事曆操作：

- 可切換上個月 / 下個月 / 回到今天
- 點選日期後，可新增該日期的照護安排
- 可填寫內容、地點/類型、開始時間、結束時間、備註
- 可點選當日安排表格資料並修改、刪除
- 資料會寫入 MySQL `schedule` 資料表
- 月曆日期有資料時會顯示橘色圓點提示
