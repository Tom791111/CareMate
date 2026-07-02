package controller;

import javax.swing.*;
import util.UIUtil;
import model.DashboardStats;
import service.DashboardService;
import service.impl.DashboardServiceImpl;
import java.awt.*;
import java.util.List;

/**
 * CareMate Final v5 Dashboard 2.1
 * 修正首頁壓縮、即時照護數據重疊、小圖示改成統一 Figma 線條 Icon。
 */
public class HomeFrame extends BaseFrame {
    private final DashboardService dashboardService = new DashboardServiceImpl();
    private JLabel percent;
    private JLabel health;
    private JLabel status;
    private JLabel bpValue;
    private JLabel tempValue;
    private JLabel medValue;
    private JLabel mealValue;
    private JLabel moodValue;
    private JProgressBar bar;
    private MiniTrendPanel trendPanel;

    public HomeFrame(String role) {
        super(homeTitle(role));
        this.role = role;
        title(homeTitle(role));

        JLabel hello = UIUtil.title("早安，今天也辛苦了");
        hello.setFont(UIUtil.font(Font.BOLD,23));
        hello.setBounds(6, 0, 258, 34);
        content.add(hello);

        JLabel hint = UIUtil.hint(roleHint(role));
        hint.setBounds(8, 34, 230, 22);
        content.add(hint);

        JButton refresh = UIUtil.outlineBtn("重新整理");
        refresh.setFont(UIUtil.font(Font.BOLD, 12));
        refresh.setBounds(262, 24, 94, 34);
        content.add(refresh);
        refresh.addActionListener(e -> loadDashboard());

        createDashboardCard();
        createLiveDataCard();
        createQuickFunctionArea(role);
        bottomNav();
        loadDashboard();
    }

    private void createDashboardCard() {
        JPanel dashboard = UIUtil.card();
        dashboard.setBounds(0, 66, 360, 100);
        content.add(dashboard);

        percent = new JLabel("--%", SwingConstants.CENTER);
        percent.setFont(UIUtil.font(Font.BOLD,34));
        percent.setForeground(UIUtil.GREEN);
        percent.setBounds(16,10,102,52);
        dashboard.add(percent);

        JLabel ptext = UIUtil.hint("今日完成率");
        ptext.setHorizontalAlignment(SwingConstants.CENTER);
        ptext.setBounds(16,64,102,22);
        dashboard.add(ptext);

        health = UIUtil.h3Label("健康分數 --");
        health.setBounds(138,14,178,26);
        dashboard.add(health);

        bar = new JProgressBar(0,100);
        bar.setValue(0);
        bar.setBounds(138,48,182,18);
        bar.setForeground(UIUtil.PRIMARY);
        bar.setBackground(new Color(245,235,225));
        bar.setBorderPainted(false);
        dashboard.add(bar);

        status = UIUtil.hint("等待 MySQL 統計資料...");
        status.setBounds(138,70,200,22);
        dashboard.add(status);
    }

    private void createLiveDataCard() {
        JPanel live = UIUtil.card();
        live.setBounds(0, 178, 360, 190);
        content.add(live);

        JLabel title = UIUtil.h3Label("即時照護數據");
        title.setBounds(18, 10, 180, 24);
        live.add(title);

        bpValue = addDataLine(live, "health", "血壓", 18, 46);
        tempValue = addDataLine(live, "report", "體溫", 188, 46);
        medValue = addDataLine(live, "medicine", "用藥", 18, 96);
        mealValue = addDataLine(live, "meal", "飲食", 188, 96);
        moodValue = addDataLine(live, "mood", "情緒", 18, 146);

        trendPanel = new MiniTrendPanel();
        trendPanel.setBounds(208, 146, 124, 26);
        live.add(trendPanel);
    }

    private JLabel addDataLine(JPanel parent, String iconType, String name, int x, int y) {
        JLabel iconLabel = UIUtil.icon(iconType, 22, UIUtil.PRIMARY);
        iconLabel.setBounds(x, y, 28, 28);
        parent.add(iconLabel);

        JLabel nameLabel = UIUtil.hint(name);
        nameLabel.setBounds(x + 34, y - 2, 58, 20);
        parent.add(nameLabel);

        JLabel value = UIUtil.h3Label("--");
        value.setFont(UIUtil.font(Font.BOLD, 13));
        value.setBounds(x + 34, y + 17, 118, 20);
        parent.add(value);
        return value;
    }

    private void createQuickFunctionArea(String role) {
        JLabel sec = UIUtil.h2Label("快捷功能");
        sec.setBounds(8, 384, 180, 30);
        content.add(sec);

        String[][] items = roleItems(role);
        int x = 0, y = 424;
        for (String[] it : items) {
            JPanel card = UIUtil.card();
            card.setBounds(x, y, 172, 64);
            content.add(card);

            JLabel icon = UIUtil.icon(it[0], 30, UIUtil.PRIMARY);
            icon.setBounds(14,10,34,34);
            card.add(icon);

            JLabel name = UIUtil.h3Label(it[1]);
            name.setBounds(60,7,90,24);
            card.add(name);

            JLabel desc = UIUtil.hint(it[2]);
            desc.setBounds(60,30,100,20);
            card.add(desc);

            JButton mask = new JButton();
            mask.setOpaque(false);
            mask.setContentAreaFilled(false);
            mask.setBorderPainted(false);
            mask.setBounds(0,0,172,64);
            card.add(mask);
            final String cls = it[3];
            mask.addActionListener(e -> open(cls));

            x += 188;
            if (x > 200) { x = 0; y += 74; }
        }
    }

    private void loadDashboard() {
        try {
            DashboardStats s = dashboardService.getTodayStats(1);
            percent.setText(s.getCompletionRate() + "%");
            health.setText("健康分數 " + s.getHealthScore());
            bar.setValue(s.getCompletionRate());
            status.setText(s.getSummaryText());
            bpValue.setText(s.getLatestBloodPressure());
            tempValue.setText(s.getLatestTemperature());
            medValue.setText(s.getLatestMedicineStatus());
            mealValue.setText(s.getLatestMealStatus());
            moodValue.setText(s.getLatestMoodStatus());
            trendPanel.setScores(s.getSevenDayScores());
            trendPanel.repaint();
        } catch (Exception ex) {
            percent.setText("0%");
            health.setText("健康分數 0");
            bar.setValue(0);
            status.setText("資料庫尚未連線");
            bpValue.setText("請檢查 MySQL");
            tempValue.setText("--");
            medValue.setText("--");
            mealValue.setText("--");
            moodValue.setText("--");
            UIUtil.error(this, ex);
        }
    }

    static String homeTitle(String role) {
        if ("ELDER".equals(role)) return "被照顧者首頁";
        if ("FOREIGN_CAREGIVER".equals(role)) return "外籍照顧者首頁";
        if ("ADMIN".equals(role)) return "管理者首頁";
        return "家屬照護總覽";
    }

    static String roleHint(String role) {
        if ("ELDER".equals(role)) return "大字體提醒、SOS 與簡易回報";
        if ("FOREIGN_CAREGIVER".equals(role)) return "Today tasks, care notes and translate";
        return "查看長輩今日照護、健康與通知";
    }

    static String[][] roleItems(String role) {
        if ("ELDER".equals(role)) return new String[][]{{"sos","SOS","緊急求救","SOSFrame"},{"health","健康","回報量測","HealthFrame"},{"mood","心情","快速回報","MoodFrame"},{"bell","提醒","通知中心","NotificationFrame"}};
        if ("FOREIGN_CAREGIVER".equals(role)) return new String[][]{{"task","任務","Today task","CareTaskFrame"},{"medicine","用藥","Medication","MedicineFrame"},{"meal","飲食","Meal note","MealFrame"},{"translate","翻譯","Translate","TranslateFrame"},{"call","通話","Video call","VideoCallFrame"},{"log","日誌","Care log","CareLogFrame"}};
        return new String[][]{{"health","健康","血壓體溫","HealthFrame"},{"medicine","用藥","服藥狀態","MedicineFrame"},{"task","安排","照顧排程","ScheduleFrame"},{"contact","聯絡人","電話視訊","ContactFrame"},{"sos","SOS","緊急求救","SOSFrame"},{"report","報表","查詢列印","ReportFrame"}};
    }

    static class MiniTrendPanel extends JPanel {
        private List<Integer> scores;
        MiniTrendPanel() { setOpaque(false); }
        void setScores(List<Integer> scores) { this.scores = scores; }
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(235, 226, 215));
            g2.drawLine(0, getHeight() - 4, getWidth(), getHeight() - 4);
            if (scores != null && scores.size() > 1) {
                g2.setColor(UIUtil.PRIMARY);
                g2.setStroke(new BasicStroke(2f));
                int prevX = 0;
                int prevY = y(scores.get(0));
                for (int i = 1; i < scores.size(); i++) {
                    int x = i * getWidth() / (scores.size() - 1);
                    int yy = y(scores.get(i));
                    g2.drawLine(prevX, prevY, x, yy);
                    g2.fillOval(x - 2, yy - 2, 4, 4);
                    prevX = x;
                    prevY = yy;
                }
            }
            g2.dispose();
        }
        private int y(int score) {
            int safe = Math.max(0, Math.min(100, score));
            return getHeight() - 4 - Math.round(safe * (getHeight() - 8) / 100f);
        }
    }
}
