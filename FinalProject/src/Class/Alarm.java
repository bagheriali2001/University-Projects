package Class;

public class Alarm {
    private int alarmID;
    private int hour;
    private int minute;
    private int delayHour;
    private String title;
    private String alarmOf;
    private boolean isActive = true;
    private boolean isUsed ;

    public Alarm(int alarmID, int hour, int minute, int delayHour, String title, String alarmOf, boolean isUsed) {
        this.alarmID = alarmID;
        this.hour = hour;
        this.minute = minute;
        this.delayHour = delayHour;
        this.alarmOf = alarmOf;
        this.title = title;
        this.isUsed = isUsed;
    }

    public int getAlarmID() {
        return alarmID;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getDelayHour() {
        return delayHour;
    }

    public String getTitle() {
        return title;
    }

    public String getAlarmOf() {
        return alarmOf;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void used() {
        isUsed = true;
    }

    public void notUsed() {
        isUsed = false;
    }

    public void next(){
        hour+=delayHour;
        if (hour>=24){
            hour-=24;
        }
    }
}
