package BPC;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class BPCServiceTest {

    BPC bpc;
    Physiotherapist physio1, physio2;
    Patient patient1, patient2;

    @BeforeEach
    public void setUp() {
        bpc = new BPC();

        // Setup Physiotherapists
        List<String> expertise1 = Arrays.asList("Physiotherapy", "Rehabilitation");
        List<String> expertise2 = Arrays.asList("Osteopathy", "Physiotherapy");
        physio1 = new Physiotherapist(1, "Dr. Alice Smith", "123 Main St", "555-1234", expertise1);
        physio2 = new Physiotherapist(2, "Dr. Bob Jones", "456 Elm St", "555-5678", expertise2);
        bpc.addPhysio(physio1);
        bpc.addPhysio(physio2);

        // Setup Patients
        patient1 = new Patient(1, "Patient 1", "Address 1", "555-0001");
        patient2 = new Patient(2, "Patient 2", "Address 2", "555-0002");
        bpc.addPatient(patient1);
        bpc.addPatient(patient2);
    }

    @Test
    public void testAddPatient() {
        bpc.addPatient(new Patient(3, "Patient 3", "Address 3", "555-0003"));
        assertTrue(bpc.patients.containsKey(3), "Patient should be added");
    }

    @Test
    public void testRemovePatient() {
        bpc.removePatient(1);
        assertFalse(bpc.patients.containsKey(1), "Patient should be removed");
    }

    @Test
    public void testBookAppointment() {
        Calendar appointmentTime = Calendar.getInstance();
        appointmentTime.set(2025, Calendar.MAY, 1, 10, 0);
        Date appointmentDate = appointmentTime.getTime();

        Appointment appointment = bpc.bookAppointment(1, "Physiotherapy", 1, appointmentDate);
        assertNotNull(appointment, "Appointment should be booked successfully");
        assertEquals("Physiotherapy", appointment.treatment.getTreatmentName(), "Treatment name should match");
    }

    @Test
    public void testCheckIn() {
        Calendar appointmentTime = Calendar.getInstance();
        appointmentTime.set(2025, Calendar.MAY, 1, 10, 0);
        Date appointmentDate = appointmentTime.getTime();

        Appointment appointment = bpc.bookAppointment(1, "Physiotherapy", 1, appointmentDate);
        bpc.checkIn(1, appointmentDate);
        assertEquals("Attended", appointment.status, "Patient should be marked as attended");
    }

    @Test
    public void testLookupByExpertise() {
        List<String> expertise = Arrays.asList("Physiotherapy");
        Physiotherapist physio = new Physiotherapist(3, "Dr. John Doe", "789 Oak St", "555-9876", expertise);
        bpc.addPhysio(physio);

        // Add an appointment for physio1
        Calendar appointmentTime = Calendar.getInstance();
        appointmentTime.set(2025, Calendar.MAY, 1, 10, 0);
        Date appointmentDate = appointmentTime.getTime();
        bpc.bookAppointment(3, "Physiotherapy", 1, appointmentDate);

        // Now check expertise lookup
        bpc.lookupByExpertise("Physiotherapy");

        // No assertion here, but we visually inspect console output for correctness
    }

    @Test
    public void testLookupByPhysio() {
        Calendar appointmentTime = Calendar.getInstance();
        appointmentTime.set(2025, Calendar.MAY, 1, 10, 0);
        Date appointmentDate = appointmentTime.getTime();
        bpc.bookAppointment(1, "Physiotherapy", 1, appointmentDate);

        // Now lookup by Physio
        bpc.lookupByPhysio("Dr. Alice Smith");

        // No assertion here, but we visually inspect console output for correctness
    }

    @Test
    public void testGenerateReport() {
        // Book some appointments
        Calendar appointmentTime = Calendar.getInstance();
        appointmentTime.set(2025, Calendar.MAY, 1, 10, 0);
        Date appointmentDate = appointmentTime.getTime();
        bpc.bookAppointment(1, "Physiotherapy", 1, appointmentDate);
        bpc.checkIn(1, appointmentDate);

        // Generate report
        bpc.generateReport();

        // No assertion here, but we visually inspect console output for correctness
    }
}
