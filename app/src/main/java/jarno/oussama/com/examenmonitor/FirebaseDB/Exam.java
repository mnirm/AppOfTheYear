package jarno.oussama.com.examenmonitor.FirebaseDB;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

@IgnoreExtraProperties
public class Exam {

    String name;
    boolean registrationAfterEndTimeAllowed;
    Long startTime;
    Long endTime;

    public Exam() {

    }
    //Date date;

    public boolean isRegistrationAfterEndTimeAllowed() {
        return registrationAfterEndTimeAllowed;
    }

    public void setRegistrationAfterEndTimeAllowed(boolean registrationAfterEndTimeAllowed) {
        this.registrationAfterEndTimeAllowed = registrationAfterEndTimeAllowed;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }
}

