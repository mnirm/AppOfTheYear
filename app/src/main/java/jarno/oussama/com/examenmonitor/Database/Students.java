package jarno.oussama.com.examenmonitor.Database;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Students {

    @PrimaryKey
    @NonNull
    private String StudentNumber;
    @NonNull
    private String StudentCardId;
    @NonNull
    private String name;
    @NonNull
    private String lastName;


    public Students() {

    }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getStudentNumber() { return StudentNumber; }
    public void setStudentNumber(String StudentNumber) { this.StudentNumber = StudentNumber; }
    public String getStudentCardId() { return StudentCardId; }
    public void setStudentCardId(String StudentCardId) { this.StudentCardId = StudentCardId; }
}
