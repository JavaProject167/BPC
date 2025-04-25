package BPC;
import java.util.*;


class Person {
    int id;
    String name;
    String address;
    String phone;

    public Person(int id, String name, String address, String phone) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
    }
}


class Physiotherapist extends Person {
    List<String> expertise;
    Map<String, List<Appointment>> treatments;  // treatment_name -> list of appointments

    public Physiotherapist(int id, String name, String address, String phone, List<String> expertise) {
        super(id, name, address, phone);
        this.expertise = expertise;
        this.treatments = new HashMap<>();
    }

    public Object getName() {
       
        throw new UnsupportedOperationException("Unimplemented method 'getName'");
    }
}

class Patient extends Person {
    public Patient(int id, String name, String address, String phone) {
        super(id, name, address, phone);
    }
}


class BPC {
    Map<Integer, Physiotherapist> physiotherapists = new HashMap<>();
    Map<Integer, Patient> patients = new HashMap<>();
    List<Appointment> appointments = new ArrayList<>();
    public void addPatient(Patient patient) {
        patients.put(patient.id, patient);
    }

    public void removePatient(int patientId) {
        patients.remove(patientId);
    }

    public void addPhysio(Physiotherapist physio) {
        physiotherapists.put(physio.id, physio);
    }

    public Appointment bookAppointment(int physioId, String treatmentName, int patientId, Date time) {
        // Find the Physiotherapist and Patient by their IDs
        Physiotherapist physio = physiotherapists.get(physioId);
        Patient patient = patients.get(patientId);
        

        // Check if the patient already has an appointment for the same treatment at the same time
    for (Appointment appt : appointments) {
        if (appt.patient.id == patientId && appt.treatment.getTreatmentName().equals(treatmentName) && appt.time.equals(time)) {
            
            return null;  // Exit if duplicate appointment is found
        }
    }
    
        // Create a new Treatment object (use treatmentName and time as inputs)
        Treatment treatmentObj = new Treatment(treatmentName, time);  // Pass treatmentName and time to create Treatment object
    
        // Create a new Appointment object using the correct constructor
        Appointment appointment = new Appointment(physio, patient, treatmentObj, time);  // Use correct constructor
    
        // Add the appointment to the appointments list
        appointments.add(appointment);
    
        // Add the appointment to the physiotherapist's treatments map
        physio.treatments.computeIfAbsent(treatmentName, k -> new ArrayList<>())
             .add(appointment);

        
             
    
        return appointment;
    }
    
    
    
    
    
    
    
    public void checkIn(int patientId, Date time) {
        for (Appointment appt : appointments) {
            if (appt.patient.id == patientId && appt.treatment.getDateTime().equals(time)) {
                appt.status = "Attended";
                break;
            }
        }
    }
    
    public void lookupByExpertise(String area) {
        for (Physiotherapist physio : physiotherapists.values()) {
            // Check if physiotherapist expertise matches the area
            if (physio.expertise.contains(area)) {
                System.out.println("Physiotherapist: " + physio.name);
                
                // Iterate over treatments only for the specified area
                for (Map.Entry<String, List<Appointment>> entry : physio.treatments.entrySet()) {
                    String treatmentType = entry.getKey(); // Get the treatment type (e.g., Physiotherapy, Rehabilitation)
                    
                    // Only show treatments that match the specified area
                    if (treatmentType.equalsIgnoreCase(area)) {
                        for (Appointment appt : entry.getValue()) {
                            if (appt.status.equals("Booked")) {
                                System.out.println("  Treatment: " + appt.treatment.getTreatmentName() + ", Time: " + appt.treatment.getDateTime());
                            }
                        }
                    }
                }
            }
        }
    }
    
    
    public void lookupByPhysio(String physioName) {
        for (Physiotherapist physio : physiotherapists.values()) {
            if (physio.name.equalsIgnoreCase(physioName)) {
                System.out.println("Physiotherapist: " + physio.name);
                for (Map.Entry<String, List<Appointment>> entry : physio.treatments.entrySet()) {
                    for (Appointment appt : entry.getValue()) {
                        if (appt.status.equals("Booked")) {
                            System.out.println("  Treatment: " + appt.treatment.getTreatmentName() + ", Time: " + appt.treatment.getDateTime());
                        }
                    }
                }
            }
        }
    }
    

    public void generateReport() {
        Map<String, List<String>> report = new HashMap<>();
        Map<String, Integer> countAttended = new HashMap<>();

        for (Appointment appt : appointments) {
            String physio = appt.physio.name;
            String patient = appt.patient.name;
            String key = physio + " (" + appt.treatment.getTreatmentName() + ")";

          
            String patientInfo = "ID: " + appt.patient.id + ", Name: " + appt.patient.name + ", Address: " + appt.patient.address + ", Phone: " + appt.patient.phone;
            report.computeIfAbsent(key, k -> new ArrayList<>()).add(patientInfo + ", Time: " + appt.time + ", Status: " + appt.status);


            if (appt.status.equals("Attended")) {
                countAttended.put(physio, countAttended.getOrDefault(physio, 0) + 1);
            }
        }

        for (Map.Entry<String, List<String>> entry : report.entrySet()) {
            System.out.println(entry.getKey() + ": ");
            for (String line : entry.getValue()) {
                System.out.println("  " + line);
            }
        }

        System.out.println("\nPhysiotherapist Attendance Ranking:");
        countAttended.entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
                .forEach(e -> System.out.println("  " + e.getKey() + ": " + e.getValue() + " attended appointments"));
    }
}

public class Main {
    public static void main(String[] args) {
        BPC bpc = new BPC();

        // Add Physiotherapists
        List<String> expertise1 = Arrays.asList("Physiotherapy", "Rehabilitation");
        List<String> expertise2 = Arrays.asList("Osteopathy", "Physiotherapy");
        List<String> expertise3 = Arrays.asList("Rehabilitation", "Sports Therapy");

        Physiotherapist physio1 = new Physiotherapist(1, "Dr. Alice Smith", "123 Main St", "555-1234", expertise1);
        Physiotherapist physio2 = new Physiotherapist(2, "Dr. Bob Jones", "456 Elm St", "555-5678", expertise2);
        Physiotherapist physio3 = new Physiotherapist(3, "Dr. Carol White", "789 Oak St", "555-9012", expertise3);

        bpc.addPhysio(physio1);
        bpc.addPhysio(physio2);
        bpc.addPhysio(physio3);

         // Add Patients
         for (int i = 1; i <= 15; i++) {
            Patient patient = new Patient(i, "Patient " + i, "Address " + i, "555-00" + i);
            bpc.addPatient(patient);
        }

        // Create a 4-week timetable with exactly two treatments for each doctor
        Calendar calendar = Calendar.getInstance();
        for (Physiotherapist physio : Arrays.asList(physio1, physio2, physio3)) {
            for (int i = 0; i < 4; i++) {  // 4 weeks
                for (int j = 0; j < 5; j++) {  // slots per day
                    calendar.set(2025, Calendar.MAY, 1 + i * 7, 10 + j*3, 0);  // Example times
                    Date date = calendar.getTime();

                    // Assign one of the two treatments from the physiotherapist's expertise
                    String treatment = physio.expertise.get(j % 2);  // This ensures exactly two treatments
                    int patientId = (int)(Math.random() * 15) + 1;  // Random patient between 1 and 15
                    bpc.bookAppointment(physio.id, treatment, patientId, date);
                }
            }
        }

       

        // Lookup example
        System.out.println("Look By Expertise: Physiotherapy");
        bpc.lookupByExpertise("Physiotherapy");

        System.out.println("Look By Expertise: Physio");
        bpc.lookupByPhysio("Dr. Alice Smith");

        // Book an appointment example
        Calendar appointmentTime = Calendar.getInstance();
        appointmentTime.set(2025, Calendar.MAY, 1, 10, 0);
        Date appointmentDate = appointmentTime.getTime();
        bpc.bookAppointment(1, "Massage", 1, appointmentDate);

        // Check-in example
        bpc.checkIn(1, appointmentDate);

        // Randomly mark some as Attended or Cancelled
        for (Appointment appt : bpc.appointments) {
            double chance = Math.random();
            if (chance < 0.3) {
                appt.attend();
            } else if (chance < 0.5) {
                appt.cancel();
            }
        }

        System.out.println("Report:");
        // Generate Report
        bpc.generateReport();
    }
}
