package jarno.oussama.com.examenmonitor.Activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import jarno.oussama.com.examenmonitor.R;

import android.view.View;

import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    public void AddUserActivity(View view) {
        Intent intentAdduser = new Intent(this, AddUserActivity.class);
        startActivity(intentAdduser);
    }
    public void StudentsListActivity(View view) {
        Intent StudentList = new Intent(this, StudentsListActivity.class);
        startActivity(StudentList);
    }
    public void NewExamActivity(View view) {
        Intent StudentList = new Intent(this, NewExamActivity.class);
        startActivity(StudentList);
    }
}
