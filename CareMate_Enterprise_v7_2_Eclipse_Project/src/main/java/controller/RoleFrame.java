package controller;

import javax.swing.*;
import util.UIUtil;
import java.awt.*;

public class RoleFrame extends BaseFrame {
    public RoleFrame() {
        super("選擇身分");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        buildHeader();
        buildHero();
        buildRoleSheet();
    }

    private void buildHeader() {
        header.removeAll();
        JButton menu = UIUtil.iconGhost("menu", 22);
        menu.setBounds(8, 8, 48, 42);
        header.add(menu);

        JLabel brand = new JLabel("CareMate");
        brand.setFont(UIUtil.h3());
        brand.setForeground(UIUtil.PRIMARY_DARK);
        brand.setBounds(66, 8, 170, 22);
        header.add(brand);

        JLabel sub = UIUtil.hint("請選擇你的身分");
        sub.setBounds(66, 34, 180, 20);
        header.add(sub);

        JLabel bell = UIUtil.icon("bell", 24, UIUtil.DARK);
        bell.setBounds(315, 9, 36, 36);
        header.add(bell);

        JLabel time = UIUtil.clockLabel();
        time.setHorizontalAlignment(SwingConstants.RIGHT);
        time.setBounds(170, 58, 180, 18);
        header.add(time);
    }

    private void buildHero() {
        content.removeAll();

        JPanel mark = UIUtil.softCard();
        mark.setBounds(143, 62, 74, 74);
        content.add(mark);

        JLabel markText = UIUtil.icon("health", 42, UIUtil.PRIMARY);
        markText.setBounds(12, 10, 42, 44);
        mark.add(markText);

        JLabel title = new JLabel("安心照護，清楚記錄", SwingConstants.CENTER);
        title.setFont(UIUtil.subTitleFont());
        title.setForeground(UIUtil.DARK);
        title.setBounds(20, 156, 320, 36);
        content.add(title);

        JLabel p1 = UIUtil.hint("依照不同身分進入專屬首頁");
        p1.setHorizontalAlignment(SwingConstants.CENTER);
        p1.setBounds(20, 214, 320, 24);
        content.add(p1);

        JLabel p2 = UIUtil.hint("家屬、長輩、外籍照顧者都能快速使用");
        p2.setHorizontalAlignment(SwingConstants.CENTER);
        p2.setBounds(20, 250, 320, 24);
        content.add(p2);
    }

    private void buildRoleSheet() {
        JPanel sheet = UIUtil.card();
        sheet.setBounds(0, 344, 360, 164);
        content.add(sheet);

        JLabel sheetTitle = new JLabel("請選擇您的身分");
        sheetTitle.setFont(UIUtil.h3());
        sheetTitle.setForeground(UIUtil.DARK);
        sheetTitle.setBounds(22, 16, 200, 28);
        sheet.add(sheetTitle);

        JButton family = smallRoleButton("家屬 / 照顧者", UIUtil.GREEN);
        JButton elder = smallRoleButton("被照顧者", UIUtil.YELLOW);
        JButton caregiver = smallRoleButton("外籍照顧者", UIUtil.BLUE);
        family.setBounds(22, 56, 316, 30);
        elder.setBounds(22, 92, 152, 30);
        caregiver.setBounds(186, 92, 152, 30);
        sheet.add(family);
        sheet.add(elder);
        sheet.add(caregiver);

        JLabel language = UIUtil.hint("外籍照顧者登入前可選 TW / VN / PH / ID");
        language.setHorizontalAlignment(SwingConstants.CENTER);
        language.setBounds(20, 130, 320, 20);
        sheet.add(language);

        family.addActionListener(e -> go("FAMILY"));
        elder.addActionListener(e -> go("ELDER"));
        caregiver.addActionListener(e -> { dispose(); new LanguageFrame().setVisible(true); });
    }

    private JButton smallRoleButton(String text, Color color) {
        JButton b = UIUtil.btn(text);
        b.setBackground(color);
        b.setFont(UIUtil.bodyFont());
        return b;
    }

    private void go(String r) {
        dispose();
        new LoginFrame(r).setVisible(true);
    }
}
