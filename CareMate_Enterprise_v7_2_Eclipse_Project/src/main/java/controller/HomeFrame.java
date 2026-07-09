package controller;

import javax.swing.*;
import util.UIUtil;
import util.I18n;
import model.DashboardStats;
import service.DashboardService;
import service.impl.DashboardServiceImpl;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

        JLabel hello = UIUtil.title("FOREIGN_CAREGIVER".equals(role) ? I18n.t("home.hello") : "早安，今天也辛苦了");
        hello.setFont(UIUtil.font(Font.BOLD,23));
        hello.setBounds(6, 0, 258, 34);
        content.add(hello);

        JLabel hint = UIUtil.hint(roleHint(role));
        hint.setBounds(8, 34, 230, 22);
        content.add(hint);

        JButton refresh = UIUtil.outlineBtn("FOREIGN_CAREGIVER".equals(role) ? I18n.t("home.refresh") : "重新整理");
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
        bindClick(dashboard, "ReportFrame", "查看健康分數與完成率報表");

        percent = new JLabel("--%", SwingConstants.CENTER);
        percent.setFont(UIUtil.font(Font.BOLD,34));
        percent.setForeground(UIUtil.GREEN);
        percent.setBounds(16,10,102,52);
        dashboard.add(percent);

        JLabel ptext = UIUtil.hint("FOREIGN_CAREGIVER".equals(role) ? I18n.t("home.completion") : "今日完成率");
        ptext.setHorizontalAlignment(SwingConstants.CENTER);
        ptext.setBounds(16,64,102,22);
        dashboard.add(ptext);

        health = UIUtil.h3Label(("FOREIGN_CAREGIVER".equals(role) ? I18n.t("home.health.score") : "健康分數") + " --");
        health.setBounds(138,14,178,26);
        dashboard.add(health);

        bar = new JProgressBar(0,100);
        bar.setValue(0);
        bar.setBounds(138,48,182,18);
        bar.setForeground(UIUtil.PRIMARY);
        bar.setBackground(new Color(245,235,225));
        bar.setBorderPainted(false);
        dashboard.add(bar);

        status = UIUtil.hint("FOREIGN_CAREGIVER".equals(role) ? I18n.t("home.waiting") : "等待 MySQL 統計資料...");
        status.setBounds(138,70,200,22);
        dashboard.add(status);
    }

    private void createLiveDataCard() {
        JPanel live = UIUtil.card();
        live.setBounds(0, 178, 360, 190);
        content.add(live);

        JLabel title = UIUtil.h3Label("FOREIGN_CAREGIVER".equals(role) ? I18n.t("home.live.data") : "即時照護數據");
        title.setBounds(18, 10, 180, 24);
        live.add(title);

        bpValue = addDataLine(live, "health", "FOREIGN_CAREGIVER".equals(role) ? I18n.t("blood.pressure") : "血壓", 18, 46);
        addClickZone(live, 12, 42, 150, 44, "HealthFrame", "查看血壓／體溫紀錄");
        tempValue = addDataLine(live, "report", "FOREIGN_CAREGIVER".equals(role) ? I18n.t("temperature") : "體溫", 188, 46);
        addClickZone(live, 182, 42, 150, 44, "HealthFrame", "查看血壓／體溫紀錄");
        medValue = addDataLine(live, "medicine", "FOREIGN_CAREGIVER".equals(role) ? I18n.t("medicine") : "用藥", 18, 96);
        addClickZone(live, 12, 92, 150, 44, "MedicineFrame", "查看用藥管理");
        mealValue = addDataLine(live, "meal", "FOREIGN_CAREGIVER".equals(role) ? I18n.t("diet") : "飲食", 188, 96);
        addClickZone(live, 182, 92, 150, 44, "MealFrame", "查看飲食紀錄");
        moodValue = addDataLine(live, "mood", "FOREIGN_CAREGIVER".equals(role) ? I18n.t("mood") : "情緒", 18, 146);
        addClickZone(live, 12, 142, 150, 44, "MoodFrame", "查看情緒紀錄");

        trendPanel = new MiniTrendPanel();
        trendPanel.setBounds(208, 146, 124, 26);
        trendPanel.setToolTipText("查看七日趨勢報表");
        live.add(trendPanel);
        addClickZone(live, 198, 140, 142, 46, "ReportFrame", "查看七日趨勢報表");
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

    private void bindClick(JComponent comp, String frameClass, String tip) {
        comp.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        comp.setToolTipText(tip);
        comp.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { open(frameClass); }
        });
    }

    private void addClickZone(JPanel parent, int x, int y, int w, int h, String frameClass, String tip) {
        JButton zone = new JButton();
        zone.setOpaque(false);
        zone.setContentAreaFilled(false);
        zone.setBorderPainted(false);
        zone.setFocusPainted(false);
        zone.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        zone.setToolTipText(tip);
        zone.setBounds(x, y, w, h);
        zone.addActionListener(e -> open(frameClass));
        parent.add(zone);
        parent.setComponentZOrder(zone, 0);
    }

    private void createQuickFunctionArea(String role) {
        JLabel sec = UIUtil.h2Label("FOREIGN_CAREGIVER".equals(role) ? I18n.t("quick.functions") : "快捷功能");
        sec.setBounds(8, 374, 180, 30);
        content.add(sec);

        String[][] items = roleItems(role);
        int x = 0, y = 414;
        for (String[] it : items) {
            JPanel card = UIUtil.card();
            card.setBounds(x, y, 172, 54);
            content.add(card);

            JLabel icon = UIUtil.icon(it[0], 26, UIUtil.PRIMARY);
            icon.setBounds(14,9,30,30);
            card.add(icon);

            JLabel name = UIUtil.h3Label(it[1]);
            name.setBounds(56,5,96,22);
            card.add(name);

            JLabel desc = UIUtil.hint(it[2]);
            desc.setBounds(56,27,108,18);
            card.add(desc);

            JButton mask = new JButton();
            mask.setOpaque(false);
            mask.setContentAreaFilled(false);
            mask.setBorderPainted(false);
            mask.setBounds(0,0,172,54);
            card.add(mask);
            final String cls = it[3];
            mask.addActionListener(e -> open(cls));

            x += 188;
            if (x > 200) { x = 0; y += 60; }
        }
    }

    private void loadDashboard() {
        try {
            DashboardStats s = dashboardService.getTodayStats(1);
            percent.setText(s.getCompletionRate() + "%");
            health.setText(("FOREIGN_CAREGIVER".equals(role) ? I18n.t("home.health.score") : "健康分數") + " " + s.getHealthScore());
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
            health.setText(("FOREIGN_CAREGIVER".equals(role) ? I18n.t("home.health.score") : "健康分數") + " 0");
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
        if ("FOREIGN_CAREGIVER".equals(role)) return I18n.t("home.title.caregiver");
        if ("ADMIN".equals(role)) return "管理者首頁";
        return "家屬照護總覽";
    }

    static String roleHint(String role) {
        if ("ELDER".equals(role)) return "大字體提醒、SOS 與簡易回報";
        if ("FOREIGN_CAREGIVER".equals(role)) return I18n.t("home.hint.caregiver");
        return "查看長輩今日照護、健康與通知";
    }

    static String[][] roleItems(String role) {
        if ("ELDER".equals(role)) return new String[][]{{"sos","SOS","緊急求救","SOSFrame"},{"health","健康","回報量測","HealthFrame"},{"mood","心情","快速回報","MoodFrame"},{"medicine","用藥","服藥提醒","MedicineFrame"},{"health","醫療","預約看診","MedicalCenterFrame"},{"bell","提醒","通知中心","NotificationFrame"}};
        if ("FOREIGN_CAREGIVER".equals(role)) return new String[][]{{"task",I18n.t("task"),I18n.t("today.task"),"CareTaskFrame"},{"medicine",I18n.t("medicine"),I18n.t("medication"),"MedicineFrame"},{"meal",I18n.t("diet"),I18n.t("meal.note"),"MealFrame"},{"translate",I18n.t("translate"),I18n.t("translate"),"TranslateFrame"},{"call",I18n.t("call"),I18n.t("video.call"),"VideoCallFrame"},{"log",I18n.t("care.log"),I18n.t("care.log.desc"),"CareLogFrame"}};
        return new String[][]{{"health","健康","血壓體溫","HealthFrame"},{"medicine","用藥","服藥狀態","MedicineFrame"},{"task","安排","照顧排程","ScheduleFrame"},{"contact","聯絡人","電話視訊","ContactFrame"},{"translate","翻譯","即時翻譯","TranslateFrame"},{"call","視訊","視訊通話","VideoCallFrame"},{"health","醫療","預約看診","MedicalCenterFrame"},{"sos","SOS","緊急求救","SOSFrame"},{"report","報表","查詢列印","ReportFrame"}};
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
