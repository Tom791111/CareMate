package controller;

import javax.swing.*;
import java.awt.*;
import util.UIUtil;
import util.I18n;

/**
 * CareMate Enterprise v7.2 International Edition
 * 外籍照顧者語言選擇：繁中、英文、越南文、印尼文、Filipino。
 */
public class LanguageFrame extends BaseFrame {

    private String selectedLanguageCode = "zh_TW";
    private JButton selectedButton;

    public LanguageFrame() {
        super("語言選擇");
        title(I18n.t("language.title"));

        JLabel heroIcon = UIUtil.icon("translate", 58, UIUtil.PRIMARY);
        heroIcon.setBounds(151, 10, 58, 58);
        content.add(heroIcon);

        JLabel mainTitle = UIUtil.h2Label(I18n.t("language.choose"));
        mainTitle.setHorizontalAlignment(SwingConstants.CENTER);
        mainTitle.setBounds(40, 78, 280, 30);
        content.add(mainTitle);

        JLabel subtitle = UIUtil.hint(I18n.t("language.hint"));
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);
        subtitle.setBounds(20, 112, 320, 24);
        content.add(subtitle);

        addLanguageButton("繁體中文", "TW", "zh_TW", 24, 154);
        addLanguageButton("English", "EN", "en", 188, 154);
        addLanguageButton("Tiếng Việt", "VN", "vi", 24, 244);
        addLanguageButton("Bahasa Indonesia", "ID", "id", 188, 244);
        addLanguageButton("Filipino", "PH", "fil", 106, 334);

        JButton next = UIUtil.btn("下一步登入");
        next.setBounds(42, 430, 276, 52);
        content.add(next);
        next.addActionListener(e -> {
            I18n.setLanguage(selectedLanguageCode);
            dispose();
            new LoginFrame("FOREIGN_CAREGIVER").setVisible(true);
        });

        JButton backRole = UIUtil.outlineBtn("返回身分選擇");
        backRole.setBounds(88, 496, 184, 42);
        content.add(backRole);
        backRole.addActionListener(e -> {
            dispose();
            new RoleFrame().setVisible(true);
        });
    }

    private void addLanguageButton(String name, String code, String localeCode, int x, int y) {
        JButton btn = UIUtil.ghostBtn("<html><center><span style='font-size:18px;font-weight:bold;'>" + code + "</span><br>" + name + "</center></html>");
        btn.setBounds(x, y, 148, 72);
        btn.setHorizontalAlignment(SwingConstants.CENTER);
        btn.setVerticalAlignment(SwingConstants.CENTER);
        btn.addActionListener(e -> selectLanguage(btn, localeCode));
        content.add(btn);

        if (selectedButton == null) {
            selectLanguage(btn, localeCode);
        }
    }

    private void selectLanguage(JButton btn, String localeCode) {
        selectedLanguageCode = localeCode;
        if (selectedButton != null) {
            selectedButton.setBackground(UIUtil.NAV_BG);
            selectedButton.setForeground(UIUtil.DARK);
        }
        selectedButton = btn;
        btn.setBackground(UIUtil.PRIMARY);
        btn.setForeground(Color.WHITE);
    }
}
