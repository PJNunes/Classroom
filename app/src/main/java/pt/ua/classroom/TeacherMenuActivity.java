package pt.ua.classroom;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class TeacherMenuActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "TeacherMenuActivity";
    private TeacherMenuActivity activity=this;
    public static final String ERROR_DETECTED = "No NFC tag detected!";
    public static final String WRITE_SUCCESS = "Text written to the NFC tag successfully!";
    public static final String WRITE_ERROR = "Error during writing, is the NFC tag close enough to your device?";
    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    IntentFilter writeTagFilters[];
    Tag myTag;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_menu);
        context = this;

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        TextView tv= (TextView) findViewById(R.id.classeName);
        tv.setText(Database.getClasseName());

        //Buttons
        Button buttonTeacher = (Button) findViewById(R.id.Button_delete_class);
        buttonTeacher.setOnClickListener(this);

        Button buttonList = (Button) findViewById(R.id.Button_list_students);
        buttonList.setOnClickListener(this);

        Button buttonPool = (Button) findViewById(R.id.Button_pools);
        buttonPool.setOnClickListener(this);

        Button buttonNFC = (Button) findViewById(R.id.write_nfc);
        buttonNFC.setOnClickListener(this);

        Button buttonAbsentee = (Button) findViewById(R.id.Button_absentees);
        buttonAbsentee.setOnClickListener(this);
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

        writeTagFilters = new IntentFilter[] { techDetected,tagDetected,ndefDetected};

        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        if(nfcAdapter!= null)
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, writeTagFilters, null);
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
                studentActivity();
                finish();
                break;

            case R.id.default_role:
                Database.setRole("teacher");
        }

        return super.onOptionsItemSelected(item);
    }

    public void studentActivity(){
        Intent i = new Intent(this, StudentActivity.class);
        // set the new task and clear flags
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.Button_delete_class:
                deleteClasse();
                break;
            case R.id.Button_list_students:
                Database.getStudents(this);
                break;
            case R.id.Button_pools:
                poolActivity();
                break;
            case R.id.write_nfc:
                write_nfc();
                break;
            case R.id.Button_absentees:
                Database.getAbsentees(this);
                break;
            default:
        }
    }

    private void write_nfc() {
        if (nfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
        }
        else {
            try {
                if (myTag == null) {
                    Log.d(TAG,"b");
                    Toast.makeText(context, ERROR_DETECTED, Toast.LENGTH_LONG).show();
                } else {
                    write(Database.getClasseID(), myTag);
                    Toast.makeText(context, WRITE_SUCCESS, Toast.LENGTH_LONG).show();
                }
            } catch (IOException | FormatException e) {
                Log.d(TAG,"a");
                Toast.makeText(context, WRITE_ERROR, Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    private void write(String text, Tag tag) throws IOException, FormatException {
        NdefRecord[] records = { createRecord(text) };
        NdefMessage message = new NdefMessage(records);
        // Get an instance of Ndef for the tag.
        Ndef ndef = Ndef.get(tag);
        // Enable I/O
        ndef.connect();
        // Write the message
        ndef.writeNdefMessage(message);
        // Close the connection
        ndef.close();
    }

    private NdefRecord createRecord(String text) throws UnsupportedEncodingException {
        String lang       = "en";
        byte[] textBytes  = text.getBytes();
        byte[] langBytes  = lang.getBytes("US-ASCII");
        int    langLength = langBytes.length;
        int    textLength = textBytes.length;
        byte[] payload    = new byte[1 + langLength + textLength];

        // set status byte (see NDEF spec for actual bits)
        payload[0] = (byte) langLength;

        // copy langbytes and textbytes into payload
        System.arraycopy(langBytes, 0, payload, 1,              langLength);
        System.arraycopy(textBytes, 0, payload, 1 + langLength, textLength);

        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN,  NdefRecord.RTD_TEXT,  new byte[0], payload);
    }


    public void poolActivity(){
        startActivity(new Intent(this, PoolActivity.class));
    }

    private void deleteClasse() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
         builder.setTitle("Are you sure you want to delete this classe: "+Database.getClasseName());

        // Set up the buttons
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                Database.deleteClasse(activity);
        }
            });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

}
