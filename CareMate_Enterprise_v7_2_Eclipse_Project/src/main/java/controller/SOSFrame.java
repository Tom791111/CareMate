package controller;

import javax.swing.*;
import java.awt.*;
import util.UIUtil;

public class SOSFrame extends BaseFrame {
    private final String[][] sosList = {
        {"119", "救護車", "call", "red"},
        {"110", "警察局", "call", "red"},
        {"112", "緊急救援", "sos", "outline"},
        {"1957", "福利資訊", "bell", "yellow"},
        {"1966", "長照專線", "contact", "yellow"}
    };

    public SOSFrame(String role) {
        super("緊急求救");
        this.role = role;
        title("緊急求救");

        JLabel topIcon = UIUtil.icon("sos", 28, UIUtil.RED);
        topIcon.setBounds(88, 0, 42, 42);
        content.add(topIcon);
        JLabel pageTitle = UIUtil.h2Label("緊急求救");
        pageTitle.setBounds(138, 4, 150, 32);
        content.add(pageTitle);

        JLabel hint = UIUtil.hint("點選下方按鈕，可建立求救紀錄與顯示提醒");
        hint.setBounds(20, 46, 320, 22);
        content.add(hint);

        int y = 85;
        for (String[] s : sosList) {
            addSOSButton(s[0], s[1], s[2], s[3], y);
            y += 72;
        }

        JPanel tip = UIUtil.card();
        tip.setBounds(0, 445, 360, 62);
        content.add(tip);
        JLabel t1 = UIUtil.h3Label("緊急提醒");
        t1.setForeground(UIUtil.RED);
        t1.setBounds(22, 10, 100, 24);
        tip.add(t1);
        JLabel t2 = UIUtil.hint("按下求救後，系統會記錄時間與事件狀態。");
        t2.setBounds(22, 35, 300, 20);
        tip.add(t2);

        bottomNav();
    }

    private void addSOSButton(String number, String title, String icon, String style, int y) {
        JButton btn;
        if ("red".equals(style)) {
            btn = UIUtil.dangerBtn(number + "   " + title);
        } else if ("yellow".equals(style)) {
            btn = new UIUtil.RoundButton(number + "   " + title, UIUtil.YELLOW, Color.WHITE);
        } else {
            btn = UIUtil.outlineBtn(number + "   " + title);
            btn.setForeground(UIUtil.RED);
        }
        btn.setIcon(new UIUtil.LineIcon(icon, 24, "yellow".equals(style) ? Color.WHITE : ("red".equals(style) ? Color.WHITE : UIUtil.RED)));
        btn.setHorizontalTextPosition(SwingConstants.LEFT);
        btn.setFont(UIUtil.font(Font.BOLD, 22));
        btn.setBounds(20, y, 320, 52);
        content.add(btn);
        btn.addActionListener(e -> UIUtil.info(this, "已建立緊急求救紀錄：" + number + " " + title));
    }
}
