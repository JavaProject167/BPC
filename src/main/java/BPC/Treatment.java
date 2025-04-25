package BPC;
import java.sql.Date;

class Treatment {
    private String treatmentName;
    private Date dateTime;

    public Treatment(String treatmentName, java.util.Date time) {
        this.treatmentName = treatmentName;
        this.dateTime = new java.sql.Date(time.getTime());
    }
    

    public Treatment(String treatmentName2, String dateTime2) {
        //TODO Auto-generated constructor stub
    }


    public String getTreatmentName() {
        return treatmentName;
    }

    public Date getDateTime() {
        return dateTime;
    }
}