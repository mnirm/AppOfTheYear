package jarno.oussama.com.examenmonitor.FirebaseDB;

import com.google.firebase.database.IgnoreExtraProperties;




@IgnoreExtraProperties
public class Student {
    private int StudentNumber;
    private String CardIdNumber;
    private String FirstName;
    private String LastName;
    private String ProfilePictureUrl;
    public Student() {

    }

    public String getProfilePictureUrl() {return ProfilePictureUrl; }

    public void setProfilePictureUrl(String profilePictureUrl) { ProfilePictureUrl = profilePictureUrl; }

    public int getStudentNumber() {
        return StudentNumber;
    }

    public void setStudentNumber(int studentNumber) {
        StudentNumber = studentNumber;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String name) {
        FirstName = name;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getCardIdNumber() {
        return CardIdNumber;
    }

    public void setCardIdNumber(String cardIdNumber) {
        CardIdNumber = cardIdNumber;
    }

}
