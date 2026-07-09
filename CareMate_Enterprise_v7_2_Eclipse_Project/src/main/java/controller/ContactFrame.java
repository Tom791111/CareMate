package controller;

import javax.swing.*;
import java.awt.*;
import util.UIUtil;

public class ContactFrame extends BaseFrame {
    private final String[][] contacts = {
        {"王小明", "家屬", "phone"},
        {"許小美", "照顧者", "call"},
        {"陳醫師", "醫院", "phone"},
        {"張護理師", "護理站", "call"},
        {"林社工", "長照窗口", "phone"},
        {"李主任", "機構聯絡", "call"}
    };

    public ContactFrame(String role) {
        super("聯絡人");
        this.role = role;
        title("聯絡人");

        JLabel icon = UIUtil.icon("contact", 30, UIUtil.BLUE);
        icon.setBounds(82, 0, 38, 38);
        content.add(icon);

        JLabel pageTitle = UIUtil.h2Label("聯絡人");
        pageTitle.setBounds(130, 3, 150, 32);
        content.add(pageTitle);

        JTextField search = UIUtil.input();
        search.setText("搜尋姓名 / 電話");
        search.setForeground(UIUtil.MUTED);
        search.setBounds(0, 50, 360, 42);
        content.add(search);

        JPanel listCard = UIUtil.card();
        listCard.setBounds(0, 108, 360, 424);
        content.add(listCard);

        JPanel list = new JPanel(null);
        list.setOpaque(false);
        list.setPreferredSize(new Dimension(330, contacts.length * 62 + 8));
        int y = 4;
        for (String[] c : contacts) {
            addContactRow(list, c[0], c[1], y);
            y += 62;
        }

        JScrollPane scroll = new JScrollPane(list);
        scroll.setBounds(12, 12, 326, 388);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        listCard.add(scroll);

        JButton add = UIUtil.btn("＋ 新增聯絡人");
        add.setBounds(32, 550, 296, 46);
        content.add(add);
        add.addActionListener(e -> UIUtil.info(this, "可在正式版串接 Contact CRUD 新增聯絡人"));

        JLabel note = UIUtil.hint("可管理家屬、醫院、護理站與緊急聯絡人");
        note.setHorizontalAlignment(SwingConstants.CENTER);
        note.setBounds(20, 604, 320, 22);
        content.add(note);

        bottomNav();
    }

    private void addContactRow(JPanel parent, String name, String relation, int y) {
        JPanel row = UIUtil.navCard();
        row.setBounds(0, y, 322, 54);
        parent.add(row);

        JLabel avatar = UIUtil.icon("account", 24, UIUtil.PRIMARY);
        avatar.setBounds(8, 8, 36, 36);
        row.add(avatar);

        JLabel nameLabel = UIUtil.h3Label(name);
        nameLabel.setBounds(52, 7, 110, 24);
        row.add(nameLabel);

        JLabel sub = UIUtil.hint(relation);
        sub.setBounds(52, 30, 120, 18);
        row.add(sub);

        JButton phone = UIUtil.ghostBtn("");
        phone.setIcon(new UIUtil.LineIcon("call", 22, UIUtil.GREEN));
        phone.setBounds(214, 8, 42, 38);
        row.add(phone);
        phone.addActionListener(e -> UIUtil.info(this, "準備撥打給：" + name));

        JButton profile = UIUtil.ghostBtn("");
        profile.setIcon(new UIUtil.LineIcon("contact", 22, UIUtil.GREEN));
        profile.setBounds(264, 8, 42, 38);
        row.add(profile);
        profile.addActionListener(e -> UIUtil.info(this, "查看聯絡人資料：" + name));
    }
}
