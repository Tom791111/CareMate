package controller;

import javax.swing.*;
import javax.swing.table.*;
import util.UIUtil;
import service.CrudService;
import model.CrudRecord;
import java.time.*;
import java.awt.*;
import java.text.MessageFormat;

/**
 * 通用 CRUD 頁面 v7.1 UX Dropdown Edition
 * 修正：版面壓縮、欄位改為可輸入的下拉式選單、被照顧者資料也改成直覺選單。
 */
public class GenericCrudFrame extends BaseFrame {
    private CrudService service;
    private JComboBox<String> c1, c2, c3, c4, note, time;
    private JTable table = new JTable();
    private DefaultTableModel model;
    private int selectedId = 0;
    private String[] ls;
    private String pageTitle;

    public GenericCrudFrame(String role, String title, String[] labels, CrudService service) {
        super(title);
        this.role = role;
        this.service = service;
        this.pageTitle = title;
        this.ls = new String[]{labels[0], labels[1], labels[2], labels[3], "備註", "時間"};
        title(title);

        c1 = combo(optionsFor(title, ls[0]));
        c2 = combo(optionsFor(title, ls[1]));
        c3 = combo(optionsFor(title, ls[2]));
        c4 = combo(optionsFor(title, ls[3]));
        note = combo(optionsFor(title, "備註"));
        time = combo(timeOptions());
        setComboValue(time, LocalDateTime.now().toString().substring(0, 16));

        JLabel pageIcon = UIUtil.icon(pageIconType(title), 28, UIUtil.PRIMARY);
        pageIcon.setBounds(4, 2, 32, 32);
        content.add(pageIcon);

        JLabel page = UIUtil.title(title);
        page.setFont(UIUtil.font(Font.BOLD, 24));
        page.setBounds(42, 0, 220, 34);
        content.add(page);

        JLabel sub = UIUtil.hint("以下欄位皆可用下拉式選單快速操作，也可自行輸入內容。");
        sub.setBounds(8, 34, 340, 22);
        content.add(sub);

        JPanel form = UIUtil.card();
        form.setBounds(0, 64, 360, 348);
        content.add(form);

        int x = 18, y = 16;
        JComboBox<?>[] fs = {c1, c2, c3, c4, note, time};
        for (int i = 0; i < fs.length; i++) {
            JLabel l = UIUtil.hint(ls[i]);
            l.setBounds(x, y, 140, 20);
            form.add(l);
            fs[i].setBounds(x, y + 24, 154, 40);
            form.add(fs[i]);
            x += 174;
            if (x > 200) {
                x = 18;
                y += 80;
            }
        }

        JButton add = UIUtil.btn("儲存");
        JButton upd = UIUtil.outlineBtn("修改");
        JButton del = UIUtil.dangerBtn("刪除");
        JButton clr = UIUtil.ghostBtn("清除");
        add.setBounds(18, 286, 78, 40);
        upd.setBounds(104, 286, 78, 40);
        del.setBounds(190, 286, 78, 40);
        clr.setBounds(276, 286, 66, 40);
        form.add(add);
        form.add(upd);
        form.add(del);
        form.add(clr);

        JPanel list = UIUtil.card();
        list.setBounds(0, 428, 360, 270);
        content.add(list);

        JLabel listTitle = UIUtil.h3Label("近期紀錄");
        listTitle.setBounds(18, 12, 120, 24);
        list.add(listTitle);

        JButton print = UIUtil.outlineBtn("列印");
        print.setFont(UIUtil.font(Font.BOLD, 12));
        print.setBounds(270, 10, 72, 32);
        list.add(print);
        print.setVisible(title.contains("報表") || title.contains("查詢") || title.contains("列印"));

        model = new DefaultTableModel(new String[]{"ID", ls[0], ls[1], ls[2], ls[3], "備註", "時間"}, 0);
        table.setModel(model);
        UIUtil.styleTable(table);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        setColumnWidths();
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(14, 50, 332, 200);
        list.add(sp);

        add.addActionListener(e -> addRecord());
        upd.addActionListener(e -> updateRecord());
        del.addActionListener(e -> deleteRecord());
        clr.addActionListener(e -> clear());
        print.addActionListener(e -> printTable());
        table.getSelectionModel().addListSelectionListener(e -> selectRow());
        bottomNav();
        loadTable();
    }

    private JComboBox<String> combo(String[] items) {
        JComboBox<String> box = new JComboBox<>(items);
        box.setEditable(true);
        UIUtil.styleCombo(box);
        return box;
    }

    private String comboValue(JComboBox<String> box) {
        Object v = box.getEditor().getItem();
        return v == null ? "" : v.toString().trim();
    }

    private void setComboValue(JComboBox<String> box, String value) {
        box.setSelectedItem(value == null ? "" : value);
    }

    private String[] timeOptions() {
        LocalDate today = LocalDate.now();
        return new String[]{
                LocalDateTime.now().toString().substring(0, 16),
                today + "T08:00", today + "T09:00", today + "T10:00",
                today + "T12:00", today + "T14:00", today + "T16:00",
                today + "T18:00", today + "T20:00"
        };
    }

    private String[] optionsFor(String title, String label) {
        if (title.contains("健康")) {
            if (label.contains("收縮")) return arr("120", "125", "130", "135", "140", "自行輸入");
            if (label.contains("舒張")) return arr("70", "75", "80", "85", "90", "自行輸入");
            if (label.contains("體溫")) return arr("36.5", "36.8", "37.0", "37.5", "38.0", "自行輸入");
            if (label.contains("時段")) return arr("早上", "中午", "下午", "晚上", "睡前");
        }
        if (title.contains("用藥")) {
            if (label.contains("藥名")) return arr("降血壓藥", "糖尿病藥", "維他命", "止痛藥", "胃藥", "自行輸入");
            if (label.contains("劑量")) return arr("半顆", "1 顆", "2 顆", "5 ml", "10 ml", "依醫囑");
            if (label.contains("時段")) return arr("早餐後", "午餐後", "晚餐後", "睡前", "必要時");
            if (label.contains("狀態")) return arr("已完成", "未完成", "延後", "忘記", "需家屬確認");
        }
        if (title.contains("飲食")) {
            if (label.contains("餐別")) return arr("早餐", "午餐", "晚餐", "點心", "飲水");
            if (label.contains("食物")) return arr("稀飯", "白飯", "麵食", "蔬菜", "水果", "牛奶", "自行輸入");
            if (label.contains("飲水")) return arr("100", "200", "300", "500", "800", "自行輸入");
            if (label.contains("營養")) return arr("正常", "偏少", "食慾不佳", "需補充水分", "需家屬注意");
        }
        if (title.contains("情緒")) {
            if (label.contains("情緒")) return arr("開心", "平穩", "普通", "焦慮", "低落", "生氣");
            if (label.contains("分數")) return arr("5", "4", "3", "2", "1");
            if (label.contains("狀態")) return arr("穩定", "需陪伴", "需關心", "建議視訊", "異常回報");
            if (label.contains("來源")) return arr("家屬", "照顧者", "被照顧者", "系統提醒");
        }
        if (title.contains("被照顧者")) {
            if (label.contains("姓名")) return arr("王阿公", "林奶奶", "陳伯伯", "自行輸入");
            if (label.contains("性別")) return arr("男", "女", "其他");
            if (label.contains("年齡")) return arr("65-70", "71-75", "76-80", "81-85", "86以上", "自行輸入");
            if (label.contains("照護")) return arr("一般照護", "需協助行走", "需用藥提醒", "需飲食控制", "高風險關懷");
        }
        if (title.contains("照顧者")) {
            if (label.contains("姓名")) return arr("Lina", "Maria", "Siti", "自行輸入");
            if (label.contains("國籍")) return arr("印尼", "越南", "菲律賓", "台灣", "其他");
            if (label.contains("語言")) return arr("中文", "英文", "印尼文", "越南文", "菲律賓英文");
            if (label.contains("狀態")) return arr("在職", "休假", "待確認", "已離職");
        }
        if (title.contains("帳戶")) {
            if (label.contains("帳號")) return arr("family", "elder", "caregiver", "自行輸入");
            if (label.contains("角色")) return arr("FAMILY", "ELDER", "FOREIGN_CAREGIVER", "ADMIN");
            if (label.contains("狀態")) return arr("ACTIVE", "INACTIVE", "LOCKED");
        }
        if (title.contains("任務") || title.contains("日誌") || title.contains("報表") || title.contains("設定")) {
            if (label.contains("任務") || label.contains("項目")) return arr("血壓量測", "用藥提醒", "飲食紀錄", "情緒關懷", "散步復健", "自行輸入");
            if (label.contains("負責") || label.contains("項目二")) return arr("家屬", "外籍照顧者", "護理師", "社工", "自行輸入");
            if (label.contains("狀態")) return arr("已完成", "未完成", "進行中", "延後", "取消");
            if (label.contains("類型") || label.contains("項目三")) return arr("健康", "用藥", "飲食", "情緒", "醫療", "生活照護");
        }
        if (label.contains("備註")) return arr("正常", "需家屬確認", "需後續追蹤", "已通知家屬", "自行輸入");
        return arr("請選擇", "正常", "需確認", "已完成", "自行輸入");
    }

    private String[] arr(String... values) { return values; }

    private void setColumnWidths() {
        int[] widths = {42, 82, 82, 82, 82, 100, 132};
        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
    }

    private String pageIconType(String t) {
        if (t.contains("健康")) return "health";
        if (t.contains("用藥")) return "medicine";
        if (t.contains("飲食")) return "meal";
        if (t.contains("情緒")) return "mood";
        if (t.contains("SOS")) return "sos";
        if (t.contains("翻譯")) return "translate";
        if (t.contains("聯絡")) return "contact";
        if (t.contains("報表")) return "report";
        if (t.contains("安排") || t.contains("任務")) return "task";
        if (t.contains("被照顧者") || t.contains("帳戶") || t.contains("照顧者")) return "account";
        return "log";
    }

    private CrudRecord read() {
        CrudRecord r = new CrudRecord();
        r.setId(selectedId);
        r.setElderId(1);
        r.setCol1(comboValue(c1));
        r.setCol2(comboValue(c2));
        r.setCol3(comboValue(c3));
        r.setCol4(comboValue(c4));
        r.setNote(comboValue(note));
        r.setRecordTime(comboValue(time));
        return r;
    }

    private void addRecord() {
        try {
            service.add(read());
            UIUtil.info(this, "新增成功");
            clear();
            loadTable();
        } catch (Exception ex) {
            UIUtil.error(this, ex);
        }
    }

    private void updateRecord() {
        try {
            service.modify(read());
            UIUtil.info(this, "修改成功");
            clear();
            loadTable();
        } catch (Exception ex) {
            UIUtil.error(this, ex);
        }
    }

    private void deleteRecord() {
        if (selectedId <= 0) {
            UIUtil.info(this, "請先選擇一筆資料");
            return;
        }
        int ok = JOptionPane.showConfirmDialog(this, "確定要刪除此筆資料嗎？", "刪除確認", JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) return;
        try {
            service.remove(selectedId);
            UIUtil.info(this, "刪除成功");
            clear();
            loadTable();
        } catch (Exception ex) {
            UIUtil.error(this, ex);
        }
    }

    private void loadTable() {
        model.setRowCount(0);
        try {
            for (CrudRecord r : service.getAll()) {
                model.addRow(new Object[]{r.getId(), r.getCol1(), r.getCol2(), r.getCol3(), r.getCol4(), r.getNote(), r.getRecordTime()});
            }
        } catch (Exception ex) {
            UIUtil.error(this, ex);
        }
    }

    private void selectRow() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            selectedId = Integer.parseInt(model.getValueAt(row, 0).toString());
            setComboValue(c1, String.valueOf(model.getValueAt(row, 1)));
            setComboValue(c2, String.valueOf(model.getValueAt(row, 2)));
            setComboValue(c3, String.valueOf(model.getValueAt(row, 3)));
            setComboValue(c4, String.valueOf(model.getValueAt(row, 4)));
            setComboValue(note, String.valueOf(model.getValueAt(row, 5)));
            setComboValue(time, String.valueOf(model.getValueAt(row, 6)));
        }
    }

    private void clear() {
        selectedId = 0;
        c1.setSelectedIndex(0);
        c2.setSelectedIndex(0);
        c3.setSelectedIndex(0);
        c4.setSelectedIndex(0);
        note.setSelectedIndex(0);
        setComboValue(time, LocalDateTime.now().toString().substring(0, 16));
    }

    private void printTable() {
        try {
            boolean ok = table.print(
                    JTable.PrintMode.FIT_WIDTH,
                    new MessageFormat("CareMate - " + pageTitle),
                    new MessageFormat("第 {0} 頁")
            );
            if (ok) UIUtil.info(this, "列印完成");
        } catch (Exception ex) {
            UIUtil.error(this, ex);
        }
    }
}
