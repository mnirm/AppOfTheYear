package jarno.oussama.com.examenmonitor;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener((View view) -> {
            startActivity(new Intent(this, AddUserActivity.class));
        });
        StudentDatabase db = StudentDatabase.getDatabase(this);
        students = db.Instance.StudentDao().getAllStudents();
        recyclerView = findViewById(R.id.recyclerviewStudents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StudentListAdapter(students);
        recyclerView.setAdapter(adapter);

    }

}
