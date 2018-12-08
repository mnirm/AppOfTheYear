package jarno.oussama.com.examenmonitor.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(indices = {@Index(value = "card_id", unique = true)})
public class Student {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "student_number")
    private String StudentNumber;

    @NonNull
    @ColumnInfo(name = "card_id")
    private String CardIdNumber;

    @NonNull
    @ColumnInfo(name = "first_name")
    private String FirstName;

    @NonNull
    @ColumnInfo(name = "last_name")
    private String LastName;



    public Student() {

    }


    public String getStudentNumber() {
        return StudentNumber;
    }

    public void setStudentNumber(String studentNumber) {
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
