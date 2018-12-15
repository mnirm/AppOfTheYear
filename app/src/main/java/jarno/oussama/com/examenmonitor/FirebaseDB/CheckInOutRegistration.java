package jarno.oussama.com.examenmonitor.FirebaseDB;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

@IgnoreExtraProperties
public class CheckInOutRegistration {

    String cardId;
    Long timeStamp;
    String examId;
    public String getCheckInOutRegistrationId() {
        return cardId+ "_" + examId;
    }
    public String getExamId() {
        return examId;
    }
    public void setExamId(String examId) {
        this.examId = examId;
    }
    public Long getTimeStamp() {
        return timeStamp;
    }
    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }
    public String getCardId() {
        return cardId;
    }
    public void setCardId(String cardId) {
        this.cardId = cardId;
    }
}
