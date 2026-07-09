package controller;
import service.impl.CareTaskServiceImpl;
public class ElderProfileFrame extends GenericCrudFrame {
    public ElderProfileFrame(String role){
        super(role,"被照顧者資料",new String[]{"姓名","性別","年齡","照護狀態"},new CareTaskServiceImpl());
    }
}
