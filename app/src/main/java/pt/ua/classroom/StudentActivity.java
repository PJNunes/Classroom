package pt.ua.classroom;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class StudentActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "StudentActivity";
    public static final String ERROR_DETECTED = "No NFC tag detected!";
    public static final String READ_SUCCESS = "Class read from the NFC tag successfully!";
    public static final String READ_ERROR = "Error during reading, is the NFC tag close enough to your device?";
    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    IntentFilter readTagFilters[];
    Tag myTag;
    Context context;
    Ndef ndef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        context = this;
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        //Buttons
        Button buttonClasses = (Button) findViewById(R.id.listClasses);
        buttonClasses.setOnClickListener(this);

        Button buttonNFC = (Button) findViewById(R.id.readNFC);
        buttonNFC.setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(nfcAdapter!= null)
            nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    public void onResume(){
        super.onResume();

        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        IntentFilter techDetected = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);

        readTagFilters = new IntentFilter[] { techDetected,tagDetected,ndefDetected};

        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        if(nfcAdapter!= null)
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, readTagFilters, null);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        myTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        Log.d(TAG, "onNewIntent: "+intent.getAction());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.log_out:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;

            case R.id.swap_role:
                Database.getTeachingClasses(this);
                break;
            case R.id.default_role:
                Database.setRole("student");

        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.listClasses:
                Database.getAttendingClasses(this);
                break;
            case R.id.readNFC:
                readNFC();
                break;
            /*case R.id.pools:
                poolAcvitity();
                finish();
                break;
                */
            default:
        }

    }

    private void readNFC() {
        if (nfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
        }
        else {
            if (myTag == null) {
                Log.d(TAG,"b");
                Toast.makeText(context, ERROR_DETECTED, Toast.LENGTH_LONG).show();
            } else {
                read();
                myTag=null;
                Toast.makeText(context, READ_SUCCESS, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void read() {
        Ndef ndef = Ndef.get(myTag);

        try {
            ndef.connect();
            NdefMessage ndefMessage = ndef.getNdefMessage();
            String message = new String(ndefMessage.getRecords()[0].getPayload());
            Log.d(TAG, "readFromNFC: "+message);
            Database.markPresence(message.substring(3));
            ndef.close();

        } catch (IOException | FormatException e) {
            e.printStackTrace();

        }
    }
    /*
    public void poolAcvitity(){
        startActivity(new Intent(this, PoolActivity.class));
    }
*/
}


