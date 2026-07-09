USE caremate_db;

-- CareMate Enterprise v7.2 International Edition
-- 三角色帳號表新增偏好語言欄位，外籍照顧者登入後可依語言顯示介面。
ALTER TABLE family_account ADD COLUMN preferred_language VARCHAR(20) DEFAULT 'zh_TW';
ALTER TABLE elder_account ADD COLUMN preferred_language VARCHAR(20) DEFAULT 'zh_TW';
ALTER TABLE foreign_caregiver_account ADD COLUMN preferred_language VARCHAR(20) DEFAULT 'zh_TW';

-- 若欄位已存在，可忽略 Duplicate column 錯誤。
-- 建議值：zh_TW, id, vi, fil, en
