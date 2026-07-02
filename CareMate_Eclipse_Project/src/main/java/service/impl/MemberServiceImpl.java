package service.impl;

import service.MemberService;
import dao.MemberDao;
import dao.impl.MemberDaoImpl;
import model.Member;
import exception.ValidationException;
import java.util.*;

public class MemberServiceImpl implements MemberService {
    private MemberDao dao = new MemberDaoImpl();

    public void register(Member m) {
        m.setAccount(clean(m.getAccount()));
        m.setPassword(clean(m.getPassword()));
        m.setName(clean(m.getName()));
        m.setRole(clean(m.getRole()));
        m.setPhone(clean(m.getPhone()));
        m.setEmail(clean(m.getEmail()));
        if (empty(m.getAccount()) || empty(m.getPassword()) || empty(m.getRole())) throw new ValidationException("帳號、密碼、角色不可空白");
        if (m.getAccount().length() < 2) throw new ValidationException("帳號至少需要 2 個字元");
        if (m.getPassword().length() < 2) throw new ValidationException("密碼至少需要 2 個字元");
        dao.insert(m);
    }

    public Member login(String a, String p, String role) {
        a = clean(a); p = clean(p); role = clean(role);
        if (empty(a) || empty(p)) throw new ValidationException("請輸入帳號與密碼");
        Member m = dao.findByAccountAndPassword(a, p);
        if (m == null) throw new ValidationException("登入失敗：帳號或密碼錯誤，或此帳號尚未註冊。");
        if (!empty(role) && !role.equals(m.getRole())) throw new ValidationException("登入角色不符合：此帳號角色是「" + roleText(m.getRole()) + "」，請回身分頁重新選擇。");
        return m;
    }

    public List<Member> getAll() { return dao.findAll(); }
    private boolean empty(String s) { return s == null || s.trim().isEmpty(); }
    private String clean(String s) { return s == null ? "" : s.trim(); }
    private String roleText(String r) {
        if ("FAMILY".equals(r)) return "家屬/照顧者";
        if ("ELDER".equals(r)) return "被照顧者";
        if ("FOREIGN_CAREGIVER".equals(r)) return "外籍照顧者";
        if ("ADMIN".equals(r)) return "管理者";
        return r;
    }
}
