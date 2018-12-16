package jarno.oussama.com.examenmonitor.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import java.util.List;


import jarno.oussama.com.examenmonitor.FirebaseDB.Student;
import jarno.oussama.com.examenmonitor.R;
import jarno.oussama.com.examenmonitor.Adapters.StudentListAdapter;


public class StudentsListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<Student> students;
    DatabaseReference studentsRef = FirebaseDatabase.getInstance().getReference("students");


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
        students = new ArrayList<Student>();
        recyclerView = findViewById(R.id.recyclerviewStudents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        studentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                students.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Student studentFBDB = data.getValue(Student.class);
                    Student student = new Student();
                    student.setFirstName(studentFBDB.getFirstName());
                    student.setLastName(studentFBDB.getLastName());
                    student.setCardIdNumber(studentFBDB.getCardIdNumber());
                    student.setStudentNumber(studentFBDB.getStudentNumber());
                    students.add(student);
                }
                adapter = new StudentListAdapter(students);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Hello", "Failed to read value.", databaseError.toException());
            }
        });
    }

}
