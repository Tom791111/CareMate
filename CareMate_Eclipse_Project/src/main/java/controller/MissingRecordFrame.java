package controller;

import util.DBUtil;
import util.UIUtil;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 右上角鈴鐺：尚未紀錄提醒頁
 * 目的：提醒家屬/照顧者今天還沒完成的健康、用藥、飲食、情緒、任務紀錄。
 */
public class MissingRecordFrame extends BaseFrame {
    private JPanel listPanel;
    private JLabel summaryLabel;

    public MissingRecordFrame(String role) {
        super("尚未紀錄提醒");
        this.role = role;
        title("尚未紀錄提醒");

        JLabel pageTitle = UIUtil.h2Label("尚未紀錄提醒");
        pageTitle.setBounds(8, 0, 220, 32);
        content.add(pageTitle);

        JLabel hint = UIUtil.hint("點選提醒卡片可前往對應頁面補登資料");
        hint.setBounds(8, 34, 300, 22);
        content.add(hint);

        JButton refresh = UIUtil.outlineBtn("重新整理");
        refresh.setFont(UIUtil.font(Font.BOLD, 12));
        refresh.setBounds(250, 2, 106, 34);
        content.add(refresh);
        refresh.addActionListener(e -> loadMissingRecords());

        JPanel summaryCard = UIUtil.card();
        summaryCard.setBounds(0, 70, 360, 88);
        content.add(summaryCard);

        JLabel bellIcon = UIUtil.icon("bell", 34, UIUtil.PRIMARY);
        bellIcon.setBounds(18, 20, 42, 42);
        summaryCard.add(bellIcon);

        summaryLabel = UIUtil.h3Label("正在檢查今日紀錄...");
        summaryLabel.setBounds(76, 18, 250, 26);
        summaryCard.add(summaryLabel);

        JLabel summaryHint = UIUtil.hint("完成後首頁 Dashboard 會同步更新");
        summaryHint.setBounds(76, 46, 250, 24);
        summaryCard.add(summaryHint);

        listPanel = new JPanel(null);
        listPanel.setOpaque(false);
        listPanel.setBounds(0, 176, 360, 500);
        content.add(listPanel);

        bottomNav();
        loadMissingRecords();
    }

    private void loadMissingRecords() {
        listPanel.removeAll();
        try {
            List<ReminderItem> items = findMissingItems(1);
            if (items.isEmpty()) {
                summaryLabel.setText("今天紀錄都完成了");
                addDoneCard();
            } else {
                summaryLabel.setText("尚有 " + items.size() + " 項未紀錄");
                int y = 0;
                for (ReminderItem item : items) {
                    addReminderCard(item, y);
                    y += 84;
                }
            }
        } catch (Exception ex) {
            summaryLabel.setText("資料庫讀取失敗");
            UIUtil.error(this, ex);
        }
        listPanel.revalidate();
        listPanel.repaint();
    }

    private void addDoneCard() {
        JPanel card = UIUtil.card();
        card.setBounds(0, 0, 360, 130);
        listPanel.add(card);

        JLabel icon = UIUtil.icon("mood", 42, UIUtil.GREEN);
        icon.setBounds(154, 18, 52, 52);
        card.add(icon);

        JLabel title = UIUtil.h2Label("今日已完成");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBounds(0, 70, 360, 30);
        card.add(title);

        JLabel text = UIUtil.hint("健康、用藥、飲食、情緒與任務都有紀錄");
        text.setHorizontalAlignment(SwingConstants.CENTER);
        text.setBounds(0, 100, 360, 22);
        card.add(text);
    }

    private void addReminderCard(ReminderItem item, int y) {
        JPanel card = UIUtil.card();
        card.setBounds(0, y, 360, 72);
        listPanel.add(card);

        JLabel icon = UIUtil.icon(item.icon, 30, UIUtil.PRIMARY);
        icon.setBounds(18, 19, 38, 38);
        card.add(icon);

        JLabel title = UIUtil.h3Label(item.title);
        title.setBounds(70, 12, 170, 26);
        card.add(title);

        JLabel desc = UIUtil.hint(item.desc);
        desc.setBounds(70, 38, 190, 22);
        card.add(desc);

        JButton go = UIUtil.outlineBtn("前往");
        go.setFont(UIUtil.font(Font.BOLD, 12));
        go.setBounds(272, 18, 72, 36);
        card.add(go);
        go.addActionListener(e -> open(item.frameClass));
    }

    private List<ReminderItem> findMissingItems(int elderId) throws SQLException {
        String today = LocalDate.now().toString();
        List<ReminderItem> items = new ArrayList<>();
        try (Connection c = DBUtil.getConnection()) {
            if (countToday(c, "health_record", elderId, today) == 0) {
                items.add(new ReminderItem("health", "健康紀錄未完成", "請補登血壓與體溫", "HealthFrame"));
            }
            if (countCompletedToday(c, "medicine_record", elderId, today, "col4") == 0) {
                items.add(new ReminderItem("medicine", "用藥尚未完成", "請確認今日服藥狀態", "MedicineFrame"));
            }
            if (countToday(c, "meal_record", elderId, today) == 0) {
                items.add(new ReminderItem("meal", "飲食尚未紀錄", "請補登早餐/午餐/晚餐", "MealFrame"));
            }
            if (countToday(c, "mood_record", elderId, today) == 0) {
                items.add(new ReminderItem("mood", "情緒尚未紀錄", "請回報今日心情狀態", "MoodFrame"));
            }
            int taskTotal = countToday(c, "care_task", elderId, today);
            int taskDone = countCompletedToday(c, "care_task", elderId, today, "col3");
            if (taskTotal == 0) {
                items.add(new ReminderItem("task", "今日任務未建立", "請新增今日照護任務", "CareTaskFrame"));
            } else if (taskDone < taskTotal) {
                items.add(new ReminderItem("task", "照護任務未完成", "今日任務完成 " + taskDone + "/" + taskTotal, "CareTaskFrame"));
            }
        }
        return items;
    }

    private int countToday(Connection c, String table, int elderId, String today) throws SQLException {
        String sql = "SELECT COUNT(*) FROM " + table + " WHERE elder_id=? AND record_time LIKE ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, elderId);
            ps.setString(2, today + "%");
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    private int countCompletedToday(Connection c, String table, int elderId, String today, String statusCol) throws SQLException {
        String sql = "SELECT COUNT(*) FROM " + table + " WHERE elder_id=? AND record_time LIKE ? " +
                "AND (" + statusCol + " LIKE '%完成%' OR UPPER(" + statusCol + ") LIKE '%COMPLETED%' OR UPPER(" + statusCol + ") LIKE '%DONE%')";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, elderId);
            ps.setString(2, today + "%");
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    private static class ReminderItem {
        String icon, title, desc, frameClass;
        ReminderItem(String icon, String title, String desc, String frameClass) {
            this.icon = icon;
            this.title = title;
            this.desc = desc;
            this.frameClass = frameClass;
        }
    }
}
