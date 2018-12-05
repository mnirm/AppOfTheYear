package jarno.oussama.com.examenmonitor;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.List;

import jarno.oussama.com.examenmonitor.Database.Student;
import jarno.oussama.com.examenmonitor.Database.StudentDatabase;

public class StudentsListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<Student> students;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });
        StudentDatabase db = StudentDatabase.getDatabase(this);
        students = db.Instance.StudentDao().getAllStudents();
        recyclerView = findViewById(R.id.recyclerviewStudents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StudentListAdapter(students);
        recyclerView.setAdapter(adapter);

    }

}
