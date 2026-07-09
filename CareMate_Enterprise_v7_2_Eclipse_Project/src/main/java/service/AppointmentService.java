package service;

import java.util.List;
import model.Appointment;

public interface AppointmentService {
    void add(Appointment appointment);
    List<Appointment> getAll();
    Appointment getNext();
    void cancel(int appointmentId);
    void remove(int appointmentId);
}
