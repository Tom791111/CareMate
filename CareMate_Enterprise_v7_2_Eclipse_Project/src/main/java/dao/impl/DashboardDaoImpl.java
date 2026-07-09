package dao.impl;

import dao.DashboardDao;
import exception.AppException;
import model.DashboardStats;
import util.DBUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Dashboard 2.0：首頁數據全部由 MySQL 計算。
 * 只要在健康、用藥、飲食、情緒、任務頁新增/修改資料，首頁重新開啟或按重新整理就會更新。
 */
public class DashboardDaoImpl implements DashboardDao {

    @Override
    public DashboardStats getTodayStats(int elderId) {
        try (Connection c = DBUtil.getConnection()) {
            String today = LocalDate.now().toString();

            int healthToday = countToday(c, "health_record", elderId, today);
            int medicineDone = countCompletedToday(c, "medicine_record", elderId, today, "col4");
            int mealToday = countToday(c, "meal_record", elderId, today);
            int moodToday = countToday(c, "mood_record", elderId, today);
            int taskDone = countCompletedToday(c, "care_task", elderId, today, "col3");
            int taskTotal = countToday(c, "care_task", elderId, today);

            int totalItems = 4 + Math.max(taskTotal, 1); // 健康、用藥、飲食、情緒 + 今日任務
            int completedItems = 0;
            if (healthToday > 0) completedItems++;
            if (medicineDone > 0) completedItems++;
            if (mealToday > 0) completedItems++;
            if (moodToday > 0) completedItems++;
            completedItems += taskTotal > 0 ? taskDone : 0;

            int completionRate = percent(completedItems, totalItems);
            int healthScore = calculateHealthScore(c, elderId, today, medicineDone, mealToday, moodToday);

            DashboardStats s = new DashboardStats();
            s.setCompletionRate(completionRate);
            s.setHealthScore(healthScore);
            s.setCompletedItems(completedItems);
            s.setTotalItems(totalItems);
            s.setSummaryText("健康、用藥、飲食、情緒、任務完成 " + completedItems + "/" + totalItems);
            fillLatest(c, elderId, s);
            s.setSevenDayScores(calcSevenDayScores(c, elderId));
            return s;
        } catch (Exception e) {
            throw new AppException("Dashboard 統計資料讀取失敗：" + e.getMessage(), e);
        }
    }

    private int calculateHealthScore(Connection c, int elderId, String today, int medicineDone, int mealToday, int moodToday) throws SQLException {
        int score = 0;
        LatestHealth h = latestHealth(c, elderId, today);
        if (h.exists) {
            if (h.bpNormal) score += 30; else score += 15;
            if (h.tempNormal) score += 20; else score += 10;
        }
        score += medicineDone > 0 ? 20 : 0;
        score += Math.min(15, mealToday * 5); // 早餐/午餐/晚餐最多 15 分
        score += moodToday > 0 ? latestMoodScore(c, elderId, today) : 0;
        return Math.min(100, Math.max(0, score));
    }

    private int countToday(Connection c, String table, int elderId, String today) throws SQLException {
        String sql = "SELECT COUNT(*) FROM " + table + " WHERE elder_id=? AND record_time LIKE ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, elderId);
            ps.setString(2, today + "%");
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? rs.getInt(1) : 0; }
        }
    }

    private int countCompletedToday(Connection c, String table, int elderId, String today, String statusCol) throws SQLException {
        String sql = "SELECT COUNT(*) FROM " + table + " WHERE elder_id=? AND record_time LIKE ? " +
                "AND (" + statusCol + " LIKE '%完成%' OR UPPER(" + statusCol + ") LIKE '%COMPLETED%' OR UPPER(" + statusCol + ") LIKE '%DONE%')";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, elderId);
            ps.setString(2, today + "%");
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? rs.getInt(1) : 0; }
        }
    }

    private LatestHealth latestHealth(Connection c, int elderId, String today) throws SQLException {
        LatestHealth h = new LatestHealth();
        String sql = "SELECT col1,col2,col3 FROM health_record WHERE elder_id=? AND record_time LIKE ? ORDER BY health_id DESC LIMIT 1";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, elderId);
            ps.setString(2, today + "%");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    h.exists = true;
                    int sys = toInt(rs.getString("col1"));
                    int dia = toInt(rs.getString("col2"));
                    double temp = toDouble(rs.getString("col3"));
                    h.bpNormal = sys >= 90 && sys <= 140 && dia >= 60 && dia <= 90;
                    h.tempNormal = temp >= 35.5 && temp <= 37.5;
                }
            }
        }
        return h;
    }

    private int latestMoodScore(Connection c, int elderId, String today) throws SQLException {
        String sql = "SELECT col2,col3 FROM mood_record WHERE elder_id=? AND record_time LIKE ? ORDER BY mood_id DESC LIMIT 1";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, elderId);
            ps.setString(2, today + "%");
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return 0;
                int moodScore = toInt(rs.getString("col2"));
                if (moodScore <= 0) moodScore = rs.getString("col3") != null && rs.getString("col3").contains("良好") ? 5 : 3;
                return Math.min(15, Math.max(5, moodScore * 3));
            }
        }
    }

    private void fillLatest(Connection c, int elderId, DashboardStats s) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement("SELECT col1,col2,col3 FROM health_record WHERE elder_id=? ORDER BY health_id DESC LIMIT 1")) {
            ps.setInt(1, elderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    s.setLatestBloodPressure(nvl(rs.getString("col1"), "--") + "/" + nvl(rs.getString("col2"), "--"));
                    s.setLatestTemperature(nvl(rs.getString("col3"), "--") + "°C");
                } else {
                    s.setLatestBloodPressure("尚無資料"); s.setLatestTemperature("尚無資料");
                }
            }
        }
        s.setLatestMedicineStatus(latest(c, "medicine_record", elderId, "col1", "col4", "尚無用藥紀錄"));
        s.setLatestMealStatus(latest(c, "meal_record", elderId, "col1", "col4", "尚無飲食紀錄"));
        s.setLatestMoodStatus(latest(c, "mood_record", elderId, "col1", "col3", "尚無情緒紀錄"));
    }

    private String latest(Connection c, String table, int elderId, String nameCol, String statusCol, String emptyText) throws SQLException {
        String idCol = table.equals("medicine_record") ? "medicine_id" : table.equals("meal_record") ? "meal_id" : table.equals("mood_record") ? "mood_id" : "task_id";
        try (PreparedStatement ps = c.prepareStatement("SELECT " + nameCol + "," + statusCol + " FROM " + table + " WHERE elder_id=? ORDER BY " + idCol + " DESC LIMIT 1")) {
            ps.setInt(1, elderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return nvl(rs.getString(nameCol), "--") + "｜" + nvl(rs.getString(statusCol), "--");
                return emptyText;
            }
        }
    }

    private List<Integer> calcSevenDayScores(Connection c, int elderId) throws SQLException {
        List<Integer> scores = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            String day = LocalDate.now().minusDays(i).toString();
            int health = countToday(c, "health_record", elderId, day) > 0 ? 35 : 0;
            int med = countCompletedToday(c, "medicine_record", elderId, day, "col4") > 0 ? 25 : 0;
            int meal = Math.min(20, countToday(c, "meal_record", elderId, day) * 7);
            int mood = countToday(c, "mood_record", elderId, day) > 0 ? 20 : 0;
            scores.add(Math.min(100, health + med + meal + mood));
        }
        return scores;
    }

    private int percent(int a, int b) { return b <= 0 ? 0 : Math.round(a * 100f / b); }
    private int toInt(String v) { try { return Integer.parseInt(v == null ? "0" : v.replaceAll("[^0-9-]", "")); } catch (Exception e) { return 0; } }
    private double toDouble(String v) { try { return Double.parseDouble(v == null ? "0" : v.replaceAll("[^0-9.]", "")); } catch (Exception e) { return 0; } }
    private String nvl(String v, String d) { return v == null || v.trim().isEmpty() ? d : v.trim(); }

    private static class LatestHealth { boolean exists; boolean bpNormal; boolean tempNormal; }
}
