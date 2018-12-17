package jarno.oussama.com.examenmonitor.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import jarno.oussama.com.examenmonitor.Adapters.ExamsListAdapter;
import jarno.oussama.com.examenmonitor.FirebaseDB.Exam;
import jarno.oussama.com.examenmonitor.FirebaseDB.Student;
import jarno.oussama.com.examenmonitor.R;

public class MyExamsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<Exam> MyExams;
    DatabaseReference examsRef = FirebaseDatabase.getInstance().getReference("exams");
    FirebaseAuth auth;
    TextView textViewNoExams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_exams);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener((View view) -> {
            startActivity(new Intent(this, AddUserActivity.class));
        });
        auth = FirebaseAuth.getInstance();
        MyExams = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerviewMyExams);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        textViewNoExams = findViewById(R.id.textViewNoExams);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Query query = examsRef.child(auth.getCurrentUser().getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                MyExams.clear();
                for ( DataSnapshot data : dataSnapshot.getChildren()){
                    Exam examFBDB = data.getValue(Exam.class);
                    Exam exam = new Exam();
                    exam.setExamId(examFBDB.getExamId());
                    exam.setCreatedByUid(examFBDB.getCreatedByUid());
                    exam.setStartTime(examFBDB.getStartTime());
                    exam.setEndTime(examFBDB.getEndTime());
                    exam.setName(examFBDB.getName());
                    exam.setRegistrationAfterEndTimeAllowed(examFBDB.getRegistrationAfterEndTimeAllowed());
                    MyExams.add(exam);
                }
                if (MyExams.isEmpty()){
                    textViewNoExams.setVisibility(View.VISIBLE);
                }
                adapter = new ExamsListAdapter(MyExams);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("DB", "Failed to read value.", databaseError.toException());
            }
        });
    }
}
