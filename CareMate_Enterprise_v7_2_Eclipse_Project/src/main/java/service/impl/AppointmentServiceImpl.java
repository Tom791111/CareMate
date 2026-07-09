package service.impl;

import dao.AppointmentDao;
import dao.impl.AppointmentDaoImpl;
import exception.ValidationException;
import model.Appointment;
import service.AppointmentService;
import java.util.List;

public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentDao dao = new AppointmentDaoImpl();

    @Override
    public void add(Appointment a){
        if(a.getHospitalName()==null || a.getHospitalName().trim().isEmpty()) throw new ValidationException("請選擇醫院或診所");
        if(a.getAppointmentDate()==null || a.getAppointmentDate().trim().isEmpty()) throw new ValidationException("請選擇預約日期");
        if(a.getAppointmentTime()==null || a.getAppointmentTime().trim().isEmpty()) throw new ValidationException("請選擇預約時間");
        dao.insert(a);
    }
    @Override public List<Appointment> getAll(){ return dao.findAll(); }
    @Override public Appointment getNext(){ return dao.findNext(); }
    @Override public void cancel(int appointmentId){ dao.updateStatus(appointmentId, "已取消"); }
    @Override public void remove(int appointmentId){ dao.delete(appointmentId); }
}
