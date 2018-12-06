package jarno.oussama.com.examenmonitor.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;
@Dao
public interface StudentDao {
    @Query("SELECT * FROM student")
    List<Student> getAllStudents();

    @Insert
    void insertAllStudents(Student... student);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertStudent(Student student);
}
