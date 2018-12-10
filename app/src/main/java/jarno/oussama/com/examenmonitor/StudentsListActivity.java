package jarno.oussama.com.examenmonitor;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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
        students = new ArrayList<>();
        StudentDatabase db = StudentDatabase.getDatabase(this);
        students = db.Instance.StudentDao().getAllStudents();
        recyclerView = findViewById(R.id.recyclerviewStudents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StudentListAdapter(students);
        recyclerView.setAdapter(adapter);

    }

}
