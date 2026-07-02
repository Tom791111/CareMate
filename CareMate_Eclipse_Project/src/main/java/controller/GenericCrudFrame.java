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
 * 通用 CRUD 頁面 v5.2
 * 修正：表單高度不足造成按鈕/欄位壓縮、JTable 高度不足、報表列印功能。
 */
public class GenericCrudFrame extends BaseFrame {
    private CrudService service;
    private JTextField c1=UIUtil.input(), c2=UIUtil.input(), c3=UIUtil.input(), c4=UIUtil.input(), note=UIUtil.input(), time=UIUtil.input();
    private JTable table=new JTable();
    private DefaultTableModel model;
    private int selectedId=0;
    private String[] ls;
    private String pageTitle;

    public GenericCrudFrame(String role, String title, String[] labels, CrudService service){
        super(title);
        this.role=role;
        this.service=service;
        this.pageTitle=title;
        this.ls=new String[]{labels[0],labels[1],labels[2],labels[3],"備註","時間"};
        title(title);
        time.setText(LocalDateTime.now().toString().substring(0,16));

        JLabel pageIcon = UIUtil.icon(pageIconType(title), 28, UIUtil.PRIMARY);
        pageIcon.setBounds(4, 2, 32, 32);
        content.add(pageIcon);

        JLabel page=UIUtil.title(title);
        page.setFont(UIUtil.font(Font.BOLD,24));
        page.setBounds(42,0,220,34);
        content.add(page);

        JLabel sub=UIUtil.hint("點選近期紀錄可帶回表單，完成新增、修改、刪除。");
        sub.setBounds(8,34,340,22);
        content.add(sub);

        JPanel form=UIUtil.card();
        form.setBounds(0,64,360,324);
        content.add(form);

        int x=18,y=16;
        JTextField[] fs={c1,c2,c3,c4,note,time};
        for(int i=0;i<fs.length;i++){
            JLabel l=UIUtil.hint(ls[i]);
            l.setBounds(x,y,140,20);
            form.add(l);
            fs[i].setBounds(x,y+24,154,38);
            form.add(fs[i]);
            x+=174;
            if(x>200){x=18;y+=78;}
        }

        JButton add=UIUtil.btn("儲存");
        JButton upd=UIUtil.outlineBtn("修改");
        JButton del=UIUtil.dangerBtn("刪除");
        JButton clr=UIUtil.ghostBtn("清除");
        add.setBounds(18,272,78,38);
        upd.setBounds(104,272,78,38);
        del.setBounds(190,272,78,38);
        clr.setBounds(276,272,66,38);
        form.add(add);form.add(upd);form.add(del);form.add(clr);

        JPanel list=UIUtil.card();
        list.setBounds(0,404,360,274);
        content.add(list);

        JLabel listTitle=UIUtil.h3Label("近期紀錄");
        listTitle.setBounds(18,12,120,24);
        list.add(listTitle);

        JButton print = UIUtil.outlineBtn("列印");
        print.setFont(UIUtil.font(Font.BOLD,12));
        print.setBounds(270,10,72,32);
        list.add(print);
        print.setVisible(title.contains("報表") || title.contains("查詢") || title.contains("列印"));

        model=new DefaultTableModel(new String[]{"ID",ls[0],ls[1],ls[2],ls[3],"備註","時間"},0);
        table.setModel(model);
        UIUtil.styleTable(table);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        setColumnWidths();
        JScrollPane sp=new JScrollPane(table);
        sp.setBounds(14,50,332,206);
        list.add(sp);

        add.addActionListener(e->addRecord());
        upd.addActionListener(e->updateRecord());
        del.addActionListener(e->deleteRecord());
        clr.addActionListener(e->clear());
        print.addActionListener(e->printTable());
        table.getSelectionModel().addListSelectionListener(e->selectRow());
        bottomNav();
        loadTable();
    }

    private void setColumnWidths(){
        int[] widths = {42,72,72,72,72,90,132};
        for(int i=0;i<widths.length;i++){
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
    }

    private String pageIconType(String t){
        if(t.contains("健康"))return "health";
        if(t.contains("用藥"))return "medicine";
        if(t.contains("飲食"))return "meal";
        if(t.contains("情緒"))return "mood";
        if(t.contains("SOS"))return "sos";
        if(t.contains("翻譯"))return "translate";
        if(t.contains("聯絡"))return "contact";
        if(t.contains("報表"))return "report";
        if(t.contains("安排")||t.contains("任務"))return "task";
        return "log";
    }

    private CrudRecord read(){
        CrudRecord r=new CrudRecord();
        r.setId(selectedId); r.setElderId(1);
        r.setCol1(c1.getText()); r.setCol2(c2.getText()); r.setCol3(c3.getText()); r.setCol4(c4.getText());
        r.setNote(note.getText()); r.setRecordTime(time.getText());
        return r;
    }
    private void addRecord(){ try{ service.add(read()); UIUtil.info(this,"新增成功"); clear(); loadTable(); }catch(Exception ex){ UIUtil.error(this,ex);} }
    private void updateRecord(){ try{ service.modify(read()); UIUtil.info(this,"修改成功"); clear(); loadTable(); }catch(Exception ex){ UIUtil.error(this,ex);} }
    private void deleteRecord(){ try{ service.remove(selectedId); UIUtil.info(this,"刪除成功"); clear(); loadTable(); }catch(Exception ex){ UIUtil.error(this,ex);} }
    private void loadTable(){ model.setRowCount(0); try{ for(CrudRecord r:service.getAll()) model.addRow(new Object[]{r.getId(),r.getCol1(),r.getCol2(),r.getCol3(),r.getCol4(),r.getNote(),r.getRecordTime()}); }catch(Exception ex){ UIUtil.error(this,ex);} }
    private void selectRow(){ int row=table.getSelectedRow(); if(row>=0){ selectedId=Integer.parseInt(model.getValueAt(row,0).toString()); c1.setText(String.valueOf(model.getValueAt(row,1))); c2.setText(String.valueOf(model.getValueAt(row,2))); c3.setText(String.valueOf(model.getValueAt(row,3))); c4.setText(String.valueOf(model.getValueAt(row,4))); note.setText(String.valueOf(model.getValueAt(row,5))); time.setText(String.valueOf(model.getValueAt(row,6))); } }
    private void clear(){ selectedId=0; c1.setText("");c2.setText("");c3.setText("");c4.setText("");note.setText("");time.setText(LocalDateTime.now().toString().substring(0,16)); }

    private void printTable(){
        try{
            boolean ok = table.print(
                    JTable.PrintMode.FIT_WIDTH,
                    new MessageFormat("CareMate - " + pageTitle),
                    new MessageFormat("第 {0} 頁")
            );
            if(ok) UIUtil.info(this, "列印完成");
        }catch(Exception ex){
            UIUtil.error(this, ex);
        }
    }
}
