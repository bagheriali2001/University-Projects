package Class;


import java.time.LocalDate;

public class Appointment {
    //Acception
    //1 = yes
    //0 = didnt get answer
    //-1 = no


    private int appointmentsID;
    private String from;
    private String to;
    private String content;
    private LocalDate when;
    private LocalDate timeRequested;
    private int acceptation;
    private String doctorResponse;
    private String filePath;

    public Appointment(int appointmentsID, String from, String to, String content
            , LocalDate when, LocalDate timeRequested, int acceptation, String doctorResponse, String filePath) {
        this.appointmentsID = appointmentsID;
        this.from = from;
        this.to = to;
        this.content = content;
        this.when = when;
        this.timeRequested = timeRequested;
        this.acceptation = acceptation;
        this.doctorResponse = doctorResponse;
        this.filePath = filePath;
    }
    public Appointment(int appointmentsID, String from, String to, String content
            , LocalDate when, LocalDate timeRequested, int acceptation, String doctorResponse) {
        this.appointmentsID = appointmentsID;
        this.from = from;
        this.to = to;
        this.content = content;
        this.when = when;
        this.timeRequested = timeRequested;
        this.acceptation = acceptation;
        this.doctorResponse = doctorResponse;
    }

    public int getAppointmentsID() {
        return appointmentsID;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getContent() {
        return content;
    }

    public LocalDate getWhen() {
        return when;
    }

    public LocalDate getTimeRequested() {
        return timeRequested;
    }

    public int getAcceptation() {
        return acceptation;
    }

    public String getDoctorResponse() {
        return doctorResponse;
    }

    public String getFilePath() {
        return filePath;
    }
}
