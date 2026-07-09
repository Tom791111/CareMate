package dao.impl;

import dao.AppointmentDao;
import exception.AppException;
import model.Appointment;
import util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDaoImpl implements AppointmentDao {
    @Override
    public void insert(Appointment a) {
        String sql = "INSERT INTO appointment(member_id,hospital_name,department,doctor_name,appointment_date,appointment_time,visit_reason,status,note) VALUES(?,?,?,?,?,?,?,?,?)";
        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1, a.getMemberId());
            ps.setString(2, a.getHospitalName());
            ps.setString(3, a.getDepartment());
            ps.setString(4, a.getDoctorName());
            ps.setString(5, a.getAppointmentDate());
            ps.setString(6, a.getAppointmentTime());
            ps.setString(7, a.getVisitReason());
            ps.setString(8, a.getStatus());
            ps.setString(9, a.getNote());
            ps.executeUpdate();
        }catch(Exception e){ throw new AppException("新增醫療預約失敗", e); }
    }

    @Override
    public List<Appointment> findAll(){
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT * FROM appointment ORDER BY appointment_date DESC, appointment_time DESC, appointment_id DESC";
        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()){
            while(rs.next()) list.add(map(rs));
            return list;
        }catch(Exception e){ throw new AppException("查詢醫療預約失敗", e); }
    }

    @Override
    public Appointment findNext(){
        String sql = "SELECT * FROM appointment WHERE status='已預約' ORDER BY appointment_date ASC, appointment_time ASC LIMIT 1";
        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()){
            return rs.next() ? map(rs) : null;
        }catch(Exception e){ throw new AppException("查詢下一次看診失敗", e); }
    }

    @Override
    public void updateStatus(int appointmentId, String status){
        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement("UPDATE appointment SET status=? WHERE appointment_id=?")){
            ps.setString(1, status);
            ps.setInt(2, appointmentId);
            ps.executeUpdate();
        }catch(Exception e){ throw new AppException("更新預約狀態失敗", e); }
    }

    @Override
    public void delete(int appointmentId){
        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement("DELETE FROM appointment WHERE appointment_id=?")){
            ps.setInt(1, appointmentId);
            ps.executeUpdate();
        }catch(Exception e){ throw new AppException("刪除預約失敗", e); }
    }

    private Appointment map(ResultSet rs) throws SQLException{
        Appointment a = new Appointment();
        a.setAppointmentId(rs.getInt("appointment_id"));
        a.setMemberId(rs.getInt("member_id"));
        a.setHospitalName(rs.getString("hospital_name"));
        a.setDepartment(rs.getString("department"));
        a.setDoctorName(rs.getString("doctor_name"));
        a.setAppointmentDate(rs.getString("appointment_date"));
        a.setAppointmentTime(rs.getString("appointment_time"));
        a.setVisitReason(rs.getString("visit_reason"));
        a.setStatus(rs.getString("status"));
        a.setNote(rs.getString("note"));
        return a;
    }
}
