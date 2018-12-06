package jarno.oussama.com.examenmonitor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void AddUserActivity(View view) {
        Intent intentAdduser = new Intent(this, AddUserActivity.class);
        startActivity(intentAdduser);
    }
    public void StudentsListActivity(View view) {
        Intent StudentList = new Intent(this, StudentsListActivity.class);
        startActivity(StudentList);
    }
}
