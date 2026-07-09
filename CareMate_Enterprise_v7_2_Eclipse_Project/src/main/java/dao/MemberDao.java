package dao;import model.Member;import java.util.*;
public interface MemberDao{void insert(Member m); List<Member> findAll(); Member findById(int id); Member findByAccountAndPassword(String account,String password); void update(Member m); void delete(int id);}
