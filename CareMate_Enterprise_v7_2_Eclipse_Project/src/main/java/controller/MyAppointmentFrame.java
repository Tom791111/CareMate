package controller;

import model.Appointment;
import service.AppointmentService;
import service.impl.AppointmentServiceImpl;
import util.UIUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.net.URI;

public class MyAppointmentFrame extends BaseFrame {
    private final AppointmentService service = new AppointmentServiceImpl();
    private JTable table;
    private DefaultTableModel model;

    public MyAppointmentFrame(String role){
        super("我的預約");
        this.role = role;
        title("我的預約");
        buildPage();
        bottomNav();
        loadTable();
    }

    private void buildPage(){
        JLabel pageTitle = UIUtil.title("我的預約");
        pageTitle.setFont(UIUtil.font(Font.BOLD, 24));
        pageTitle.setBounds(8, 0, 260, 34);
        content.add(pageTitle);
        JLabel hint = UIUtil.hint("查看、取消、刪除預約，或直接開啟地圖導航。");
        hint.setBounds(8, 34, 330, 24);
        content.add(hint);

        JPanel card = UIUtil.card();
        card.setBounds(0, 72, 360, 430);
        content.add(card);
        model = new DefaultTableModel(new String[]{"ID","醫院","科別","醫師","日期","時間","狀態"},0);
        table = new JTable(model);
        UIUtil.styleTable(table);
        table.setRowHeight(32);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(14,18,332,250);
        card.add(sp);

        JButton mapBtn = UIUtil.btn("地圖導航");
        mapBtn.setBounds(18,286,140,38);
        mapBtn.addActionListener(e -> openSelectedMap());
        card.add(mapBtn);
        JButton cancelBtn = UIUtil.outlineBtn("取消預約");
        cancelBtn.setBounds(184,286,140,38);
        cancelBtn.addActionListener(e -> cancelSelected());
        card.add(cancelBtn);
        JButton deleteBtn = UIUtil.outlineBtn("刪除紀錄");
        deleteBtn.setBounds(18,340,140,38);
        deleteBtn.addActionListener(e -> deleteSelected());
        card.add(deleteBtn);
        JButton refreshBtn = UIUtil.outlineBtn("重新整理");
        refreshBtn.setBounds(184,340,140,38);
        refreshBtn.addActionListener(e -> loadTable());
        card.add(refreshBtn);

        JButton addBtn = UIUtil.btn("新增預約");
        addBtn.setBounds(18,522,306,42);
        addBtn.addActionListener(e -> open("AppointmentFrame"));
        content.add(addBtn);
    }

    private void loadTable(){
        model.setRowCount(0);
        try{
            for(Appointment a: service.getAll()){
                model.addRow(new Object[]{a.getAppointmentId(),a.getHospitalName(),a.getDepartment(),a.getDoctorName(),a.getAppointmentDate(),a.getAppointmentTime(),a.getStatus()});
            }
        }catch(Exception e){ model.addRow(new Object[]{"-","MySQL 尚未連線","-","-","-","-","-"}); }
    }

    private int selectedId(){
        int row = table.getSelectedRow();
        if(row < 0){ UIUtil.info(this,"請先選擇一筆預約資料"); return -1; }
        try{ return Integer.parseInt(model.getValueAt(row,0).toString()); }
        catch(Exception e){ UIUtil.info(this,"此筆資料無法操作"); return -1; }
    }
    private String selectedHospital(){
        int row = table.getSelectedRow();
        if(row < 0){ UIUtil.info(this,"請先選擇一筆預約資料"); return null; }
        return String.valueOf(model.getValueAt(row,1));
    }
    private void cancelSelected(){
        int id=selectedId(); if(id<0) return;
        int result=JOptionPane.showConfirmDialog(this,"確定要取消這筆預約嗎？","取消預約",JOptionPane.YES_NO_OPTION);
        if(result==JOptionPane.YES_OPTION){ service.cancel(id); UIUtil.info(this,"已取消預約"); loadTable(); }
    }
    private void deleteSelected(){
        int id=selectedId(); if(id<0) return;
        int result=JOptionPane.showConfirmDialog(this,"確定要刪除此筆預約紀錄嗎？\n刪除後無法復原。","刪除確認",JOptionPane.YES_NO_OPTION);
        if(result==JOptionPane.YES_OPTION){ service.remove(id); UIUtil.info(this,"已刪除預約紀錄"); loadTable(); }
    }
    private void openSelectedMap(){
        String hospital=selectedHospital(); if(hospital==null||hospital.trim().isEmpty()) return;
        try{
            String url="https://www.google.com/maps/search/?api=1&query="+java.net.URLEncoder.encode(hospital,"UTF-8");
            Desktop.getDesktop().browse(new URI(url));
        }catch(Exception e){ UIUtil.error(this,e); }
    }
}
