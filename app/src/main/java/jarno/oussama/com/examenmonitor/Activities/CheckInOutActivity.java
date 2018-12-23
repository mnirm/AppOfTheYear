package jarno.oussama.com.examenmonitor.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import jarno.oussama.com.examenmonitor.FirebaseDB.Exam;
import jarno.oussama.com.examenmonitor.FirebaseDB.CheckInOutRegistration;
import jarno.oussama.com.examenmonitor.Nfc.NFC;
import jarno.oussama.com.examenmonitor.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class CheckInOutActivity extends AppCompatActivity {
    String examID;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference examsRef;
    DatabaseReference checkInRegistrationRef;
    DatabaseReference checkOutRegistrationRef;
    Exam currentExam;
    TextView textViewExamTitle;
    NFC nfc;
    View view;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in_out);
        textViewExamTitle = findViewById(R.id.textViewExamTitle);
        Intent intent = getIntent();
        examID = intent.getStringExtra("EXAM_ID");
        auth = FirebaseAuth.getInstance();
        examsRef = firebaseDatabase.getReference("exams").child(auth.getCurrentUser().getUid());
        checkInRegistrationRef = firebaseDatabase.getReference("checkInRegistrations");
        checkOutRegistrationRef = firebaseDatabase.getReference("checkOutRegistrations");
        view = findViewById(android.R.id.content);
        nfc = new NFC(this, this, view);
        if (savedInstanceState != null) {
            examID = savedInstanceState.getString("EXAM_ID");
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("EXAM_ID", examID);
    }

    @Override
    protected void onStart() {
        super.onStart();
        examsRef.child(examID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentExam = dataSnapshot.getValue(Exam.class);
                textViewExamTitle.setText(currentExam.getName());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        nfc.enableForeGroundDispatch();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        nfc.setResolveIntent(intent, (boolean isValid) -> {
            if (isValid) {
                CheckInOutRegistration checkInOutEntry = new CheckInOutRegistration();
                checkInOutEntry.setCardId(nfc.nfcID);
                checkInOutEntry.setExamId(currentExam.getExamId());
                checkInOutEntry.setTimeStamp(Calendar.getInstance().getTimeInMillis());
                checkInRegistrationRef.child(checkInOutEntry.getExamId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(checkInOutEntry.getCardId())) {
                            checkOutRegistrationRef.child(checkInOutEntry.getExamId()).child(checkInOutEntry.getCardId()).setValue(checkInOutEntry);
                            Snackbar.make(view, nfc.nfcID + " uitgechecked", Snackbar.LENGTH_SHORT).show();
                        } else {
                            checkInRegistrationRef.child(checkInOutEntry.getExamId()).child(checkInOutEntry.getCardId()).setValue(checkInOutEntry);
                            Snackbar.make(view, nfc.nfcID + " ingechecked", Snackbar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            } else {
                Snackbar.make(view, "ongeldige kaart", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
