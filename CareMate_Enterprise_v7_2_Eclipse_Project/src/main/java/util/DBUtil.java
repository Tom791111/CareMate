package util;

import java.sql.*;
import java.util.*;
import java.io.*;
import exception.AppException;

/**
 * CareMate Final v5 DBUtil
 * JDK 11 + MySQL 8.0
 * 修正重點：
 * 1. 不再讓錯誤的 JDBC URL 造成整個 Swing 畫面空白。
 * 2. 自動建立 caremate_db 與基本資料表。
 * 3. 提供清楚中文錯誤訊息，方便初學者排除 MySQL 問題。
 */
public class DBUtil {
    private static final Properties props = new Properties();
    private static final String DB_NAME = "caremate_db";

    static {
        try (InputStream in = DBUtil.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in != null) props.load(in);
            Class.forName("com.mysql.cj.jdbc.Driver");
            ensureDatabaseAndTables();
        } catch (Exception e) {
            throw new AppException("資料庫初始化失敗：" + rootMessage(e), e);
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(databaseUrl(), user(), password());
        } catch (SQLException e) {
            throw new AppException("資料庫連線失敗：" + friendly(e), e);
        }
    }

    private static String user() {
        return firstNonBlank("db.user", "username", "user", "root");
    }

    private static String password() {
        return firstNonBlank("db.password", "password", "1234");
    }

    private static String firstNonBlank(String k1, String k2, String def) {
        String v = props.getProperty(k1);
        if (v == null || v.trim().isEmpty()) v = props.getProperty(k2);
        return (v == null || v.trim().isEmpty()) ? def : v.trim();
    }

    private static String firstNonBlank(String k1, String k2, String k3, String def) {
        String v = props.getProperty(k1);
        if (v == null || v.trim().isEmpty()) v = props.getProperty(k2);
        if (v == null || v.trim().isEmpty()) v = props.getProperty(k3);
        return (v == null || v.trim().isEmpty()) ? def : v.trim();
    }

    private static String databaseUrl() {
        String raw = props.getProperty("db.url");
        if (raw == null || raw.trim().isEmpty()) raw = props.getProperty("url");
        return normalizeUrl(raw, true);
    }

    private static String serverUrl() {
        String raw = props.getProperty("db.url");
        if (raw == null || raw.trim().isEmpty()) raw = props.getProperty("url");
        return normalizeUrl(raw, false);
    }

    /**
     * 允許使用者 db.properties 寫錯時自動修正或回預設值。
     * 正確範例：jdbc:mysql://localhost:3306/caremate_db?useSSL=false&serverTimezone=Asia/Taipei&allowPublicKeyRetrieval=true&characterEncoding=UTF-8
     */
    private static String normalizeUrl(String raw, boolean withDatabase) {
        String options = "?useSSL=false&serverTimezone=Asia/Taipei&allowPublicKeyRetrieval=true&characterEncoding=UTF-8";
        String fallback = "jdbc:mysql://localhost:3306/" + (withDatabase ? DB_NAME : "") + options;

        if (raw == null) return fallback;
        String url = raw.trim();
        if (url.isEmpty()) return fallback;

        // 常見打錯：jdbc:mysql//localhost:3306/caremate_db 少冒號
        if (url.startsWith("jdbc:mysql//")) url = url.replaceFirst("jdbc:mysql//", "jdbc:mysql://");
        // 常見打錯：localhost:3306/caremate_db 沒有 jdbc:mysql://
        if (!url.startsWith("jdbc:mysql://")) return fallback;

        try {
            int queryIndex = url.indexOf('?');
            String main = queryIndex >= 0 ? url.substring(0, queryIndex) : url;
            String query = queryIndex >= 0 ? url.substring(queryIndex) : options;

            // 去掉最後 /
            while (main.endsWith("/")) main = main.substring(0, main.length() - 1);

            // main 應為 jdbc:mysql://host:port 或 jdbc:mysql://host:port/db
            String prefix = "jdbc:mysql://";
            String hostPart = main.substring(prefix.length());
            int slash = hostPart.indexOf('/');
            String hostPort = slash >= 0 ? hostPart.substring(0, slash) : hostPart;
            if (hostPort.trim().isEmpty()) return fallback;

            String rebuilt = prefix + hostPort + "/" + (withDatabase ? DB_NAME : "");
            return rebuilt + query;
        } catch (Exception e) {
            return fallback;
        }
    }

    private static void ensureDatabaseAndTables() throws SQLException {
        try (Connection c = DriverManager.getConnection(serverUrl(), user(), password()); Statement st = c.createStatement()) {
            st.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME + " DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
        }

        try (Connection c = DriverManager.getConnection(databaseUrl(), user(), password()); Statement st = c.createStatement()) {
            st.executeUpdate("CREATE TABLE IF NOT EXISTS member(" +
                    "member_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "account VARCHAR(50) UNIQUE NOT NULL," +
                    "password VARCHAR(100) NOT NULL," +
                    "name VARCHAR(50)," +
                    "role VARCHAR(30) NOT NULL," +
                    "phone VARCHAR(30)," +
                    "email VARCHAR(100)," +
                    "status VARCHAR(20) DEFAULT 'ACTIVE'," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)");

            st.executeUpdate("CREATE TABLE IF NOT EXISTS elder(" +
                    "elder_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "member_id INT," +
                    "name VARCHAR(50), gender VARCHAR(20), birthday DATE, note TEXT," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)");

            st.executeUpdate("CREATE TABLE IF NOT EXISTS caregiver(" +
                    "caregiver_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "member_id INT, name VARCHAR(50), nationality VARCHAR(50), language VARCHAR(50)," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)");

            createCrudTable(st, "health_record", "health_id");
            createCrudTable(st, "medicine_record", "medicine_id");
            createCrudTable(st, "meal_record", "meal_id");
            createCrudTable(st, "mood_record", "mood_id");
            createCrudTable(st, "care_task", "task_id");
            createCrudTable(st, "schedule", "schedule_id");
            createCrudTable(st, "contact", "contact_id");
            createCrudTable(st, "notification", "notification_id");
            createCrudTable(st, "emergency_log", "emergency_id");
            createCrudTable(st, "translation_history", "translation_id");
            createCrudTable(st, "call_log", "call_id");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS appointment(" +
                    "appointment_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "member_id INT DEFAULT 1," +
                    "hospital_name VARCHAR(100) NOT NULL," +
                    "department VARCHAR(50)," +
                    "doctor_name VARCHAR(50)," +
                    "appointment_date DATE," +
                    "appointment_time VARCHAR(30)," +
                    "visit_reason VARCHAR(100)," +
                    "status VARCHAR(30) DEFAULT '已預約'," +
                    "note TEXT," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS favorite_hospital(" +
                    "favorite_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "member_id INT DEFAULT 1," +
                    "hospital_name VARCHAR(100) NOT NULL," +
                    "type VARCHAR(30)," +
                    "address VARCHAR(255)," +
                    "phone VARCHAR(30)," +
                    "map_keyword VARCHAR(150)," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS medical_notice(" +
                    "notice_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "member_id INT DEFAULT 1," +
                    "title VARCHAR(100)," +
                    "content TEXT," +
                    "notice_time DATETIME," +
                    "status VARCHAR(30) DEFAULT '未提醒'," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS hospital_history(" +
                    "history_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "member_id INT DEFAULT 1," +
                    "hospital_name VARCHAR(100)," +
                    "department VARCHAR(50)," +
                    "doctor_name VARCHAR(50)," +
                    "visit_date DATE," +
                    "note TEXT," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

            st.executeUpdate("INSERT IGNORE INTO member(account,password,name,role,phone,email,status) VALUES" +
                    "('family','1234','家屬測試帳號','FAMILY','0912000000','family@test.com','ACTIVE')," +
                    "('elder','1234','被照顧者測試帳號','ELDER','0912111111','elder@test.com','ACTIVE')," +
                    "('caregiver','1234','外籍照顧者測試帳號','FOREIGN_CAREGIVER','0912222222','caregiver@test.com','ACTIVE')," +
                    "('admin','1234','管理者','ADMIN','0912333333','admin@test.com','ACTIVE')");
            st.executeUpdate("INSERT IGNORE INTO elder(elder_id,member_id,name,gender,birthday,note) VALUES(1,2,'阿公','男','1945-01-01','測試被照顧者')");
            seedDashboardDemoData(c);
        }
    }

    /**
     * Dashboard 2.0 示範資料：只在資料表完全空白時建立一次。
     * 之後使用者透過 CRUD 新增/修改資料，首頁數據會依 MySQL 真實資料重新計算。
     */
    private static void seedDashboardDemoData(Connection c) throws SQLException {
        if (tableCount(c, "health_record") == 0) {
            try (PreparedStatement ps = c.prepareStatement("INSERT INTO health_record(elder_id,col1,col2,col3,col4,note,record_time) VALUES(1,?,?,?,?,?,DATE_FORMAT(NOW(),'%Y-%m-%dT%H:%i'))")) {
                ps.setString(1, "125"); ps.setString(2, "82"); ps.setString(3, "36.6"); ps.setString(4, "上午"); ps.setString(5, "Dashboard 2.0 示範血壓體溫"); ps.executeUpdate();
            }
        }
        if (tableCount(c, "medicine_record") == 0) {
            try (PreparedStatement ps = c.prepareStatement("INSERT INTO medicine_record(elder_id,col1,col2,col3,col4,note,record_time) VALUES(1,?,?,?,?,?,DATE_FORMAT(NOW(),'%Y-%m-%dT%H:%i'))")) {
                ps.setString(1, "血壓藥"); ps.setString(2, "1顆"); ps.setString(3, "早上"); ps.setString(4, "已完成"); ps.setString(5, "Demo 可於用藥頁修改狀態"); ps.executeUpdate();
            }
        }
        if (tableCount(c, "meal_record") == 0) {
            try (PreparedStatement ps = c.prepareStatement("INSERT INTO meal_record(elder_id,col1,col2,col3,col4,note,record_time) VALUES(1,?,?,?,?,?,DATE_FORMAT(NOW(),'%Y-%m-%dT%H:%i'))")) {
                ps.setString(1, "早餐"); ps.setString(2, "粥、蛋、青菜"); ps.setString(3, "350"); ps.setString(4, "已完成"); ps.setString(5, "Demo 可於飲食頁新增午餐晚餐"); ps.executeUpdate();
            }
        }
        if (tableCount(c, "mood_record") == 0) {
            try (PreparedStatement ps = c.prepareStatement("INSERT INTO mood_record(elder_id,col1,col2,col3,col4,note,record_time) VALUES(1,?,?,?,?,?,DATE_FORMAT(NOW(),'%Y-%m-%dT%H:%i'))")) {
                ps.setString(1, "開心"); ps.setString(2, "4"); ps.setString(3, "良好"); ps.setString(4, "照顧者回報"); ps.setString(5, "Demo 情緒資料"); ps.executeUpdate();
            }
        }
        if (tableCount(c, "care_task") == 0) {
            try (PreparedStatement ps = c.prepareStatement("INSERT INTO care_task(elder_id,col1,col2,col3,col4,note,record_time) VALUES(1,?,?,?,?,?,DATE_FORMAT(NOW(),'%Y-%m-%dT%H:%i'))")) {
                ps.setString(1, "協助散步"); ps.setString(2, "照顧者"); ps.setString(3, "已完成"); ps.setString(4, "日常任務"); ps.setString(5, "Demo 今日任務"); ps.executeUpdate();
            }
        }

        if (tableCount(c, "translation_history") == 0) {
            try (PreparedStatement ps = c.prepareStatement("INSERT INTO translation_history(elder_id,col1,col2,col3,col4,note,record_time) VALUES(1,?,?,?,?,?,DATE_FORMAT(NOW(),'%Y-%m-%dT%H:%i'))")) {
                ps.setString(1, "中文"); ps.setString(2, "印尼文"); ps.setString(3, "請記得吃藥"); ps.setString(4, "Ingat minum obat"); ps.setString(5, "Demo 即時翻譯紀錄"); ps.executeUpdate();
            }
        }
        if (tableCount(c, "call_log") == 0) {
            try (PreparedStatement ps = c.prepareStatement("INSERT INTO call_log(elder_id,col1,col2,col3,col4,note,record_time) VALUES(1,?,?,?,?,?,DATE_FORMAT(NOW(),'%Y-%m-%dT%H:%i'))")) {
                ps.setString(1, "王小明"); ps.setString(2, "視訊"); ps.setString(3, "已完成"); ps.setString(4, "每日照護確認"); ps.setString(5, "Demo 視訊通話紀錄"); ps.executeUpdate();
            }
        }
        if (tableCount(c, "appointment") == 0) {
            try (PreparedStatement ps = c.prepareStatement("INSERT INTO appointment(member_id,hospital_name,department,doctor_name,appointment_date,appointment_time,visit_reason,status,note) VALUES(1,?,?,?,?,?,?,?,?)")) {
                ps.setString(1, "新店慈濟醫院");
                ps.setString(2, "家醫科");
                ps.setString(3, "王醫師");
                ps.setString(4, java.time.LocalDate.now().plusDays(7).toString());
                ps.setString(5, "上午 09:30");
                ps.setString(6, "定期回診");
                ps.setString(7, "已預約");
                ps.setString(8, "Demo 下一次看診資料");
                ps.executeUpdate();
            }
        }
    }

    private static int tableCount(Connection c, String table) throws SQLException {
        try (Statement st = c.createStatement(); ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM " + table)) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private static void createCrudTable(Statement st, String table, String idCol) throws SQLException {
        st.executeUpdate("CREATE TABLE IF NOT EXISTS " + table + "(" +
                idCol + " INT AUTO_INCREMENT PRIMARY KEY," +
                "elder_id INT DEFAULT 1," +
                "col1 VARCHAR(255), col2 VARCHAR(255), col3 VARCHAR(255), col4 VARCHAR(255)," +
                "note TEXT, record_time VARCHAR(30)," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)");
    }

    private static String friendly(SQLException e) {
        String msg = e.getMessage();
        if (msg == null) msg = "";
        if (msg.contains("Access denied")) return "MySQL 帳號或密碼錯誤，請檢查 src/main/resources/db.properties 的 db.password。";
        if (msg.contains("Unknown database")) return "找不到 caremate_db，請先啟動 MySQL，或手動執行 src/main/sql/schema.sql。";
        if (msg.contains("Communications link failure") || msg.contains("Connection refused")) return "MySQL 尚未啟動，請先開啟 MySQL Server。";
        if (msg.contains("Malformed database URL")) return "JDBC URL 格式錯誤，請使用 jdbc:mysql://localhost:3306/caremate_db?useSSL=false&serverTimezone=Asia/Taipei&allowPublicKeyRetrieval=true&characterEncoding=UTF-8";
        return msg;
    }

    private static String rootMessage(Throwable e) {
        Throwable t = e;
        while (t.getCause() != null) t = t.getCause();
        if (t instanceof SQLException) return friendly((SQLException) t);
        return t.getMessage() == null ? e.getMessage() : t.getMessage();
    }
}
