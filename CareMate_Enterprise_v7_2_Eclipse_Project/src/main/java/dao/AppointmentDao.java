package dao;

import java.util.List;
import model.Appointment;

public interface AppointmentDao {
    void insert(Appointment appointment);
    List<Appointment> findAll();
    Appointment findNext();
    void updateStatus(int appointmentId, String status);
    void delete(int appointmentId);
}
