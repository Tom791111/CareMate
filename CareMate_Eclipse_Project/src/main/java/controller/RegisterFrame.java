package controller;

import javax.swing.*;
import util.UIUtil;
import model.Member;
import service.MemberService;
import service.impl.MemberServiceImpl;
import java.awt.*;

public class RegisterFrame extends BaseFrame {
    JTextField account = UIUtil.input(), name = UIUtil.input(), phone = UIUtil.input(), email = UIUtil.input();
    JPasswordField pwd = UIUtil.passwordInput();
    JComboBox<String> roleBox = new JComboBox<>(new String[]{"FAMILY", "ELDER", "FOREIGN_CAREGIVER", "ADMIN"});
    MemberService service = new MemberServiceImpl();

    public RegisterFrame(String role) {
        super("我的帳戶註冊");
        this.role = role;
        buildHeader();
        buildRegisterCard();
    }

    private void buildHeader() {
        header.removeAll();
        JButton back = UIUtil.ghostBtn("←");
        back.setFont(UIUtil.h2());
        back.setBounds(8, 8, 48, 42);
        header.add(back);
        back.addActionListener(e -> { dispose(); new LoginFrame(role).setVisible(true); });

        JLabel brand = new JLabel("CareMate");
        brand.setFont(UIUtil.h3());
        brand.setForeground(UIUtil.PRIMARY_DARK);
        brand.setBounds(66, 8, 170, 22);
        header.add(brand);

        JLabel sub = UIUtil.hint("建立我的帳戶");
        sub.setBounds(66, 34, 210, 20);
        header.add(sub);

        JLabel time = UIUtil.clockLabel();
        time.setHorizontalAlignment(SwingConstants.RIGHT);
        time.setBounds(170, 58, 180, 18);
        header.add(time);
    }

    private void buildRegisterCard() {
        content.removeAll();

        JLabel title = new JLabel("我的帳戶註冊", SwingConstants.CENTER);
        title.setFont(UIUtil.titleFont());
        title.setForeground(UIUtil.DARK);
        title.setBounds(20, 8, 320, 40);
        content.add(title);

        JLabel tip = UIUtil.hint("完成註冊後即可登入 CareMate 使用照護功能");
        tip.setHorizontalAlignment(SwingConstants.CENTER);
        tip.setBounds(15, 50, 330, 24);
        content.add(tip);

        JPanel card = UIUtil.card();
        card.setBounds(0, 86, 360, 418);
        content.add(card);

        field(card, "帳號", account, 18);
        field(card, "密碼", pwd, 78);
        field(card, "姓名", name, 138);
        field(card, "電話", phone, 198);
        field(card, "Email", email, 258);

        JLabel r = UIUtil.label("角色");
        r.setBounds(28, 318, 80, 26);
        card.add(r);
        roleBox.setSelectedItem(role);
        roleBox.setBounds(108, 316, 224, 36);
        UIUtil.styleCombo(roleBox);
        card.add(roleBox);

        JButton save = UIUtil.btn("建立帳戶");
        save.setBounds(28, 366, 304, 42);
        card.add(save);
        save.addActionListener(e -> save());
    }

    void field(JPanel panel, String s, JTextField t, int y) {
        JLabel l = UIUtil.label(s);
        l.setBounds(28, y, 80, 26);
        panel.add(l);
        t.setBounds(108, y - 2, 224, 36);
        panel.add(t);
    }

    void save() {
        try {
            Member m = new Member(account.getText(), new String(pwd.getPassword()), name.getText(), roleBox.getSelectedItem().toString());
            m.setPhone(phone.getText());
            m.setEmail(email.getText());
            service.register(m);
            UIUtil.info(this, "註冊成功，請登入");
            dispose();
            new LoginFrame(m.getRole()).setVisible(true);
        } catch (Exception ex) {
            UIUtil.error(this, ex);
        }
    }
}
