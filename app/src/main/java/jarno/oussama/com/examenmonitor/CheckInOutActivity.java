package jarno.oussama.com.examenmonitor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import jarno.oussama.com.examenmonitor.FirebaseDB.Exam;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class CheckInOutActivity extends AppCompatActivity {
    String examName;
    DatabaseReference examsRef;
    Query getExamQuery;
    Exam currentExam;
    TextView textViewExamTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in_out);
        textViewExamTitle = findViewById(R.id.textViewExamTitle);
        Intent intent = getIntent();
        examName = intent.getStringExtra("EXAM_NAME");
        examsRef = FirebaseDatabase.getInstance().getReference("exams");
        getExamQuery = examsRef.orderByChild("name").limitToFirst(1).equalTo(examName);
        //getExamQuery.addListenerForSingleValueEvent();
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*getExamQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot DataSnapshot : dataSnapshot.getChildren()) {
                    currentExam = DataSnapshot.getValue(Exam.class);

                }
//                currentExam = (Exam) dataSnapshot;
                textViewExamTitle.setText(currentExam.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
        getExamQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    currentExam = dataSnapshot.getValue(Exam.class);


                textViewExamTitle.setText(currentExam.getName());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                currentExam = dataSnapshot.getValue(Exam.class);


                textViewExamTitle.setText(currentExam.getName());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                textViewExamTitle.setText(examName + "was removed");
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
