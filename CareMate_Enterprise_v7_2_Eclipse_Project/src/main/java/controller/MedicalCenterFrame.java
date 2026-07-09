package controller;

import util.UIUtil;
import javax.swing.*;
import java.awt.*;

public class MedicalCenterFrame extends BaseFrame {
    public MedicalCenterFrame(String role){
        super("醫療服務中心");
        this.role = role;
        title("醫療服務中心");
        buildPage();
        bottomNav();
    }

    private void buildPage(){
        JLabel page = UIUtil.title("醫療服務中心");
        page.setFont(UIUtil.font(Font.BOLD, 24));
        page.setBounds(8, 0, 260, 34);
        content.add(page);

        JLabel hint = UIUtil.hint("預約看診、查詢附近醫療院所、管理提醒。");
        hint.setBounds(8, 34, 330, 24);
        content.add(hint);

        addCard("預約診所／醫院", "選擇醫院、科別、醫師與時間", "health", 0, 72, "AppointmentFrame");
        addCard("附近醫院診所", "查詢醫院、診所、藥局與復健中心", "contact", 0, 154, "HospitalMapFrame");
        addCard("我的預約", "查看、取消、刪除看診預約", "task", 0, 236, "MyAppointmentFrame");
        addCard("常用醫院", "收藏常去的醫療院所", "bell", 0, 318, "HospitalMapFrame");
        addCard("急診資訊", "快速查詢附近急診與導航", "sos", 0, 400, "HospitalMapFrame");
        addCard("慢箋領藥", "查詢附近藥局與領藥提醒", "medicine", 0, 482, "HospitalMapFrame");

        JPanel note = UIUtil.card();
        note.setBounds(0, 584, 360, 82);
        content.add(note);
        JLabel t = UIUtil.h3Label("高齡友善提醒");
        t.setBounds(18, 12, 160, 24);
        note.add(t);
        JLabel d = UIUtil.hint("看診前請確認健保卡、慢箋、近期血壓體溫紀錄。 ");
        d.setBounds(18, 42, 310, 24);
        note.add(d);
    }

    private void addCard(String title, String desc, String iconType, int x, int y, String frameClass){
        JPanel card = UIUtil.card();
        card.setBounds(x, y, 360, 70);
        content.add(card);

        JLabel icon = UIUtil.icon(iconType, 28, UIUtil.PRIMARY);
        icon.setBounds(16, 16, 32, 32);
        card.add(icon);

        JLabel t = UIUtil.h3Label(title);
        t.setBounds(62, 10, 210, 24);
        card.add(t);

        JLabel d = UIUtil.hint(desc);
        d.setBounds(62, 36, 220, 22);
        card.add(d);

        JButton go = UIUtil.outlineBtn("前往");
        go.setFont(UIUtil.font(Font.BOLD, 12));
        go.setBounds(284, 18, 58, 32);
        go.addActionListener(e -> open(frameClass));
        card.add(go);
    }
}
