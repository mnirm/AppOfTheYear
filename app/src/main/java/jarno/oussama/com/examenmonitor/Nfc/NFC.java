package jarno.oussama.com.examenmonitor.Nfc;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class NFC {
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private Context context;
    private Activity activity;
    public String  nfcID;
    View view;

    public NFC(Context context,Activity activity ,View view) {
        this.activity = activity;
        this.context = context;
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(context);
        this.view = view;

        if (nfcAdapter == null){
            Snackbar.make(view,"Your device doesn't have an NFC reader",Snackbar.LENGTH_SHORT).show();
            return;
        }else{
            if (!nfcAdapter.isEnabled()) {
                showWirelessSettings();
            }
        }
        pendingIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, context.getClass())
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }
    private void showWirelessSettings() {
        Toast.makeText(context, "You need to enable NFC", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        context.startActivity(intent);
    }
    public void enableForeGroundDispatch(){
        if (nfcAdapter != null) {
            nfcAdapter.enableForegroundDispatch(activity, pendingIntent, null, null);
        }
    }
    public void setResolveIntent(Intent intent,  nfcOnScan nfcOnScan){
        activity.setIntent(intent);
        resolveIntent(intent, nfcOnScan);
    }

    private void resolveIntent(Intent intent, nfcOnScan nfcOnScan) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)){
            byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
            nfcID = ByteArrayToHexString(id);
            if(nfcID.substring(9).trim().equals("DE") ){

                nfcOnScan.handleNfcScan(true);
            }else{

                nfcOnScan.handleNfcScan(false);
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
}

