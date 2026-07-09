package controller;
import service.impl.CareTaskServiceImpl;
public class CaregiverProfileFrame extends GenericCrudFrame {
    public CaregiverProfileFrame(String role){
        super(role,"照顧者資料",new String[]{"姓名","國籍","語言","狀態"},new CareTaskServiceImpl());
    }
}
