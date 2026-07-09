package controller;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import model.CrudRecord;
import service.impl.TranslationServiceImpl;
import util.UIUtil;

/**
 * CareMate 即時翻譯頁面 v5.3
 * 參考 CareTranslateAI_Japandi_v2_5 的 Japandi 卡片式語音翻譯流程：
 * 1. 大型「按住說話」按鈕
 * 2. 常用照護語句快速翻譯
 * 3. 中文 / 越南文 / 印尼文 / 菲律賓英文同步顯示
 * 4. 翻譯結果自動寫入 MySQL translation_history
 */
public class TranslateFrame extends BaseFrame {
    private final TranslationServiceImpl service = new TranslationServiceImpl();
    private final JComboBox<String> phraseBox = new JComboBox<>();
    private final JComboBox<String> targetBox = new JComboBox<>(new String[]{"越南文", "印尼文", "菲律賓英文", "全部語言"});
    private final JTextArea resultArea = new JTextArea();
    private final DefaultTableModel tableModel = new DefaultTableModel(new String[]{"ID", "中文", "譯文", "語言", "時間"}, 0);
    private final JTable table = new JTable(tableModel);

    private final Map<String, String[]> dictionary = new LinkedHashMap<>();

    public TranslateFrame(String role) {
        super("即時翻譯");
        this.role = role;
        seedDictionary();
        title("即時翻譯");
        buildContent();
        bottomNav();
        loadRecords();
    }

    private void buildContent() {
        JLabel icon = UIUtil.icon("translate", 28, UIUtil.PRIMARY);
        icon.setBounds(2, 0, 32, 32);
        content.add(icon);

        JLabel page = UIUtil.h2Label("即時翻譯");
        page.setBounds(42, 0, 180, 34);
        content.add(page);

        JLabel sub = UIUtil.hint("按下語音按鈕或選擇常用照護語句，即可翻譯並保存紀錄。");
        sub.setBounds(8, 34, 342, 22);
        content.add(sub);

        JPanel hero = UIUtil.card();
        hero.setBounds(0, 64, 360, 260);
        content.add(hero);

        JButton speak = new JButton("<html><center>按住說話<br><span style='font-size:11px'>MVP 示範：點擊後翻譯常用照護語</span></center></html>");
        speak.setFont(UIUtil.font(Font.BOLD, 24));
        speak.setForeground(Color.WHITE);
        speak.setBackground(new Color(171, 137, 103));
        speak.setBorderPainted(false);
        speak.setFocusPainted(false);
        speak.setContentAreaFilled(false);
        speak.setOpaque(true);
        speak.setCursor(new Cursor(Cursor.HAND_CURSOR));
        speak.setBounds(65, 28, 230, 170);
        hero.add(speak);
        speak.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) { speak.setText("<html><center>聆聽中...<br><span style='font-size:11px'>請說出照護提醒</span></center></html>"); }
            public void mouseReleased(MouseEvent e) { speak.setText("<html><center>按住說話<br><span style='font-size:11px'>MVP 示範：點擊後翻譯常用照護語</span></center></html>"); translateSelected(); }
        });

        JLabel hint = UIUtil.hint("目前為 MVP 示範，可擴充接入語音辨識 API。");
        hint.setHorizontalAlignment(SwingConstants.CENTER);
        hint.setBounds(26, 205, 300, 22);
        hero.add(hint);

        JPanel form = UIUtil.card();
        form.setBounds(0, 336, 360, 158);
        content.add(form);

        JLabel pLabel = UIUtil.hint("常用照護語句");
        pLabel.setBounds(18, 14, 140, 20);
        form.add(pLabel);
        for (String key : dictionary.keySet()) phraseBox.addItem(key);
        phraseBox.setBounds(18, 40, 154, 38);
        UIUtil.styleCombo(phraseBox);
        form.add(phraseBox);

        JLabel tLabel = UIUtil.hint("目標語言");
        tLabel.setBounds(190, 14, 140, 20);
        form.add(tLabel);
        targetBox.setBounds(190, 40, 154, 38);
        UIUtil.styleCombo(targetBox);
        form.add(targetBox);

        JButton doTranslate = UIUtil.btn("翻譯");
        doTranslate.setBounds(18, 96, 102, 42);
        form.add(doTranslate);
        doTranslate.addActionListener(e -> translateSelected());

        JButton clear = UIUtil.ghostBtn("清除");
        clear.setBounds(132, 96, 92, 42);
        form.add(clear);
        clear.addActionListener(e -> resultArea.setText(""));

        JButton record = UIUtil.outlineBtn("紀錄");
        record.setBounds(236, 96, 108, 42);
        form.add(record);
        record.addActionListener(e -> loadRecords());

        JPanel result = UIUtil.card();
        result.setBounds(0, 510, 360, 150);
        content.add(result);
        JLabel rTitle = UIUtil.h3Label("翻譯結果");
        rTitle.setBounds(18, 12, 120, 24);
        result.add(rTitle);
        resultArea.setFont(UIUtil.bodyFont());
        resultArea.setForeground(UIUtil.DARK);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setEditable(false);
        resultArea.setBackground(Color.WHITE);
        JScrollPane rScroll = new JScrollPane(resultArea);
        rScroll.setBorder(BorderFactory.createLineBorder(UIUtil.LINE));
        rScroll.setBounds(18, 44, 324, 86);
        result.add(rScroll);

        JPanel list = UIUtil.card();
        list.setBounds(0, 676, 360, 118);
        content.add(list);
        JLabel lTitle = UIUtil.h3Label("近期紀錄");
        lTitle.setBounds(18, 10, 120, 24);
        list.add(lTitle);
        UIUtil.styleTable(table);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        int[] ws = {38, 95, 120, 70, 120};
        for (int i = 0; i < ws.length; i++) table.getColumnModel().getColumn(i).setPreferredWidth(ws[i]);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(14, 40, 332, 58);
        list.add(sp);
    }

    private void translateSelected() {
        String zh = String.valueOf(phraseBox.getSelectedItem());
        String target = String.valueOf(targetBox.getSelectedItem());
        String[] trans = dictionary.get(zh);
        String text;
        if ("越南文".equals(target)) {
            text = "中文：" + zh + "\n越南文：" + trans[0];
            save(zh, "越南文", trans[0]);
        } else if ("印尼文".equals(target)) {
            text = "中文：" + zh + "\n印尼文：" + trans[1];
            save(zh, "印尼文", trans[1]);
        } else if ("菲律賓英文".equals(target)) {
            text = "中文：" + zh + "\n菲律賓英文：" + trans[2];
            save(zh, "菲律賓英文", trans[2]);
        } else {
            text = "中文：" + zh + "\n越南文：" + trans[0] + "\n印尼文：" + trans[1] + "\n菲律賓英文：" + trans[2];
            save(zh, "全部語言", trans[0] + " / " + trans[1] + " / " + trans[2]);
        }
        resultArea.setText(text);
        JOptionPane.showMessageDialog(this, text, "CareMate 即時翻譯", JOptionPane.INFORMATION_MESSAGE);
        loadRecords();
    }

    private void save(String zh, String lang, String translated) {
        try {
            CrudRecord r = new CrudRecord();
            r.setElderId(1);
            r.setCol1("中文");
            r.setCol2(lang);
            r.setCol3(zh);
            r.setCol4(translated);
            r.setNote("CareTranslateAI Japandi MVP 翻譯紀錄");
            r.setRecordTime(LocalDateTime.now().toString().substring(0, 16));
            service.add(r);
        } catch (Exception ex) {
            UIUtil.error(this, ex);
        }
    }

    private void loadRecords() {
        tableModel.setRowCount(0);
        try {
            int count = 0;
            for (CrudRecord r : service.getAll()) {
                tableModel.addRow(new Object[]{r.getId(), r.getCol3(), r.getCol4(), r.getCol2(), r.getRecordTime()});
                if (++count >= 5) break;
            }
        } catch (Exception ex) {
            UIUtil.error(this, ex);
        }
    }

    private void seedDictionary() {
        dictionary.put("請喝水。", new String[]{"Xin hãy uống nước.", "Silakan minum air.", "Please drink water."});
        dictionary.put("請記得吃藥。", new String[]{"Xin nhớ uống thuốc.", "Tolong ingat minum obat.", "Please remember to take medicine."});
        dictionary.put("現在要量血壓。", new String[]{"Bây giờ cần đo huyết áp.", "Sekarang perlu mengukur tekanan darah.", "It is time to check blood pressure."});
        dictionary.put("請問你哪裡不舒服？", new String[]{"Bạn thấy không khỏe ở đâu?", "Bagian mana yang terasa tidak nyaman?", "Where do you feel uncomfortable?"});
        dictionary.put("我們要去散步。", new String[]{"Chúng ta sẽ đi dạo.", "Kita akan berjalan-jalan.", "We are going for a walk."});
        dictionary.put("請慢慢起身。", new String[]{"Xin hãy từ từ đứng dậy.", "Silakan bangun perlahan.", "Please get up slowly."});
    }
}
