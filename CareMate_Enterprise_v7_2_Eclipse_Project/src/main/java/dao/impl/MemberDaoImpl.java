package dao.impl;

import dao.MemberDao;
import model.Member;
import util.DBUtil;
import exception.AppException;
import java.sql.*;
import java.util.*;

public class MemberDaoImpl implements MemberDao {
    public void insert(Member m) {
        String sql = "INSERT INTO member(account,password,name,role,phone,email,status) VALUES(?,?,?,?,?,?,?)";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, trim(m.getAccount()));
            ps.setString(2, trim(m.getPassword()));
            ps.setString(3, trim(m.getName()));
            ps.setString(4, trim(m.getRole()));
            ps.setString(5, trim(m.getPhone()));
            ps.setString(6, trim(m.getEmail()));
            ps.setString(7, m.getStatus() == null ? "ACTIVE" : trim(m.getStatus()));
            ps.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new AppException("新增會員失敗：此帳號已存在，請換一個帳號。", e);
        } catch (Exception e) {
            throw new AppException("新增會員失敗：" + rootMessage(e), e);
        }
    }

    public List<Member> findAll() {
        List<Member> list = new ArrayList<>();
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT * FROM member ORDER BY member_id DESC"); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
            return list;
        } catch (Exception e) { throw new AppException("查詢會員清單失敗：" + rootMessage(e), e); }
    }

    public Member findById(int id) {
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT * FROM member WHERE member_id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? map(rs) : null; }
        } catch (Exception e) { throw new AppException("查詢會員失敗：" + rootMessage(e), e); }
    }

    public Member findByAccountAndPassword(String a, String p) {
        String sql = "SELECT * FROM member WHERE TRIM(account)=TRIM(?) AND TRIM(password)=TRIM(?) AND IFNULL(status,'ACTIVE')='ACTIVE'";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, a);
            ps.setString(2, p);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? map(rs) : null; }
        } catch (Exception e) { throw new AppException("登入查詢失敗：" + rootMessage(e), e); }
    }

    public void update(Member m) {
        String sql = "UPDATE member SET password=?,name=?,role=?,phone=?,email=?,status=? WHERE member_id=?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, trim(m.getPassword())); ps.setString(2, trim(m.getName())); ps.setString(3, trim(m.getRole()));
            ps.setString(4, trim(m.getPhone())); ps.setString(5, trim(m.getEmail())); ps.setString(6, trim(m.getStatus())); ps.setInt(7, m.getMemberId());
            ps.executeUpdate();
        } catch (Exception e) { throw new AppException("修改會員失敗：" + rootMessage(e), e); }
    }

    public void delete(int id) {
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement("DELETE FROM member WHERE member_id=?")) {
            ps.setInt(1, id); ps.executeUpdate();
        } catch (Exception e) { throw new AppException("刪除會員失敗：" + rootMessage(e), e); }
    }

    private Member map(ResultSet rs) throws SQLException {
        Member m = new Member();
        m.setMemberId(rs.getInt("member_id")); m.setAccount(rs.getString("account")); m.setPassword(rs.getString("password"));
        m.setName(rs.getString("name")); m.setRole(rs.getString("role")); m.setPhone(rs.getString("phone")); m.setEmail(rs.getString("email")); m.setStatus(rs.getString("status"));
        return m;
    }

    private String trim(String s) { return s == null ? null : s.trim(); }
    private String rootMessage(Throwable e) { Throwable t=e; while(t.getCause()!=null) t=t.getCause(); return t.getMessage()==null?e.getMessage():t.getMessage(); }
}
