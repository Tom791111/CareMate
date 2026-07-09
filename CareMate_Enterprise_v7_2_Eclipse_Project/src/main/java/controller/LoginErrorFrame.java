package controller;

import javax.swing.*;
import util.UIUtil;
import java.awt.*;

public class LoginErrorFrame extends BaseFrame {
    public LoginErrorFrame(String role, String msg) {
        super("登入失敗");
        this.role = role;
        build(msg);
    }

    private void build(String msg) {
        header.removeAll();
        JButton back = UIUtil.iconGhost("back", 22);
        back.setBounds(0, 8, 48, 42);
        header.add(back);
        back.addActionListener(e -> { dispose(); new LoginFrame(role).setVisible(true); });

        JLabel brand = new JLabel("CareMate");
        brand.setFont(UIUtil.h3());
        brand.setForeground(UIUtil.PRIMARY_DARK);
        brand.setBounds(58, 8, 120, 22);
        header.add(brand);

        JLabel sub = UIUtil.hint("登入失敗");
        sub.setBounds(58, 34, 210, 20);
        header.add(sub);

        JLabel bell = UIUtil.icon("bell", 24, UIUtil.DARK);
        bell.setBounds(312, 8, 42, 42);
        header.add(bell);

        JLabel time = UIUtil.clockLabel();
        time.setHorizontalAlignment(SwingConstants.RIGHT);
        time.setBounds(170, 55, 180, 18);
        header.add(time);

        content.removeAll();

        JPanel card = UIUtil.card();
        card.setBounds(0, 72, 360, 310);
        content.add(card);

        JLabel icon = new JLabel("✕", SwingConstants.CENTER);
        icon.setFont(new Font("Microsoft JhengHei", Font.BOLD, 44));
        icon.setForeground(UIUtil.RED);
        icon.setBounds(132, 28, 96, 68);
        card.add(icon);

        JLabel title = new JLabel("無法登入", SwingConstants.CENTER);
        title.setFont(UIUtil.titleFont());
        title.setForeground(UIUtil.DARK);
        title.setBounds(20, 104, 320, 42);
        card.add(title);

        JTextArea detail = new JTextArea(msg == null ? "請重新確認帳號、密碼與 MySQL 連線設定。" : msg);
        detail.setLineWrap(true);
        detail.setWrapStyleWord(true);
        detail.setEditable(false);
        detail.setOpaque(false);
        detail.setFont(UIUtil.bodyFont());
        detail.setForeground(UIUtil.MUTED);
        detail.setBounds(34, 158, 292, 70);
        card.add(detail);

        JButton retry = UIUtil.btn("回登入頁");
        retry.setBounds(34, 242, 292, 44);
        card.add(retry);
        retry.addActionListener(e -> { dispose(); new LoginFrame(role).setVisible(true); });

        JButton register = UIUtil.outlineBtn("建立新帳戶");
        register.setBounds(34, 396, 292, 44);
        content.add(register);
        register.addActionListener(e -> { dispose(); new RegisterFrame(role).setVisible(true); });
    }
}
