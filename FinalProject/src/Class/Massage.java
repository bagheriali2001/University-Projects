package Class;

import java.time.LocalDate;

public class Massage {
    private int massageID;
    private String from;
    private String to;
    private LocalDate time;
    private String content;

    public Massage(int massageID, String from, String to, LocalDate time, String content) {
        this.massageID = massageID;
        this.from = from;
        this.to = to;
        this.time = time;
        this.content = content;
    }

    public int getMassageID() {
        return massageID;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public LocalDate getTime() {
        return time;
    }

    public String getContent() {
        return content;
    }
}
