package controller;

import model.Appointment;
import service.AppointmentService;
import service.impl.AppointmentServiceImpl;
import util.UIUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AppointmentFrame extends BaseFrame {
    private final AppointmentService service = new AppointmentServiceImpl();
    private JComboBox<String> hospitalBox, departmentBox, doctorBox, dateBox, timeBox, reasonBox;
    private JTextArea noteArea;
    private JTable table;
    private DefaultTableModel model;

    public AppointmentFrame(String role){
        super("預約診所／醫院");
        this.role = role;
        title("預約診所／醫院");
        buildPage();
        bottomNav();
        loadTable();
    }

    private void buildPage(){
        JLabel pageTitle = UIUtil.title("預約診所／醫院");
        pageTitle.setFont(UIUtil.font(Font.BOLD, 24));
        pageTitle.setBounds(8, 0, 260, 34);
        content.add(pageTitle);
        JLabel hint = UIUtil.hint("用下拉選單快速完成看診預約，降低輸入負擔。");
        hint.setBounds(8, 34, 330, 24);
        content.add(hint);

        JPanel form = UIUtil.card();
        form.setBounds(0, 66, 360, 330);
        content.add(form);

        hospitalBox = combo(new String[]{"新店慈濟醫院","耕莘醫院","台大醫院","馬偕醫院","附近診所","社區復健診所"});
        departmentBox = combo(new String[]{"家醫科","內科","復健科","骨科","神經內科","心臟內科","牙科","眼科","耳鼻喉科","中醫"});
        doctorBox = combo(new String[]{"王醫師","陳醫師","林醫師","張醫師","現場掛號","由院方安排"});
        dateBox = combo(nextDates());
        timeBox = combo(new String[]{"上午 08:30","上午 09:00","上午 09:30","上午 10:00","下午 14:00","下午 14:30","下午 15:00","晚上 18:30"});
        reasonBox = combo(new String[]{"定期回診","血壓追蹤","慢性病追蹤","復健治療","用藥諮詢","身體不適","其他"});

        addField(form,"醫院診所",hospitalBox,18,18);
        addField(form,"科別",departmentBox,18,72);
        addField(form,"醫師",doctorBox,18,126);
        addField(form,"日期",dateBox,188,18);
        addField(form,"時間",timeBox,188,72);
        addField(form,"看診原因",reasonBox,188,126);

        JLabel noteLabel = UIUtil.hint("備註");
        noteLabel.setBounds(18,184,100,22);
        form.add(noteLabel);
        noteArea = new JTextArea();
        noteArea.setFont(UIUtil.font(Font.PLAIN, 14));
        noteArea.setLineWrap(true);
        noteArea.setWrapStyleWord(true);
        JScrollPane noteScroll = new JScrollPane(noteArea);
        noteScroll.setBounds(18,208,322,58);
        form.add(noteScroll);

        JButton addBtn = UIUtil.btn("確認預約");
        addBtn.setBounds(18,280,148,38);
        addBtn.addActionListener(e -> addAppointment());
        form.add(addBtn);
        JButton mapBtn = UIUtil.outlineBtn("地圖查詢");
        mapBtn.setBounds(184,280,148,38);
        mapBtn.addActionListener(e -> openMap(String.valueOf(hospitalBox.getSelectedItem())));
        form.add(mapBtn);

        buildTable();
    }

    private JComboBox<String> combo(String[] items){ JComboBox<String> box = new JComboBox<>(items); UIUtil.styleCombo(box); return box; }
    private void addField(JPanel panel, String label, JComboBox<String> box, int x, int y){ JLabel l=UIUtil.hint(label); l.setBounds(x,y,130,22); panel.add(l); box.setBounds(x,y+24,152,36); panel.add(box); }
    private String[] nextDates(){ String[] arr=new String[14]; DateTimeFormatter fmt=DateTimeFormatter.ofPattern("yyyy-MM-dd"); for(int i=0;i<arr.length;i++) arr[i]=LocalDate.now().plusDays(i).format(fmt); return arr; }

    private void buildTable(){
        JPanel list = UIUtil.card();
        list.setBounds(0, 412, 360, 230);
        content.add(list);
        JLabel t = UIUtil.h3Label("我的預約");
        t.setBounds(18,10,140,24);
        list.add(t);
        model = new DefaultTableModel(new String[]{"ID","醫院","科別","醫師","日期","時間","狀態"},0);
        table = new JTable(model);
        UIUtil.styleTable(table);
        table.setRowHeight(30);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(14,42,332,132);
        list.add(sp);
        JButton cancelBtn = UIUtil.outlineBtn("取消預約");
        cancelBtn.setBounds(18,184,140,34);
        cancelBtn.addActionListener(e -> cancelAppointment());
        list.add(cancelBtn);
        JButton refreshBtn = UIUtil.outlineBtn("重新整理");
        refreshBtn.setBounds(184,184,140,34);
        refreshBtn.addActionListener(e -> loadTable());
        list.add(refreshBtn);
    }

    private void addAppointment(){
        try{
            Appointment a = new Appointment();
            a.setMemberId(1);
            a.setHospitalName(String.valueOf(hospitalBox.getSelectedItem()));
            a.setDepartment(String.valueOf(departmentBox.getSelectedItem()));
            a.setDoctorName(String.valueOf(doctorBox.getSelectedItem()));
            a.setAppointmentDate(String.valueOf(dateBox.getSelectedItem()));
            a.setAppointmentTime(String.valueOf(timeBox.getSelectedItem()));
            a.setVisitReason(String.valueOf(reasonBox.getSelectedItem()));
            a.setStatus("已預約");
            a.setNote(noteArea.getText());
            service.add(a);
            UIUtil.info(this,"預約成功，已加入我的預約。\n提醒：看診請攜帶健保卡與慢箋。");
            noteArea.setText("");
            loadTable();
        }catch(Exception ex){ UIUtil.error(this, ex); }
    }

    private void cancelAppointment(){
        int row = table.getSelectedRow();
        if(row < 0){ UIUtil.info(this,"請先選擇要取消的預約"); return; }
        int id = Integer.parseInt(model.getValueAt(row,0).toString());
        int result = JOptionPane.showConfirmDialog(this,"確定要取消這筆預約嗎？","取消確認",JOptionPane.YES_NO_OPTION);
        if(result == JOptionPane.YES_OPTION){ service.cancel(id); loadTable(); UIUtil.info(this,"已取消預約"); }
    }

    private void loadTable(){
        if(model==null) return;
        model.setRowCount(0);
        try{
            for(Appointment a: service.getAll()){
                model.addRow(new Object[]{a.getAppointmentId(),a.getHospitalName(),a.getDepartment(),a.getDoctorName(),a.getAppointmentDate(),a.getAppointmentTime(),a.getStatus()});
            }
        }catch(Exception e){ model.addRow(new Object[]{"-","MySQL 尚未連線","-","-","-","-","-"}); }
    }

    private void openMap(String keyword){
        try{
            String url = "https://www.google.com/maps/search/?api=1&query=" + java.net.URLEncoder.encode(keyword,"UTF-8");
            Desktop.getDesktop().browse(new URI(url));
        }catch(Exception ex){ UIUtil.error(this, ex); }
    }
}
