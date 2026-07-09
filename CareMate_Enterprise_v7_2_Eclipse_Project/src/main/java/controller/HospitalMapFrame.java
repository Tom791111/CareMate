package controller;

import util.UIUtil;
import util.DBUtil;
import exception.AppException;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HospitalMapFrame extends BaseFrame {
    private JComboBox<String> areaBox, typeBox, distanceBox;
    private JPanel listPanel;

    public HospitalMapFrame(String role){
        super("附近醫療院所");
        this.role = role;
        title("附近醫療院所");
        buildPage();
        bottomNav();
        loadHospitals();
    }

    private void buildPage(){
        JLabel pageTitle = UIUtil.title("附近醫療院所");
        pageTitle.setFont(UIUtil.font(Font.BOLD, 24));
        pageTitle.setBounds(8,0,260,34);
        content.add(pageTitle);
        JLabel hint = UIUtil.hint("下拉選擇地區與類型，直接開啟 Google Maps 導航。");
        hint.setBounds(8,34,340,24);
        content.add(hint);

        JPanel search = UIUtil.card();
        search.setBounds(0,66,360,108);
        content.add(search);
        areaBox = combo(new String[]{"新北市新店區","新北市永和區","新北市中和區","台北市文山區","台北市大安區"});
        typeBox = combo(new String[]{"全部","醫院","診所","藥局","復健中心","中醫","牙醫","眼科","耳鼻喉科","心理諮商"});
        distanceBox = combo(new String[]{"1 公里","3 公里","5 公里","10 公里"});
        addField(search,"地區",areaBox,14,12,144);
        addField(search,"類型",typeBox,178,12,154);
        addField(search,"距離",distanceBox,14,62,144);
        JButton searchBtn = UIUtil.btn("搜尋");
        searchBtn.setFont(UIUtil.font(Font.BOLD, 13));
        searchBtn.setBounds(178,64,154,34);
        searchBtn.addActionListener(e -> loadHospitals());
        search.add(searchBtn);

        listPanel = new JPanel(null);
        listPanel.setOpaque(false);
        listPanel.setPreferredSize(new Dimension(340, 620));
        JScrollPane sp = new JScrollPane(listPanel);
        sp.setBorder(null);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        sp.setBounds(0,190,360,500);
        content.add(sp);
    }

    private JComboBox<String> combo(String[] items){ JComboBox<String> box = new JComboBox<>(items); UIUtil.styleCombo(box); return box; }
    private void addField(JPanel panel, String label, JComboBox<String> box, int x, int y, int w){ JLabel l=UIUtil.hint(label); l.setBounds(x,y,w,18); panel.add(l); box.setBounds(x,y+20,w,34); panel.add(box); }

    private void loadHospitals(){
        listPanel.removeAll();
        List<Hospital> hospitals = demoHospitals(String.valueOf(areaBox.getSelectedItem()), String.valueOf(typeBox.getSelectedItem()));
        int y = 0;
        for(Hospital h: hospitals){ addHospitalCard(h, y); y += 136; }
        listPanel.setPreferredSize(new Dimension(340, Math.max(520, y + 20)));
        listPanel.revalidate();
        listPanel.repaint();
    }

    private void addHospitalCard(Hospital h, int y){
        JPanel card = UIUtil.card();
        card.setBounds(0,y,340,126);
        listPanel.add(card);
        JLabel icon = UIUtil.icon("health", 26, UIUtil.PRIMARY);
        icon.setBounds(14,14,30,30);
        card.add(icon);
        JLabel name = UIUtil.h3Label(h.name);
        name.setBounds(54,10,200,24);
        card.add(name);
        JLabel meta = UIUtil.hint(h.type + "｜★★★★★｜" + h.distance);
        meta.setBounds(54,34,230,20);
        card.add(meta);
        JLabel address = UIUtil.hint("📍 " + h.address);
        address.setBounds(18,62,300,20);
        card.add(address);
        JLabel phone = UIUtil.hint("☎ " + h.phone);
        phone.setBounds(18,84,140,20);
        card.add(phone);
        JButton map = UIUtil.btn("導航");
        map.setFont(UIUtil.font(Font.BOLD, 12));
        map.setBounds(178,82,70,30);
        map.addActionListener(e -> openMap(h.name));
        card.add(map);
        JButton fav = UIUtil.outlineBtn("收藏");
        fav.setFont(UIUtil.font(Font.BOLD, 12));
        fav.setBounds(258,82,70,30);
        fav.addActionListener(e -> saveFavorite(h));
        card.add(fav);
    }

    private List<Hospital> demoHospitals(String area, String type){
        List<Hospital> list = new ArrayList<>();
        list.add(new Hospital("新店慈濟醫院","醫院","新北市新店區建國路289號","02-6628-9779","2.1 km"));
        list.add(new Hospital("耕莘醫院","醫院","新北市新店區中正路362號","02-2219-3391","3.4 km"));
        list.add(new Hospital("北新診所","診所","新北市新店區北新路","02-2912-0000","0.8 km"));
        list.add(new Hospital("新店復健中心","復健中心","新北市新店區中興路","02-2918-0000","1.2 km"));
        list.add(new Hospital("大坪林藥局","藥局","新北市新店區民權路","02-8911-0000","0.5 km"));
        if(type==null || "全部".equals(type)) return list;
        List<Hospital> filtered = new ArrayList<>();
        for(Hospital h:list) if(h.type.contains(type)) filtered.add(h);
        return filtered.isEmpty() ? list : filtered;
    }

    private void openMap(String keyword){
        try{
            String url = "https://www.google.com/maps/search/?api=1&query=" + java.net.URLEncoder.encode(keyword,"UTF-8");
            Desktop.getDesktop().browse(new URI(url));
        }catch(Exception e){ UIUtil.error(this, e); }
    }

    private void saveFavorite(Hospital h){
        String sql = "INSERT INTO favorite_hospital(member_id,hospital_name,type,address,phone,map_keyword) VALUES(1,?,?,?,?,?)";
        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)){
            ps.setString(1, h.name);
            ps.setString(2, h.type);
            ps.setString(3, h.address);
            ps.setString(4, h.phone);
            ps.setString(5, h.name);
            ps.executeUpdate();
            UIUtil.info(this, "已加入常用醫院：" + h.name);
        }catch(Exception ex){ throw new AppException("收藏醫院失敗", ex); }
    }

    private static class Hospital{
        String name,type,address,phone,distance;
        Hospital(String name,String type,String address,String phone,String distance){this.name=name;this.type=type;this.address=address;this.phone=phone;this.distance=distance;}
    }
}
