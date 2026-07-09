package service;import model.Member;import java.util.*; public interface MemberService{void register(Member m); Member login(String account,String password,String role); List<Member> getAll();}
