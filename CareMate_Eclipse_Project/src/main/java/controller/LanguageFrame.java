package controller;

import javax.swing.*;
import java.awt.*;
import util.UIUtil;

public class LanguageFrame extends BaseFrame {

    private String selectedLanguage = "繁體中文";
    private JButton selectedButton;

    public LanguageFrame() {
        super("語言選擇");
        title("外籍照顧者 - 請選擇語言");

        // 主標題區：完全放在手機 content 寬度 360 內，避免跑到右側
        JLabel heroIcon = UIUtil.icon("translate", 58, UIUtil.PRIMARY);
        heroIcon.setBounds(151, 16, 58, 58);
        content.add(heroIcon);

        JLabel mainTitle = UIUtil.h2Label("選擇使用語言");
        mainTitle.setHorizontalAlignment(SwingConstants.CENTER);
        mainTitle.setBounds(40, 86, 280, 30);
        content.add(mainTitle);

        JLabel subtitle = UIUtil.hint("請選擇熟悉的語言，接著進入登入流程");
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);
        subtitle.setBounds(20, 120, 320, 24);
        content.add(subtitle);

        // 語言卡片區：2 欄卡片式，符合 Figma 手機排版
        addLanguageButton("繁體中文", "TW", 24, 166);
        addLanguageButton("English", "EN", 188, 166);
        addLanguageButton("Tiếng Việt", "VN", 24, 256);
        addLanguageButton("Bahasa", "ID", 188, 256);

        JButton next = UIUtil.btn("下一步登入");
        next.setBounds(42, 382, 276, 52);
        content.add(next);
        next.addActionListener(e -> {
            dispose();
            new LoginFrame("FOREIGN_CAREGIVER").setVisible(true);
        });

        JButton backRole = UIUtil.outlineBtn("返回身分選擇");
        backRole.setBounds(88, 448, 184, 42);
        content.add(backRole);
        backRole.addActionListener(e -> {
            dispose();
            new RoleFrame().setVisible(true);
        });
    }

    private void addLanguageButton(String name, String code, int x, int y) {
        JButton btn = UIUtil.ghostBtn("<html><center><span style='font-size:18px;font-weight:bold;'>" + code + "</span><br>" + name + "</center></html>");
        btn.setBounds(x, y, 148, 72);
        btn.setHorizontalAlignment(SwingConstants.CENTER);
        btn.setVerticalAlignment(SwingConstants.CENTER);
        btn.addActionListener(e -> {
            selectedLanguage = name;
            if (selectedButton != null) {
                selectedButton.setBackground(UIUtil.NAV_BG);
                selectedButton.setForeground(UIUtil.DARK);
            }
            selectedButton = btn;
            btn.setBackground(UIUtil.PRIMARY);
            btn.setForeground(Color.WHITE);
        });
        content.add(btn);

        if (selectedButton == null) {
            selectedButton = btn;
            btn.setBackground(UIUtil.PRIMARY);
            btn.setForeground(Color.WHITE);
        }
    }
}
