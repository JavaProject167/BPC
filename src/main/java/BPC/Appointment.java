package BPC;
import java.sql.Date;
public class Appointment {
    Physiotherapist physio;
    Patient patient;
    Treatment treatment;
    String status;
    Date time;

    public Appointment(Physiotherapist physio, Patient patient, Treatment treatment, java.util.Date time) {
        this.physio = physio;
        this.patient = patient;
        this.treatment = treatment;
        this.time = new Date(time.getTime());  // convert java.util.Date to java.sql.Date
        this.status = "Booked";
    }

    public String getAppointmentDetails() {
        return "Appointment with " + physio.name + " for " + treatment.getTreatmentName() +
               " at " + time + " | Status: " + status;
    }

    public void attend() {
        this.status = "Attended";
    }

    public void cancel() {
        this.status = "Cancelled";
    }

    public String getStatus() {
        return status;
    }
}
