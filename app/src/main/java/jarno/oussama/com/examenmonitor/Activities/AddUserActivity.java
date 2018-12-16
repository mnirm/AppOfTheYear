package jarno.oussama.com.examenmonitor.Activities;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import jarno.oussama.com.examenmonitor.FirebaseDB.Student;
import jarno.oussama.com.examenmonitor.Nfc.NFC;
import jarno.oussama.com.examenmonitor.R;


public class AddUserActivity extends AppCompatActivity {
    TextView nfcStatusTextView;
    EditText nameEditText,studentNumberEditText,lastNameEditText;
    String  name, lastName;
    int studentNumber;
    NFC nfc;
    View view;
    Button addButton;
    DatabaseReference studentsRef = FirebaseDatabase.getInstance().getReference("students");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        nfcStatusTextView = findViewById(R.id.textViewNfcStatus);
        nameEditText = findViewById(R.id.editTextSurname);
        lastNameEditText = findViewById(R.id.editTextLastname);
        studentNumberEditText = findViewById(R.id.editTextSNummer);
        addButton = findViewById(R.id.buttonAddUser);
        view = findViewById(android.R.id.content);
        nfc = new NFC(this,this,view);
    }
    @Override
    protected void onResume() {
        super.onResume();
        nfc.enableForeGroundDispatch();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        nfc.setResolveIntent(intent,(boolean isValid) ->{
            if (isValid){
                addButton.setEnabled(true);
                nfcStatusTextView.setText(nfc.nfcID);
            }else{
                Snackbar.make(view,"ongeldige kaart",Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void AddUserToDB(View view) {
        Initialize();
        Student student = new Student();
        if (validate()){
            student.setFirstName(name);
            student.setLastName(lastName);
            student.setStudentNumber(studentNumber);
            student.setCardIdNumber(nfc.nfcID);
            studentsRef.child(Integer.toString(student.getStudentNumber())).setValue(student);
            Snackbar.make(view,student.getFirstName() + " is toegevoegd",Snackbar.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    private boolean validate() {
        if(name.isEmpty()) {
            nameEditText.setError(getResources().getString(R.string.name_required));
            return false;
        }
        if(lastName.isEmpty()) {
            lastNameEditText.setError(getResources().getString(R.string.last_name_required));
            return false;
        }
        if(studentNumberEditText.getText().toString().isEmpty()) {
            studentNumberEditText.setError(getResources().getString(R.string.studentnumber_required));
            return false;
        }
        return true;
    }

    public void Initialize(){
        name = nameEditText.getText().toString().trim();
        lastName = lastNameEditText.getText().toString().trim();
        studentNumber = Integer.parseInt(studentNumberEditText.getText().toString());
    }
}

    

