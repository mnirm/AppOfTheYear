package jarno.oussama.com.examenmonitor.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import jarno.oussama.com.examenmonitor.FirebaseDB.Exam;
import jarno.oussama.com.examenmonitor.FirebaseDB.CheckInOutRegistration;
import jarno.oussama.com.examenmonitor.FirebaseDB.Student;
import jarno.oussama.com.examenmonitor.Nfc.NFC;
import jarno.oussama.com.examenmonitor.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
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
    DatabaseReference studentsRef;
    Exam currentExam;
    TextView textViewExamTitle, textViewRegistrationFeedback;
    NFC nfc;
    View view;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in_out);
        textViewExamTitle = findViewById(R.id.textViewExamTitle);
        textViewRegistrationFeedback = findViewById(R.id.textViewRegistrationFeedback);
        Intent intent = getIntent();
        examID = intent.getStringExtra("EXAM_ID");
        auth = FirebaseAuth.getInstance();
        examsRef = firebaseDatabase.getReference("exams").child(auth.getCurrentUser().getUid());
        checkInRegistrationRef = firebaseDatabase.getReference("checkInRegistrations");
        checkOutRegistrationRef = firebaseDatabase.getReference("checkOutRegistrations");
        studentsRef = firebaseDatabase.getReference("students");
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
        examsRef.child(examID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    currentExam = dataSnapshot.getValue(Exam.class);
                    textViewExamTitle.setText(currentExam.getName());
                }else{
                    Intent examlist = new Intent(getApplicationContext(), MyExamsActivity.class);
                    startActivity(examlist);
                }

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
                            studentsRef.child(nfc.nfcID).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Student student = dataSnapshot.getValue(Student.class);
                                    registrationFeedback(CheckInOutRegistration.RegistrationType.checkOut,student);
                                    registrationTextViewAnimation();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    textViewRegistrationFeedback.setText(getString(R.string.unknown_student_message));
                                }
                            });
                        } else {
                            checkInRegistrationRef.child(checkInOutEntry.getExamId()).child(checkInOutEntry.getCardId()).setValue(checkInOutEntry);
                            studentsRef.child(nfc.nfcID).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Student student = dataSnapshot.getValue(Student.class);
                                    registrationFeedback(CheckInOutRegistration.RegistrationType.checkIn,student);
                                    registrationTextViewAnimation();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    textViewRegistrationFeedback.setText(getString(R.string.unknown_student_message));
                                }
                            });
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
    private void registrationFeedback(CheckInOutRegistration.RegistrationType registrationType, Student student){
        if (registrationType == CheckInOutRegistration.RegistrationType.checkIn)
            textViewRegistrationFeedback.setText(String.format("Je hebt successvol ingecheckt %s, veel succes met %s", student.getFirstName(), currentExam.getName()));
        else
            textViewRegistrationFeedback.setText(String.format("Je hebt successvol uitgecheckt %s, Tot ziens!", student.getFirstName()));
    }

    private void registrationTextViewAnimation() {
        AlphaAnimation fadeIn = new AlphaAnimation(0.0f , 1.0f ) ;
        AlphaAnimation fadeOut = new AlphaAnimation( 1.0f , 0.0f ) ;
        textViewRegistrationFeedback.startAnimation(fadeIn);
        textViewRegistrationFeedback.startAnimation(fadeOut);
        fadeIn.setDuration(800);
        fadeIn.setFillAfter(true);
        fadeOut.setDuration(800);
        fadeOut.setFillAfter(true);
        fadeOut.setStartOffset(3200+fadeIn.getStartOffset());
    }

}
