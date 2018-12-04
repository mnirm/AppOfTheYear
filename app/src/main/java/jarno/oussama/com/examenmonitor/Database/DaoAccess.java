package jarno.oussama.com.examenmonitor.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

@Dao
public interface DaoAccess {

    @Insert
    void insertOnlySingleStudent (Students student);
    @Query("SELECT * FROM Students Where studentNumber = :studentNumber")
    Students fetchOneStudentbyStudentNumber (int studentNumber);
    @Query("SELECT * FROM Students Where StudentCardId = :studentCardId")
    Students fetchOneStudentbyStudentCardId (int studentCardId);
    @Query("SELECT * FROM Students")
    Students fetchOAllStudents ();
    @Update
    void updateStudent (Students student);
    @Delete
    void deleteStudent (Students student);
}
