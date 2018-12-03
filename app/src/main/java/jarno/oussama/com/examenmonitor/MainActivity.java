package jarno.oussama.com.examenmonitor;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    String nfcID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.nfcDataTextView);
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
            if (!nfcAdapter.isEnabled())
                showWirelessSettings();

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

                byte[] empty = new byte[0];
                byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
                nfcID = new String(ByteArrayToHexString(id));

                }
                textView.setText(nfcID);
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
    }
