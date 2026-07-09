package controller;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import model.CrudRecord;
import service.CrudService;
import service.impl.CallLogServiceImpl;
import service.impl.ContactServiceImpl;
import util.UIUtil;

/**
 * CareMate 視訊通話頁 v5.3
 * 參考 Apple iPhone FaceTime / Phone 介面：聯絡人卡片、通話預覽、圓形控制鍵、通話紀錄。
 * 資料仍透過 MVC + Service + DAO 寫入 MySQL call_log。
 */
public class VideoCallFrame extends BaseFrame {
    private final CrudService service = new CallLogServiceImpl();
    private JComboBox<String> contactBox;
    private JComboBox<String> typeBox;
    private JLabel statusLabel;
    private JLabel timerLabel;
    private JTable table;
    private DefaultTableModel tableModel;
    private Timer callTimer;
    private int seconds = 0;
    private boolean inCall = false;
    private String currentName = "王小明";

    public VideoCallFrame(String role){
        super("視訊通話");
        this.role = role;
        title("視訊通話");
        buildPage();
        bottomNav();
        loadLogs();
    }

    private void buildPage(){
        JLabel pageIcon = UIUtil.icon("call", 28, UIUtil.PRIMARY);
        pageIcon.setBounds(4, 0, 32, 32);
        content.add(pageIcon);

        JLabel pageTitle = UIUtil.title("視訊通話");
        pageTitle.setFont(UIUtil.font(Font.BOLD, 24));
        pageTitle.setBounds(42, 0, 180, 34);
        content.add(pageTitle);

        JLabel sub = UIUtil.hint("選擇聯絡人後可開始視訊，結束後自動寫入通話紀錄。");
        sub.setBounds(8, 34, 345, 22);
        content.add(sub);

        buildContactCard();
        buildCallPreview();
        buildControlPanel();
        buildRecentLogs();
    }

    private void buildContactCard(){
        JPanel card = UIUtil.card();
        card.setBounds(0, 62, 360, 118);
        content.add(card);

        JLabel avatar = new AvatarLabel("王");
        avatar.setBounds(18, 18, 62, 62);
        card.add(avatar);

        JLabel label = UIUtil.hint("通話對象");
        label.setBounds(96, 14, 90, 20);
        card.add(label);

contactBox = new JComboBox<String>(loadContactNames());
        contactBox.setEditable(true);
        UIUtil.styleCombo(contactBox);
        contactBox.setBounds(96, 38, 144, 38);
        card.add(contactBox);
        contactBox.addActionListener(e -> currentName = selectedContactName());

        typeBox = new JComboBox<String>(new String[]{"視訊通話", "語音通話", "家屬會議", "照護回報"});
        UIUtil.styleCombo(typeBox);
        typeBox.setBounds(248, 38, 92, 38);
        card.add(typeBox);

        JLabel hint = UIUtil.hint("可從下拉選單選擇家屬、醫師、護理站等對象");
        hint.setBounds(96, 84, 235, 24);
        card.add(hint);
    }

    private void buildCallPreview(){
        JPanel preview = new GradientCallPanel();
        preview.setBounds(0, 196, 360, 230);
        content.add(preview);

        JLabel face = new BigAvatarLabel("王");
        face.setBounds(125, 22, 106, 106);
        preview.add(face);

        JLabel name = new JLabel("FaceTime Video", SwingConstants.CENTER);
        name.setFont(UIUtil.font(Font.BOLD, 20));
        name.setForeground(Color.WHITE);
        name.setBounds(40, 134, 280, 28);
        preview.add(name);

        statusLabel = new JLabel("尚未開始通話", SwingConstants.CENTER);
        statusLabel.setFont(UIUtil.font(Font.PLAIN, 14));
        statusLabel.setForeground(new Color(250,250,250));
        statusLabel.setBounds(40, 162, 280, 24);
        preview.add(statusLabel);

        timerLabel = new JLabel("00:00", SwingConstants.CENTER);
        timerLabel.setFont(UIUtil.font(Font.BOLD, 16));
        timerLabel.setForeground(Color.WHITE);
        timerLabel.setBounds(128, 188, 100, 28);
        preview.add(timerLabel);
    }

    private void buildControlPanel(){
        JPanel panel = UIUtil.card();
        panel.setBounds(0, 440, 360, 116);
        content.add(panel);

        JButton mute = circleButton("mic", "靜音");
        JButton camera = circleButton("cam", "相機");
        JButton speaker = circleButton("speaker", "擴音");
        JButton start = circleButton("start", "開始");
        JButton end = circleButton("end", "結束");

        mute.setBounds(20, 18, 54, 54);
        camera.setBounds(88, 18, 54, 54);
        speaker.setBounds(156, 18, 54, 54);
        start.setBounds(224, 18, 54, 54);
        end.setBounds(292, 18, 54, 54);
        panel.add(mute); panel.add(camera); panel.add(speaker); panel.add(start); panel.add(end);

        addControlText(panel, "靜音", 20);
        addControlText(panel, "相機", 88);
        addControlText(panel, "擴音", 156);
        addControlText(panel, "開始", 224);
        addControlText(panel, "結束", 292);

        mute.addActionListener(e -> toggleButton(mute, "已靜音", "靜音"));
        camera.addActionListener(e -> toggleButton(camera, "相機關閉", "相機"));
        speaker.addActionListener(e -> toggleButton(speaker, "擴音開啟", "擴音"));
        start.addActionListener(e -> startCall());
        end.addActionListener(e -> endCall());
    }

    private void buildRecentLogs(){
        JPanel list = UIUtil.card();
        list.setBounds(0, 572, 360, 128);
        content.add(list);

        JLabel t = UIUtil.h3Label("最近通話紀錄");
        t.setBounds(18, 10, 160, 24);
        list.add(t);

        tableModel = new DefaultTableModel(new String[]{"對象", "類型", "狀態", "時間"}, 0);
        table = new JTable(tableModel);
        UIUtil.styleTable(table);
        table.setRowHeight(30);
        table.getColumnModel().getColumn(0).setPreferredWidth(72);
        table.getColumnModel().getColumn(1).setPreferredWidth(72);
        table.getColumnModel().getColumn(2).setPreferredWidth(72);
        table.getColumnModel().getColumn(3).setPreferredWidth(122);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(14, 42, 332, 72);
        list.add(sp);
    }

    private String[] loadContactNames(){
        java.util.LinkedHashSet<String> names = new java.util.LinkedHashSet<String>();
        names.add("王小明｜家屬");
        names.add("許小美｜照顧者");
        names.add("陳醫師｜醫院");
        names.add("張護理師｜護理站");
        names.add("林社工｜長照窗口");
        names.add("李主任｜機構聯絡");
        try{
            CrudService contactService = new ContactServiceImpl();
            for(CrudRecord r : contactService.getAll()){
                String name = r.getCol1();
                String relation = r.getCol2();
                if(name != null && !name.trim().isEmpty()){
                    names.add(name.trim() + (relation == null || relation.trim().isEmpty() ? "" : "｜" + relation.trim()));
                }
            }
        }catch(Exception ignore){
            // MySQL 尚未連線時仍保留預設通話對象，避免畫面無法開啟。
        }
        return names.toArray(new String[0]);
    }

    private String selectedContactName(){
        Object selected = contactBox == null ? null : contactBox.getSelectedItem();
        String value = selected == null ? "" : selected.toString().trim();
        int sep = value.indexOf('｜');
        return sep >= 0 ? value.substring(0, sep).trim() : value;
    }

    private JButton circleButton(String type, String tooltip){
        JButton b = new RoundControlButton(type);
        b.setToolTipText(tooltip);
        return b;
    }

    private void addControlText(JPanel p, String text, int x){
        JLabel l = UIUtil.hint(text);
        l.setHorizontalAlignment(SwingConstants.CENTER);
        l.setBounds(x - 2, 76, 58, 22);
        p.add(l);
    }

    private void toggleButton(JButton b, String onText, String offText){
        Object active = b.getClientProperty("active");
        boolean isActive = active instanceof Boolean && ((Boolean)active);
        b.putClientProperty("active", !isActive);
        statusLabel.setText(isActive ? offText : onText);
        b.repaint();
    }

    private void startCall(){
        currentName = selectedContactName();
        if(currentName.isEmpty()){
            UIUtil.info(this, "請先選擇通話對象");
            return;
        }
        if(inCall) return;
        inCall = true;
        seconds = 0;
        statusLabel.setText("正在與「" + currentName + "」通話中");
        if(callTimer != null) callTimer.stop();
        callTimer = new Timer(1000, e -> {
            seconds++;
            int m = seconds / 60;
            int s = seconds % 60;
            timerLabel.setText(String.format("%02d:%02d", m, s));
        });
        callTimer.start();
    }

    private void endCall(){
        if(!inCall){
            statusLabel.setText("尚未開始通話");
            return;
        }
        inCall = false;
        if(callTimer != null) callTimer.stop();
        statusLabel.setText("通話已結束，已建立紀錄");
        saveLog("已完成", seconds);
        timerLabel.setText("00:00");
        seconds = 0;
        loadLogs();
    }

    private void saveLog(String status, int duration){
        try{
            CrudRecord r = new CrudRecord();
            r.setElderId(1);
            r.setCol1(currentName);
            r.setCol2(String.valueOf(typeBox.getSelectedItem()));
            r.setCol3(status);
            r.setCol4(duration + " 秒");
            r.setNote("iPhone 風格視訊通話介面建立");
            r.setRecordTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
            service.add(r);
        }catch(Exception ex){
            UIUtil.error(this, ex);
        }
    }

    private void loadLogs(){
        tableModel.setRowCount(0);
        try{
            int count = 0;
            for(CrudRecord r : service.getAll()){
                tableModel.addRow(new Object[]{r.getCol1(), r.getCol2(), r.getCol3(), r.getRecordTime()});
                count++;
                if(count >= 6) break;
            }
        }catch(Exception ex){
            tableModel.addRow(new Object[]{"尚未連線", "MySQL", "請檢查", "db.properties"});
        }
    }

    private static class AvatarLabel extends JLabel{
        private final String txt;
        AvatarLabel(String txt){ this.txt = txt; setHorizontalAlignment(SwingConstants.CENTER); setFont(UIUtil.font(Font.BOLD, 22)); setForeground(UIUtil.PRIMARY_DARK); }
        protected void paintComponent(Graphics g){
            Graphics2D g2=(Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(255,236,220));
            g2.fillOval(0,0,getWidth()-1,getHeight()-1);
            g2.setColor(UIUtil.PRIMARY);
            g2.drawOval(1,1,getWidth()-3,getHeight()-3);
            g2.dispose();
            setText(txt);
            super.paintComponent(g);
        }
    }

    private static class BigAvatarLabel extends JLabel{
        private final String txt;
        BigAvatarLabel(String txt){ this.txt = txt; setHorizontalAlignment(SwingConstants.CENTER); setFont(UIUtil.font(Font.BOLD, 38)); setForeground(Color.WHITE); }
        protected void paintComponent(Graphics g){
            Graphics2D g2=(Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(255,255,255,44));
            g2.fillOval(0,0,getWidth()-1,getHeight()-1);
            g2.setColor(new Color(255,255,255,120));
            g2.drawOval(2,2,getWidth()-5,getHeight()-5);
            g2.dispose();
            setText(txt);
            super.paintComponent(g);
        }
    }

    private static class GradientCallPanel extends UIUtil.RoundPanel{
        GradientCallPanel(){ super(32, new Color(70, 93, 112), true); }
        protected void paintComponent(Graphics g){
            Graphics2D g2=(Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint gp = new GradientPaint(0,0,new Color(64,90,112), getWidth(), getHeight(), new Color(215,145,100));
            g2.setPaint(gp);
            g2.fillRoundRect(0,0,getWidth()-8,getHeight()-10,32,32);
            g2.setColor(new Color(255,255,255,30));
            g2.fillOval(-30,-40,150,150);
            g2.fillOval(240,140,130,130);
            g2.dispose();
            super.paintChildren(g);
        }
    }

    private static class RoundControlButton extends JButton{
        private final String type;
        RoundControlButton(String type){
            this.type = type;
            setBorderPainted(false);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        protected void paintComponent(Graphics g){
            Graphics2D g2=(Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            boolean active = Boolean.TRUE.equals(getClientProperty("active"));
            Color bg;
            Color fg;
            if("end".equals(type)) { bg = UIUtil.RED; fg = Color.WHITE; }
            else if("start".equals(type)) { bg = UIUtil.GREEN; fg = Color.WHITE; }
            else { bg = active ? UIUtil.PRIMARY : UIUtil.NAV_BG; fg = active ? Color.WHITE : UIUtil.DARK; }
            g2.setColor(bg);
            g2.fillOval(0,0,getWidth()-1,getHeight()-1);
            g2.setColor(fg);
            g2.setStroke(new BasicStroke(2.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            drawSymbol(g2, fg);
            g2.dispose();
        }
        private void drawSymbol(Graphics2D g2, Color fg){
            int w=getWidth(), h=getHeight();
            if("mic".equals(type)){
                g2.drawRoundRect(w/2-6, 12, 12, 20, 10, 10);
                g2.drawArc(w/2-12, 22, 24, 16, 200, 140);
                g2.drawLine(w/2, 38, w/2, 44);
                g2.drawLine(w/2-8, 44, w/2+8, 44);
            } else if("cam".equals(type)){
                g2.drawRoundRect(12,18,24,18,6,6);
                g2.drawLine(36,23,46,17);
                g2.drawLine(36,31,46,37);
                g2.drawLine(46,17,46,37);
            } else if("speaker".equals(type)){
                g2.drawRect(12,24,9,10);
                g2.drawLine(21,24,32,16);
                g2.drawLine(21,34,32,42);
                g2.drawLine(32,16,32,42);
                g2.drawArc(33,22,10,16,-45,90);
            } else if("start".equals(type)){
                g2.drawLine(19,34,28,42); g2.drawLine(28,42,41,20); g2.drawLine(41,20,35,18);
            } else if("end".equals(type)){
                g2.drawArc(14,24,26,22,20,140);
                g2.drawLine(16,34,24,38);
                g2.drawLine(38,34,30,38);
            }
        }
    }
}
