package model;
public class Member{private int memberId; private String account,password,name,role,phone,email,status;
 public Member(){} public Member(String account,String password,String name,String role){this.account=account;this.password=password;this.name=name;this.role=role;this.status="ACTIVE";}
 public int getMemberId(){return memberId;} public void setMemberId(int v){memberId=v;} public String getAccount(){return account;} public void setAccount(String v){account=v;} public String getPassword(){return password;} public void setPassword(String v){password=v;} public String getName(){return name;} public void setName(String v){name=v;} public String getRole(){return role;} public void setRole(String v){role=v;} public String getPhone(){return phone;} public void setPhone(String v){phone=v;} public String getEmail(){return email;} public void setEmail(String v){email=v;} public String getStatus(){return status;} public void setStatus(String v){status=v;}
}
