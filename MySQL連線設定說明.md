# CareMate MySQL 連線設定說明

本版本已設定為 MySQL 8.0 連線版。

## 1. MySQL 連線設定檔
請打開：

```text
CareMate_Eclipse_Project/src/main/resources/db.properties
```

預設內容：

```properties
db.url=jdbc:mysql://localhost:3306/caremate_db?useSSL=false&serverTimezone=Asia/Taipei&allowPublicKeyRetrieval=true&characterEncoding=UTF-8
db.user=root
db.password=1234
username=root
password=1234
```

若你的 MySQL 密碼不是 `1234`，請只改：

```properties
db.password=你的MySQL密碼
password=你的MySQL密碼
```

## 2. 建立資料庫
方式一：讓程式自動建立

本版本的 `DBUtil.java` 已會自動建立 `caremate_db` 與基本資料表。

方式二：手動建立

在 MySQL Workbench 執行：

```text
Database/schema.sql
```

或匯入：

```text
Database/caremate_db_demo.sql
```

## 3. Eclipse 匯入方式

1. Eclipse → File → Import
2. Maven → Existing Maven Projects
3. 選擇：

```text
CareMate_Graduation_Delivery_MySQLConnected/CareMate_Eclipse_Project
```

4. 右鍵專案 → Maven → Update Project
5. 執行：

```text
src/main/java/controller/Main.java
```

## 4. 測試帳號

```text
family / 1234
elder / 1234
caregiver / 1234
admin / 1234
```

## 5. 若連線失敗

常見原因：

- MySQL Server 沒有啟動
- MySQL 密碼不是 1234
- Maven 沒有下載 mysql-connector-j
- db.properties 寫錯 JDBC URL

正確 JDBC URL：

```properties
db.url=jdbc:mysql://localhost:3306/caremate_db?useSSL=false&serverTimezone=Asia/Taipei&allowPublicKeyRetrieval=true&characterEncoding=UTF-8
```
