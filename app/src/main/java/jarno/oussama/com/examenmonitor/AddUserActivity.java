package jarno.oussama.com.examenmonitor;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.provider.Settings;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import jarno.oussama.com.examenmonitor.Database.Student;
import jarno.oussama.com.examenmonitor.Database.StudentDatabase;

public class AddUserActivity extends AppCompatActivity {
    TextView nfcStatusTextView;
    EditText nameEditText,studentNumberEditText,lastNameEditText;
    String  nfcID,name, lastName, studentNumber;
    View view;
    Button addButton;
    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    StudentDatabase db;

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
        StudentDatabase db = StudentDatabase.getDatabase(this);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null){
            Snackbar.make(view,"Your device doesn't have an NFC reader",Snackbar.LENGTH_SHORT).show();
            return;
        }else{
            if (!nfcAdapter.isEnabled()) {
                showWirelessSettings();
            }
        }
        pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, this.getClass())
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null) {
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        resolveIntent(intent);
    }

    private void resolveIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)){
            byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
            nfcID = ByteArrayToHexString(id);
            if(nfcID.substring(9).trim().equals("DE") ){
                addButton.setEnabled(true);
                nfcStatusTextView.setText(nfcID);
            }else{
                Snackbar.make(view,"ongeldige kaart",Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    String ByteArrayToHexString(byte [] inarray)
    {
        int i, j, in, lastIndex;
        String [] hex = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
        String tempOut= "";
        String out = "";
        lastIndex = 0;
        for(j = 0 ; j < inarray.length ; ++j)
        {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            tempOut += hex[i];
            i = in & 0x0f;
            tempOut += hex[i];
        }
        for (int k=1; k <= tempOut.length(); k++)
            if (k%2 == 0) {
                out += tempOut.substring(lastIndex, k);
                if (k < tempOut.length())
                    out += ":";
                lastIndex = k;
            }
        return out;
    }

    private void showWirelessSettings() {
        Toast.makeText(this, "You need to enable NFC", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        startActivity(intent);
    }

    public void AddUserTtoDB(View view) {
        Initialize();
        Student student = new Student();
        if (validate()){
            student.setFirstName(name);
            student.setLastName(lastName);
            student.setStudentNumber(studentNumber);
            student.setCardIdNumber(nfcID);
            db.Instance.StudentDao().insertStudent(student);
            Snackbar.make(view,student.getFirstName() + " is toegevoegd",Snackbar.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    private boolean validate() {
        boolean valid = true;
        if(name.isEmpty())
            nameEditText.setError(getResources().getString(R.string.name_required));
            valid =false;
        if(lastName.isEmpty())
            lastNameEditText.setError(getResources().getString(R.string.last_name_required));
            valid =false;
        if(studentNumber.isEmpty())
            studentNumberEditText.setError(getResources().getString(R.string.studentnumber_required));
            valid =false;
        return valid;
    }

    public void Initialize(){
        name = nameEditText.getText().toString().trim();
        lastName = lastNameEditText.getText().toString().trim();
        studentNumber = studentNumberEditText.getText().toString().trim();
    }
}

    

