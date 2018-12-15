package jarno.oussama.com.examenmonitor;

import androidx.appcompat.app.AppCompatActivity;
import jarno.oussama.com.examenmonitor.FirebaseDB.Exam;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class NewExamActivity extends AppCompatActivity {
    EditText editTextExamName;
    TextView textViewStartTime;
    TextView textViewEndTime;
    Switch switchRegistrationsAllowedAfterEndTime;
    TimePicker timePicker;
    LinearLayout linearLayoutTimePicker;
    Button buttonSetTime;
    Button buttonNewExam;
    TextView clickedView;
    Calendar startTime;
    Calendar endTime;
    String examName;
    DatabaseReference examsRef;
    Exam exam = new Exam();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_exam);
        editTextExamName = findViewById(R.id.editTextExamName);
        textViewStartTime = findViewById(R.id.textViewStartTime);
        textViewEndTime = findViewById(R.id.textViewEndTime);
        linearLayoutTimePicker = findViewById(R.id.linearLayoutTimePicker);
        switchRegistrationsAllowedAfterEndTime = findViewById(R.id.switchRegistrationsAllowedAfterEndTime);
        buttonSetTime = findViewById(R.id.buttonSetTime);
        buttonNewExam = findViewById(R.id.buttonNewExam);
        timePicker = findViewById(R.id.timePicker);
        startTime = new GregorianCalendar();
        startTime.set(Calendar.MILLISECOND,Calendar.getInstance().get(Calendar.MILLISECOND));
        endTime = new GregorianCalendar();
        //int temp = ;
        endTime.set(Calendar.MILLISECOND,Calendar.getInstance().get(Calendar.MILLISECOND) + 3600000);
        textViewStartTime.setText(startTime.get(Calendar.HOUR) + ":" +startTime.get(Calendar.MINUTE) );
        textViewEndTime.setText(endTime.get(Calendar.HOUR) + ":" + endTime.get(Calendar.MINUTE) );
        examsRef = FirebaseDatabase.getInstance().getReference("exams");
        textViewStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutTimePicker.setVisibility(View.VISIBLE);
                clickedView = (TextView) v;
            }
        });
        textViewEndTime.setOnClickListener((View v) -> {
            linearLayoutTimePicker.setVisibility(View.VISIBLE);
            clickedView = (TextView) v;
        });
        buttonSetTime.setOnClickListener((View v) -> {
            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();
            if (clickedView.getId() == textViewStartTime.getId()) {
                startTime.set(Calendar.HOUR, timePicker.getHour());
                startTime.set(Calendar.MINUTE, timePicker.getMinute());
            }
            if (clickedView.getId() == textViewEndTime.getId()) {
                endTime.set(Calendar.HOUR, timePicker.getHour());
                endTime.set(Calendar.MINUTE, timePicker.getMinute());
            }
            clickedView.setText(hour + ":" + minute);
            linearLayoutTimePicker.setVisibility(View.GONE);
        });
        buttonNewExam.setOnClickListener((View v) -> {
            initialize();
            if (validateExam()) {
                exam.setStartTime(startTime.getTimeInMillis());
                exam.setEndTime(endTime.getTimeInMillis());
                exam.setName(examName);
                exam.setRegistrationAfterEndTimeAllowed(switchRegistrationsAllowedAfterEndTime.isChecked());
                exam.setExamId(examName +"_"+startTime.get(Calendar.DAY_OF_MONTH)+"_"+ startTime.get(Calendar.HOUR));
                //examsRef.push().setValue(exam);
                examsRef.child(exam.getExamId()).setValue(exam);
                Intent intent = new Intent(this, CheckInOutActivity.class).putExtra("EXAM_NAME", exam.getName());
                startActivity(intent);
            }
        });
    }

    private boolean validateExam() {
        if (examName.isEmpty()) {
            editTextExamName.setError(getResources().getText(R.string.exam_name_required));
            return false;
        }
        return true;
    }

    private void initialize() {
        examName = editTextExamName.getText().toString().trim();
    }
}
