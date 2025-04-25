package BPC;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.text.SimpleDateFormat;
import java.util.Date;



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
        
        // Path to the Excel file
    File file = new File("PatientReport.xlsx");

    // Delete the existing file (if any) to ensure overwrite
    if (file.exists()) {
        file.delete();
    }

    // Create a new Excel workbook and sheet
    XSSFWorkbook workbook = new XSSFWorkbook();
    XSSFSheet sheet = workbook.createSheet("Patient Report");

    

    // Add header row
    Row header = sheet.createRow(0);
    header.createCell(0).setCellValue("Physiotherapist (Treatment)");
    header.createCell(1).setCellValue("Patient ID");
    header.createCell(2).setCellValue("Patient Name");
    header.createCell(3).setCellValue("Patient Address");
    header.createCell(4).setCellValue("Patient Phone");
    header.createCell(5).setCellValue("Appointment Time");
    header.createCell(6).setCellValue("Status");

    // Set header style for better readability
    CellStyle headerStyle = workbook.createCellStyle();
    headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
    headerStyle.setAlignment(HorizontalAlignment.CENTER);

    // Apply header style to the header cells
    for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
        header.getCell(i).setCellStyle(headerStyle);
    }

    
    // Use SimpleDateFormat to format the time values
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // Writing the report data to the Excel sheet
    int rowIndex = 1;
    for (Appointment appt : appointments) {
        String physio = appt.physio.name;
        String treatmentName = appt.treatment.getTreatmentName();
        String patientID = String.valueOf(appt.patient.id);
        String patientName = appt.patient.name;
        String patientAddress = appt.patient.address;
        String patientPhone = appt.patient.phone;
         // Format the appointment time
         Date appointmentDate = appt.time; // Assuming appt.time is a Date object
         String formattedTime = dateFormat.format(appointmentDate);
         
        String status = appt.status;

        // Key for the report (Physiotherapist (Treatment))
        String key = physio + " (" + treatmentName + ")";

        // Write physiotherapist and treatment in the first column
        Row row = sheet.createRow(rowIndex++);
        row.createCell(0).setCellValue(key);

        // Write patient details in the subsequent columns
        row.createCell(1).setCellValue(patientID);
        row.createCell(2).setCellValue(patientName);
        row.createCell(3).setCellValue(patientAddress);
        row.createCell(4).setCellValue(patientPhone);
        row.createCell(5).setCellValue(formattedTime);
        row.createCell(6).setCellValue(status);

        // Add the patient details to the report map
        report.computeIfAbsent(key, k -> new ArrayList<>()).add(patientName);

        // Track attended appointments for attendance ranking
        if (status.equals("Attended")) {
            countAttended.put(physio, countAttended.getOrDefault(physio, 0) + 1);
        }
    }

    // Writing the attendance ranking section
    int rankRowStartIndex = rowIndex + 2;  // Adding some space before ranking section
    Row rankHeader = sheet.createRow(rankRowStartIndex);
    rankHeader.createCell(0).setCellValue("Physiotherapist");
    rankHeader.createCell(1).setCellValue("Count");

    // Apply header style to ranking header
    rankHeader.getCell(0).setCellStyle(headerStyle);
    rankHeader.getCell(1).setCellStyle(headerStyle);

    rowIndex = rankRowStartIndex + 1;  // Next row for ranking data

    // Write attendance ranking data to the sheet
    for (Map.Entry<String, Integer> entry : countAttended.entrySet()) {
        Row rankRow = sheet.createRow(rowIndex++);
        rankRow.createCell(0).setCellValue(entry.getKey());
        rankRow.createCell(1).setCellValue(entry.getValue());
    }

    // Adjust column widths for better readability
    for (int i = 0; i < 7; i++) {
        sheet.autoSizeColumn(i);  // Auto-size all columns from 0 to 6
    }

    // Write the Excel file to disk
    try (FileOutputStream fileOut = new FileOutputStream(file)) {
        workbook.write(fileOut);
        workbook.close();
        System.out.println("Report has been written to PatientReport.xlsx");
    } catch (IOException e) {
        e.printStackTrace();
    }           
                    
                
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
