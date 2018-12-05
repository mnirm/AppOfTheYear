package jarno.oussama.com.examenmonitor;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import jarno.oussama.com.examenmonitor.Database.Student;
import jarno.oussama.com.examenmonitor.Database.StudentDatabase;

public class AddUserActivity extends AppCompatActivity {
    TextView nfcStatusTextView;
    EditText nameEditText;
    EditText lastNameEditText;
    EditText studentNumberEditText;
    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    String nfcID;
    StudentDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        nfcStatusTextView = findViewById(R.id.textViewNfcStatus);
        nameEditText = findViewById(R.id.editTextSurname);
        lastNameEditText = findViewById(R.id.editTextLastname);
        studentNumberEditText = findViewById(R.id.editTextSNummer);
        StudentDatabase db = StudentDatabase.getDatabase(this);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null){
            Toast.makeText(this,"you dont have a nfc reader", Toast.LENGTH_SHORT).show();
            return;
        }
        pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, this.getClass())
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);


    }
    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null) {
            if (!nfcAdapter.isEnabled()) {
                showWirelessSettings();
            }
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
            enableInputs();
            nfcID = new String(ByteArrayToHexString(id));

        }
        nfcStatusTextView.setText(nfcID);
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
    private void enableInputs(){
        nameEditText.setClickable(true);
        nameEditText.setFocusable(true);
        nameEditText.setCursorVisible(true);
        nameEditText.setFocusableInTouchMode(true);
        lastNameEditText.setClickable(true);
        lastNameEditText.setFocusable(true);
        lastNameEditText.setCursorVisible(true);
        lastNameEditText.setFocusableInTouchMode(true);
        studentNumberEditText.setClickable(true);
        studentNumberEditText.setFocusable(true);
        studentNumberEditText.setCursorVisible(true);
        studentNumberEditText.setFocusableInTouchMode(true);

    }

    public void AddUserTtoDB(View view) {
        Student student = new Student();
        student.setFirstName(nameEditText.getText().toString());
        student.setLastName(lastNameEditText.getText().toString());
        student.setStudentNumber(studentNumberEditText.getText().toString());
        student.setCardIdNumber(nfcID);
        db.Instance.StudentDao().insertStudent(student);
        startActivity(new Intent(this, MainActivity.class));
        Toast.makeText(this,student.getFirstName() + " is toegevoegd",Toast.LENGTH_SHORT).show();
    }
}

    

