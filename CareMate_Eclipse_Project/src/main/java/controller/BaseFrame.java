package controller;

import javax.swing.*;
import java.awt.*;
import util.UIUtil;

public class BaseFrame extends JFrame {
    protected String role;
    protected JPanel root, phone, header, content, nav;
    protected Color roleColor = UIUtil.PRIMARY;

    public BaseFrame(String title) {
        setTitle("CareMate - " + title);
        setSize(980, 980);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(UIUtil.BG);

        root = new JPanel(null); root.setOpaque(false); root.setBounds(0,0,980,980); add(root);
        phone = UIUtil.softCard(); phone.setBounds(285, 18, 410, 900); root.add(phone);
        header = new JPanel(null); header.setOpaque(false); header.setBounds(24, 18, 360, 78); phone.add(header);
        content = new JPanel(null); content.setOpaque(false); content.setBounds(24, 104, 360, 710); phone.add(content);
        nav = new JPanel(null); nav.setOpaque(false); nav.setBounds(24, 828, 360, 58); phone.add(nav);
    }

    protected void title(String text) {
        JButton back = UIUtil.iconGhost("back", 22); back.setBounds(0, 8, 48, 42); header.add(back);
        back.addActionListener(e -> goHome());
        JLabel brand = new JLabel("CareMate"); brand.setFont(UIUtil.h3()); brand.setForeground(UIUtil.PRIMARY_DARK); brand.setBounds(58, 8, 120, 22); header.add(brand);
        JLabel sub = UIUtil.hint(text); sub.setBounds(58, 34, 210, 20); header.add(sub);
        JButton bell = UIUtil.iconGhost("bell", 24); bell.setBounds(312, 8, 42, 42); header.add(bell);
        bell.setToolTipText("尚未紀錄提醒");
        bell.addActionListener(e -> open("MissingRecordFrame"));
        JLabel time = UIUtil.clockLabel(); time.setHorizontalAlignment(SwingConstants.RIGHT); time.setBounds(170, 55, 180, 18); header.add(time);
    }

    protected void bottomNav() {
        String[] names = {"首頁", "任務", "新增", "通知", "帳戶"};
        String[] icons = {"home", "task", "plus", "bell", "account"};
        String[] cls = {"HomeFrame", "CareTaskFrame", "HealthFrame", "NotificationFrame", "AccountFrame"};
        int x=0;
        for(int i=0;i<names.length;i++){
            JButton b = UIUtil.ghostBtn("<html><center><br>"+names[i]+"</center></html>");
            b.setIcon(new UIUtil.LineIcon(icons[i], 20, UIUtil.DARK));
            b.setHorizontalTextPosition(SwingConstants.CENTER);
            b.setVerticalTextPosition(SwingConstants.BOTTOM);
            b.setFont(UIUtil.tabFont()); b.setBounds(x, 2, 66, 52); nav.add(b);
            final String c=cls[i]; b.addActionListener(e -> open(c)); x += 73;
        }
    }

    protected JButton back(){ JButton b=UIUtil.outlineBtn("返回首頁"); b.setBounds(16, 530, 138, 42); content.add(b); b.addActionListener(e->goHome()); return b; }
    protected void goHome(){ dispose(); new HomeFrame(role).setVisible(true); }
    protected void open(String cls){ try{ dispose(); BaseFrame f=(BaseFrame)Class.forName("controller."+cls).getConstructor(String.class).newInstance(role); f.setVisible(true);}catch(Exception ex){UIUtil.error(this, ex);} }
}
