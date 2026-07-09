package controller;

import javax.swing.*;
import util.UIUtil;
import service.MemberService;
import service.impl.MemberServiceImpl;
import model.Member;
import java.awt.*;

public class LoginFrame extends BaseFrame {
    private JTextField account = UIUtil.input();
    private JPasswordField pwd = UIUtil.passwordInput();
    private MemberService service = new MemberServiceImpl();

    public LoginFrame(String role) {
        super("登入");
        this.role = role;
        buildHeader();
        buildLoginCard();
    }

    private void buildHeader() {
        header.removeAll();
        JButton back = UIUtil.ghostBtn("←");
        back.setFont(UIUtil.h2());
        back.setBounds(8, 8, 48, 42);
        header.add(back);
        back.addActionListener(e -> { dispose(); new RoleFrame().setVisible(true); });

        JLabel brand = new JLabel("CareMate");
        brand.setFont(UIUtil.h3());
        brand.setForeground(UIUtil.PRIMARY_DARK);
        brand.setBounds(66, 8, 170, 22);
        header.add(brand);

        JLabel sub = UIUtil.hint(roleName(role));
        sub.setBounds(66, 34, 210, 20);
        header.add(sub);

        JLabel time = UIUtil.clockLabel();
        time.setHorizontalAlignment(SwingConstants.RIGHT);
        time.setBounds(170, 58, 180, 18);
        header.add(time);
    }

    private void buildLoginCard() {
        content.removeAll();

        JPanel mark = UIUtil.softCard();
        mark.setBounds(136, 28, 88, 88);
        content.add(mark);
        JLabel icon = new JLabel(roleShort(role), SwingConstants.CENTER);
        icon.setFont(new Font("Microsoft JhengHei", Font.BOLD, 30));
        icon.setForeground(roleColor(role));
        icon.setBounds(0, 18, 80, 42);
        mark.add(icon);

        JLabel title = new JLabel("歡迎回來", SwingConstants.CENTER);
        title.setFont(UIUtil.titleFont());
        title.setForeground(UIUtil.DARK);
        title.setBounds(20, 130, 320, 40);
        content.add(title);

        JLabel hint = UIUtil.hint("請輸入帳號與密碼登入 CareMate");
        hint.setHorizontalAlignment(SwingConstants.CENTER);
        hint.setBounds(20, 172, 320, 24);
        content.add(hint);

        JPanel card = UIUtil.card();
        card.setBounds(0, 224, 360, 278);
        content.add(card);

        field(card, "帳號", account, 30);
        field(card, "密碼", pwd, 104);

        JButton login = UIUtil.btn("登入");
        login.setBounds(28, 174, 304, 46);
        card.add(login);

        JButton reg = UIUtil.outlineBtn("建立我的帳戶");
        reg.setBounds(28, 228, 304, 40);
        card.add(reg);

        login.addActionListener(e -> login());
        reg.addActionListener(e -> { dispose(); new RegisterFrame(role).setVisible(true); });
    }

    private void field(JPanel panel, String label, JTextField input, int y) {
        JLabel l = UIUtil.label(label);
        l.setBounds(28, y, 100, 24);
        panel.add(l);
        input.setBounds(28, y + 30, 304, 38);
        panel.add(input);
    }

    private String roleName(String r) {
        if ("ELDER".equals(r)) return "被照顧者登入";
        if ("FOREIGN_CAREGIVER".equals(r)) return "外籍照顧者登入";
        if ("ADMIN".equals(r)) return "管理者登入";
        return "家屬 / 照顧者登入";
    }

    private String roleShort(String r) {
        if ("ELDER".equals(r)) return "長輩";
        if ("FOREIGN_CAREGIVER".equals(r)) return "照護";
        if ("ADMIN".equals(r)) return "管理";
        return "家屬";
    }

    private Color roleColor(String r) {
        if ("ELDER".equals(r)) return UIUtil.YELLOW;
        if ("FOREIGN_CAREGIVER".equals(r)) return UIUtil.BLUE;
        if ("ADMIN".equals(r)) return UIUtil.RED;
        return UIUtil.GREEN;
    }

    private void login() {
        try {
            Member m = service.login(account.getText(), new String(pwd.getPassword()), role);
            dispose();
            new HomeFrame(m.getRole()).setVisible(true);
        } catch (Exception ex) {
            new LoginErrorFrame(role, ex.getMessage()).setVisible(true);
        }
    }
}
