package jarno.oussama.com.examenmonitor.Database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Student.class},version = 5)
public abstract class StudentDatabase extends RoomDatabase{
    public static StudentDatabase Instance;
    public abstract StudentDao StudentDao();

    public static StudentDatabase getDatabase(Context context ) {
        if (Instance == null){
            Instance =  Room.databaseBuilder(context.getApplicationContext(),StudentDatabase.class,"development")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return Instance;
    }
}
