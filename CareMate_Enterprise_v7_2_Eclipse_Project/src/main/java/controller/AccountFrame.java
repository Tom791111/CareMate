package controller;
import service.impl.CareTaskServiceImpl;
public class AccountFrame extends GenericCrudFrame {
    public AccountFrame(String role){
        super(role,"我的帳戶",new String[]{"帳號","角色","通知偏好","狀態"},new CareTaskServiceImpl());
    }
}
