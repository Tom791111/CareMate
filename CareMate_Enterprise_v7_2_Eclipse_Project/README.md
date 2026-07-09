# CareMate Enterprise v7.2 International Edition

CareMate 是以 Java Swing / Eclipse / Maven / MySQL 製作的長照照護管理系統。

## v7.2 重點

- 三角色登入：家屬、被照顧者、外籍照顧者
- 外籍照顧者語言選擇
- 新增 Filipino / Tagalog
- 多語系資源檔架構 ResourceBundle
- Dashboard、醫療服務中心、預約看診、Google Maps 導航、翻譯、視訊通話、照護紀錄等模組

## 開發環境

- JDK 11
- MySQL 8.0
- Eclipse Java SE + WindowBuilder
- Maven Project
- MVC + DAO Pattern + Service Layer

## MySQL

請先執行：

1. `Database/schema.sql`
2. `Database/migration_v7_medical_center.sql`
3. `Database/migration_v7_2_international.sql`

若已有資料庫，請先備份後再執行 migration。

## 測試帳號

請參考：`Documents/測試帳號.md`

## 匯入 Eclipse

匯入資料夾：

`CareMate_Enterprise_v7_2_Eclipse_Project`

執行：

`src/main/java/controller/Main.java`
