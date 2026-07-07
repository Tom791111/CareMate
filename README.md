# 🏥 CareMate 安心伴－智慧長照照護管理系統

> **Java｜Swing｜MVC｜MySQL**

![Java](https://img.shields.io/badge/Java-JDK%208+-orange)
![Swing](https://img.shields.io/badge/GUI-Java%20Swing-blue)
![MySQL](https://img.shields.io/badge/Database-MySQL-4479A1)
![Architecture](https://img.shields.io/badge/Architecture-MVC-success)
![License](https://img.shields.io/badge/License-MIT-green)

---

# 📖 專案介紹

**CareMate（安心伴）** 是一套以 **Java Swing** 開發的智慧長照照護管理系統，採用 **MVC（Model-View-Controller）架構**，並結合 **MySQL** 資料庫，提供家屬、被照顧者及外籍照顧者完整的照護管理平台。

系統整合健康管理、用藥提醒、飲食紀錄、情緒紀錄、照護任務、即時翻譯、視訊通話、緊急求助及照護報表等功能，協助提升長照服務品質與家庭照護效率。

---

# 🎯 專案目標

CareMate 希望解決長照照護中常見的問題：

* 健康紀錄分散且不易保存
* 家屬無法即時掌握照護狀況
* 外籍照顧者語言溝通困難
* 用藥與照護任務容易遺漏
* 缺乏整合式照護資訊平台

透過數位化管理，建立更有效率且更安心的照護流程。

---

# ✨ 系統特色

* 👨‍👩‍👧 多角色登入
* 🌍 多國語言介面
* ❤️ 健康管理
* 💊 用藥提醒
* 🍱 飲食紀錄
* 😊 情緒管理
* 📅 照護行程
* 👥 聯絡人管理
* 🔔 通知提醒
* 🚨 SOS 緊急求助
* 🌐 即時翻譯
* 📹 視訊通話
* 📄 報表列印
* 📊 Dashboard 儀表板
* ⚠️ 未完成紀錄提醒
* 🗄️ MySQL 資料庫

---

# 📋 系統功能

## 👤 會員管理

* 使用者登入
* 使用者註冊
* 個人資料管理
* 身分驗證

---

## 👥 多角色使用者

系統提供四種角色：

* 家屬
* 被照顧者
* 外籍照顧者
* 管理者

---

## ❤️ 健康紀錄

管理：

* 血壓
* 體溫
* 健康歷史
* CRUD 操作

---

## 💊 用藥管理

功能包含：

* 用藥紀錄
* 用藥提醒
* 完成狀態
* 查詢管理

---

## 🍱 飲食管理

紀錄每日：

* 早餐
* 午餐
* 晚餐
* 點心

---

## 😊 心情紀錄

每日心情追蹤：

* 很好
* 好
* 普通
* 差
* 很差

---

## 👥 聯絡人管理

建立：

* 家屬
* 醫療院所
* 看護
* 緊急聯絡人

---

## 📅 照護任務

安排：

* 用藥
* 回診
* 復健
* 運動
* 洗澡
* 量血壓

---

## 🔔 通知提醒

提醒：

* 用藥
* 行程
* 健康檢查
* 未完成紀錄

---

## 🌐 即時翻譯

支援照護溝通：

* 中文
* 印尼文
* 越南文
* 英文

---

## 🚨 SOS 緊急求助

快速建立：

* 緊急事件
* 緊急通知
* 求助紀錄

---

## 📹 視訊通話

提供照護視訊功能：

* 視訊紀錄
* 通話紀錄

---

## 📊 Dashboard

統整：

* 今日任務
* 健康統計
* 用藥提醒
* 未完成項目
* 照護摘要

---

## 📄 報表管理

提供：

* 健康報表
* 照護報表
* 列印功能

---

# 🏗️ 系統架構

本專案採用 MVC 架構。

```text
CareMate
│
├── Controller
├── Service
├── DAO
├── Model
├── Util
├── Exception
└── MySQL Database
```

---

# 📂 專案目錄

```text
src
│
├── controller
├── model
├── dao
├── service
├── util
└── exception
```

---

# 💻 使用技術

| 技術      | 說明         |
| ------- | ---------- |
| Java    | JDK 8+     |
| Swing   | GUI 使用者介面  |
| MVC     | 系統架構       |
| JDBC    | 資料庫連線      |
| MySQL   | 關聯式資料庫     |
| Eclipse | 開發工具       |
| Git     | 版本控制       |
| GitHub  | 專案管理       |
| Figma   | UI / UX 設計 |

---

# 🗃️ 資料庫

主要資料表：

* Member
* HealthRecord
* MedicineRecord
* MealRecord
* MoodRecord
* Contact
* CareTask
* Notification
* EmergencyLog
* TranslationHistory
* ScheduleRecord
* CareLog
* CallLog
* DashboardStats

---

# 🚀 執行方式

## 1.專案

```bash
git clone https://github.com/你的GitHub帳號/CareMate.git
```

---

## 2. 建立資料庫

```sql
CREATE DATABASE caremate;
```

---

## 3. 修改資料庫設定

設定資料庫連線資訊：

```properties
db.url=jdbc:mysql://localhost:3306/caremate
db.user=root
db.password=你的密碼
```

---

## 4. 執行專案

執行：

```text
Main.java
```

即可啟動系統。

---

# 🔮 未來規劃

* AI 健康分析
* AI 照護建議
* Google Calendar 同步
* LINE Notify 提醒
* Android App
* iOS App
* 雲端同步
* AI 語音辨識
* AI 翻譯
* PDF 自動報表

---

# 👨‍💻 作者

**甘少棠**

UI / UX Designer｜Java Developer

**CareMate－智慧長照照護管理系統**

---

# 📄 License

MIT License

Copyright © 2026 KST Studio

