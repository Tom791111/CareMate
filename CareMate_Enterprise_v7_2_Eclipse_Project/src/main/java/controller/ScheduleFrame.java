package controller;

import dao.impl.GenericCrudDao;
import model.CrudRecord;
import service.CrudService;
import service.impl.GenericCrudService;
import util.UIUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * CareMate Final v5.3 - Apple Calendar style 照顧安排
 * 可點選日期、新增/修改/刪除照護安排，資料寫入 MySQL schedule 資料表。
 */
public class ScheduleFrame extends BaseFrame {
    private final String[] week = {"一", "二", "三", "四", "五", "六", "日"};
    private YearMonth currentMonth = YearMonth.now();
    private LocalDate selectedDate = LocalDate.now();

    private final CrudService service = new GenericCrudService(
            new GenericCrudDao<CrudRecord>("schedule", "schedule_id", CrudRecord.class)
    );

    private JPanel calendarCard;
    private JPanel agendaCard;
    private JLabel monthLabel;
    private JLabel selectedLabel;
    private JTextField titleField = UIUtil.input();
    private JTextField startField = UIUtil.input();
    private JTextField endField = UIUtil.input();
    private JTextField locationField = UIUtil.input();
    private JTextField noteField = UIUtil.input();
    private JTable table = new JTable();
    private DefaultTableModel tableModel;
    private int selectedId = 0;

    public ScheduleFrame(String role) {
        super("照顧安排");
        this.role = role;
        title("照顧安排");
        buildHeaderTitle();
        buildCalendar();
        buildForm();
        buildAgenda();
        bottomNav();
        refreshAll();
    }

    private void buildHeaderTitle() {
        JLabel icon = UIUtil.icon("task", 28, UIUtil.BLUE);
        icon.setBounds(6, 0, 32, 32);
        content.add(icon);

        JLabel title = UIUtil.h2Label("照顧安排");
        title.setBounds(48, 0, 180, 32);
        content.add(title);

        monthLabel = UIUtil.hint("");
        monthLabel.setBounds(8, 38, 150, 22);
        content.add(monthLabel);

        JButton prev = UIUtil.ghostBtn("‹");
        prev.setFont(UIUtil.font(Font.BOLD, 22));
        prev.setBounds(224, 32, 42, 32);
        content.add(prev);
        prev.addActionListener(e -> { currentMonth = currentMonth.minusMonths(1); refreshAll(); });

        JButton next = UIUtil.ghostBtn("›");
        next.setFont(UIUtil.font(Font.BOLD, 22));
        next.setBounds(272, 32, 42, 32);
        content.add(next);
        next.addActionListener(e -> { currentMonth = currentMonth.plusMonths(1); refreshAll(); });

        JButton today = UIUtil.outlineBtn("今天");
        today.setFont(UIUtil.font(Font.BOLD, 12));
        today.setBounds(316, 32, 42, 32);
        content.add(today);
        today.addActionListener(e -> { currentMonth = YearMonth.now(); selectedDate = LocalDate.now(); refreshAll(); });
    }

    private void buildCalendar() {
        calendarCard = UIUtil.card();
        calendarCard.setBounds(0, 68, 360, 248);
        content.add(calendarCard);
    }

    private void buildForm() {
        JPanel form = UIUtil.card();
        form.setBounds(0, 328, 360, 226);
        content.add(form);

        selectedLabel = UIUtil.h3Label("新增照護安排");
        selectedLabel.setBounds(16, 14, 260, 26);
        form.add(selectedLabel);

        addField(form, "內容", titleField, 16, 66, 156);
        addField(form, "地點/類型", locationField, 188, 66, 150);
        addField(form, "開始", startField, 16, 126, 94);
        addField(form, "結束", endField, 120, 126, 94);
        addField(form, "備註", noteField, 224, 126, 114);

        JButton save = UIUtil.btn("新增");
        JButton update = UIUtil.outlineBtn("修改");
        JButton delete = UIUtil.dangerBtn("刪除");
        JButton clear = UIUtil.ghostBtn("清除");
        save.setFont(UIUtil.font(Font.BOLD, 14));
        update.setFont(UIUtil.font(Font.BOLD, 14));
        delete.setFont(UIUtil.font(Font.BOLD, 14));
        clear.setFont(UIUtil.font(Font.BOLD, 14));
        save.setBounds(16, 180, 76, 34);
        update.setBounds(100, 180, 76, 34);
        delete.setBounds(184, 180, 76, 34);
        clear.setBounds(268, 180, 70, 34);
        form.add(save); form.add(update); form.add(delete); form.add(clear);

        save.addActionListener(e -> addSchedule());
        update.addActionListener(e -> updateSchedule());
        delete.addActionListener(e -> deleteSchedule());
        clear.addActionListener(e -> clearForm());
    }

    private void buildAgenda() {
        agendaCard = UIUtil.card();
        agendaCard.setBounds(0, 568, 360, 138);
        content.add(agendaCard);

        JLabel listTitle = UIUtil.h3Label("當日安排");
        listTitle.setBounds(16, 10, 130, 22);
        agendaCard.add(listTitle);

        tableModel = new DefaultTableModel(new String[]{"ID", "內容", "開始", "結束", "地點", "備註"}, 0);
        table.setModel(tableModel);
        UIUtil.styleTable(table);
        table.setFont(UIUtil.font(Font.PLAIN, 12));
        table.setRowHeight(30);
        table.getTableHeader().setFont(UIUtil.font(Font.BOLD, 12));
        table.getColumnModel().getColumn(0).setPreferredWidth(36);
        table.getColumnModel().getColumn(1).setPreferredWidth(88);
        table.getColumnModel().getColumn(2).setPreferredWidth(58);
        table.getColumnModel().getColumn(3).setPreferredWidth(58);
        table.getColumnModel().getColumn(4).setPreferredWidth(72);
        table.getColumnModel().getColumn(5).setPreferredWidth(90);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(14, 42, 332, 80);
        agendaCard.add(sp);
        table.getSelectionModel().addListSelectionListener(e -> selectRow());
    }

    private void addField(JPanel p, String label, JTextField f, int x, int y, int w) {
        JLabel l = UIUtil.hint(label);
        l.setBounds(x, y - 20, w, 18);
        p.add(l);
        f.setBounds(x, y, w, 34);
        p.add(f);
    }

    private void refreshAll() {
        monthLabel.setText(currentMonth.getYear() + " 年 " + currentMonth.getMonthValue() + " 月");
        if (!YearMonth.from(selectedDate).equals(currentMonth)) {
            selectedDate = currentMonth.atDay(1);
        }
        renderCalendar();
        loadAgenda();
        fillDefaultTime();
    }

    private void renderCalendar() {
        calendarCard.removeAll();
        int startX = 12;
        int cellW = 48;
        for (int i = 0; i < 7; i++) {
            JLabel w = new JLabel(week[i], SwingConstants.CENTER);
            w.setFont(UIUtil.font(Font.BOLD, 13));
            w.setForeground(i >= 5 ? UIUtil.RED : UIUtil.DARK);
            w.setBounds(startX + i * cellW, 12, 42, 22);
            calendarCard.add(w);
        }

        int firstDow = currentMonth.atDay(1).getDayOfWeek().getValue();
        int days = currentMonth.lengthOfMonth();
        int rowH = 34;
        int y0 = 38;
        for (int day = 1; day <= days; day++) {
            int index = firstDow - 1 + day - 1;
            int row = index / 7;
            int col = index % 7;
            LocalDate date = currentMonth.atDay(day);
            JPanel cell = new JPanel(null);
            cell.setOpaque(false);
            cell.setBounds(startX + col * cellW, y0 + row * rowH, 42, 32);
            calendarCard.add(cell);

            boolean selected = date.equals(selectedDate);
            boolean today = date.equals(LocalDate.now());
            JLabel d = new JLabel(String.valueOf(day), SwingConstants.CENTER);
            d.setFont(UIUtil.font(selected ? Font.BOLD : Font.PLAIN, 13));
            d.setForeground(selected ? Color.WHITE : (col >= 5 ? UIUtil.RED : UIUtil.DARK));
            d.setOpaque(selected || today);
            d.setBackground(selected ? UIUtil.PRIMARY : new Color(255, 238, 222));
            d.setBounds(7, 0, 28, 22);
            cell.add(d);

            String summary = eventSummary(date);
            if (!summary.isEmpty()) {
                JLabel dot = new JLabel("●", SwingConstants.CENTER);
                dot.setFont(UIUtil.font(Font.BOLD, 8));
                dot.setForeground(UIUtil.PRIMARY_DARK);
                dot.setBounds(0, 22, 42, 8);
                cell.add(dot);
                cell.setToolTipText(summary);
            }

            cell.setCursor(new Cursor(Cursor.HAND_CURSOR));
            cell.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    selectedDate = date;
                    clearForm();
                    refreshAll();
                }
            });
        }
        calendarCard.revalidate();
        calendarCard.repaint();
    }

    private void loadAgenda() {
        selectedLabel.setText(selectedDate.format(DateTimeFormatter.ofPattern("M/d")) + " 新增照護安排");
        tableModel.setRowCount(0);
        try {
            List<CrudRecord> all = service.getAll();
            for (CrudRecord r : all) {
                if (selectedDate.toString().equals(r.getRecordTime())) {
                    tableModel.addRow(new Object[]{r.getId(), r.getCol1(), r.getCol2(), r.getCol3(), r.getCol4(), r.getNote()});
                }
            }
        } catch (Exception ex) {
            UIUtil.error(this, ex);
        }
    }

    private String eventSummary(LocalDate date) {
        StringBuilder sb = new StringBuilder();
        try {
            for (CrudRecord r : service.getAll()) {
                if (date.toString().equals(r.getRecordTime())) {
                    if (sb.length() > 0) sb.append("、");
                    sb.append(r.getCol1());
                }
            }
        } catch (Exception ignored) { }
        return sb.toString();
    }

    private CrudRecord readForm() {
        CrudRecord r = new CrudRecord();
        r.setId(selectedId);
        r.setElderId(1);
        r.setCol1(titleField.getText().trim());
        r.setCol2(startField.getText().trim());
        r.setCol3(endField.getText().trim());
        r.setCol4(locationField.getText().trim());
        r.setNote(noteField.getText().trim());
        r.setRecordTime(selectedDate.toString());
        return r;
    }

    private void addSchedule() {
        try {
            service.add(readForm());
            UIUtil.info(this, "照顧安排已新增");
            clearForm();
            refreshAll();
        } catch (Exception ex) { UIUtil.error(this, ex); }
    }

    private void updateSchedule() {
        try {
            service.modify(readForm());
            UIUtil.info(this, "照顧安排已修改");
            clearForm();
            refreshAll();
        } catch (Exception ex) { UIUtil.error(this, ex); }
    }

    private void deleteSchedule() {
        try {
            service.remove(selectedId);
            UIUtil.info(this, "照顧安排已刪除");
            clearForm();
            refreshAll();
        } catch (Exception ex) { UIUtil.error(this, ex); }
    }

    private void selectRow() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        selectedId = Integer.parseInt(String.valueOf(tableModel.getValueAt(row, 0)));
        titleField.setText(String.valueOf(tableModel.getValueAt(row, 1)));
        startField.setText(String.valueOf(tableModel.getValueAt(row, 2)));
        endField.setText(String.valueOf(tableModel.getValueAt(row, 3)));
        locationField.setText(String.valueOf(tableModel.getValueAt(row, 4)));
        noteField.setText(String.valueOf(tableModel.getValueAt(row, 5)));
        selectedLabel.setText("編輯照護安排");
    }

    private void clearForm() {
        selectedId = 0;
        titleField.setText("");
        locationField.setText("");
        noteField.setText("");
        fillDefaultTime();
        table.clearSelection();
    }

    private void fillDefaultTime() {
        if (startField.getText().trim().isEmpty()) startField.setText(LocalTime.now().withMinute(0).format(DateTimeFormatter.ofPattern("HH:mm")));
        if (endField.getText().trim().isEmpty()) endField.setText(LocalTime.now().plusHours(1).withMinute(0).format(DateTimeFormatter.ofPattern("HH:mm")));
    }
}
