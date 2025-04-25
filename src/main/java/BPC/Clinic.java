package BPC;
import java.util.*;



public class Clinic {
    private List<Physiotherapist> physiotherapists;
    private List<Patient> patients;
    private List<Appointment> appointments;

    public Clinic() {
        physiotherapists = new ArrayList<>();
        patients = new ArrayList<>();
        appointments = new ArrayList<>();
    }

    public void addPhysiotherapist(Physiotherapist physiotherapist) {
        physiotherapists.add(physiotherapist);
    }

    public void addPatient(Patient patient) {
        patients.add(patient);
    }

    public Physiotherapist findPhysiotherapistByName(String name) {
        for (Physiotherapist physiotherapist : physiotherapists) {
            if (physiotherapist.getName().equals(name)) {
                return physiotherapist;
            }
        }
        return null;
    }

    public void bookAppointment(Patient patient, Physiotherapist physiotherapist, Treatment treatment) {
        Appointment appointment = new Appointment(physiotherapist, patient, treatment, null);
        appointments.add(appointment);
        System.out.println("Appointment booked: " + appointment.getAppointmentDetails());
    }

    public void cancelAppointment(Appointment appointment) {
        appointment.cancel();
        System.out.println("Appointment cancelled: " + appointment.getAppointmentDetails());
    }

    public void attendAppointment(Appointment appointment) {
        appointment.attend();
        System.out.println("Appointment attended: " + appointment.getAppointmentDetails());
    }

    public void generateReport() {
        Map<String, Integer> attendedAppointments = new HashMap<>();
        
        // Count attended appointments for each physiotherapist
        for (Appointment appointment : appointments) {
            if (appointment.getStatus().equals("attended")) {
                String physioName = appointment.getAppointmentDetails().split(",")[1].split(":")[1].trim();
                attendedAppointments.put(physioName, attendedAppointments.getOrDefault(physioName, 0) + 1);
            }
        }

        // Sort physiotherapists by attended appointments in descending order
        List<Map.Entry<String, Integer>> list = new ArrayList<>(attendedAppointments.entrySet());
        list.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        // Print report
        System.out.println("Physiotherapists ranked by attended appointments:");
        for (Map.Entry<String, Integer> entry : list) {
            System.out.println("Physiotherapist: " + entry.getKey() + " - " + entry.getValue() + " attended appointments.");
        }
    }
}
