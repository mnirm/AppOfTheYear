package jarno.oussama.com.examenmonitor.FirebaseDB;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

@IgnoreExtraProperties
public class Exam {
    private String name;
    private String examId;
    private String createdByUid;
    private Long startTime;
    private Long endTime;
    private boolean registrationAfterEndTimeAllowed;

    public Exam() {

    }
    public String getCreatedByUid() {
        return createdByUid;
    }

    public void setCreatedByUid(String createdByUid) {
        this.createdByUid = createdByUid;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public boolean getRegistrationAfterEndTimeAllowed() {
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

