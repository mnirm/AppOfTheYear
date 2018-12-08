package jarno.oussama.com.examenmonitor.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

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
