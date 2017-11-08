package pt.ua.classroom;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class TeacherActivity extends AppCompatActivity implements ClassesListFragment.OnItemSelectedListener,View.OnClickListener {
    private static final String TAG = "Teacher Activity";
    private TeacherActivity activity=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        //Buttons
        Button buttonTeacher = (Button) findViewById(R.id.new_classe);
        buttonTeacher.setOnClickListener(this);
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
                startActivity(new Intent(this, Login.class));
                finish();
            case R.id.swap_role:
                studentActivity();
                finish();
            case R.id.default_role:
                Database.setRole("teacher");
        }

        return super.onOptionsItemSelected(item);
    }

    // Handles the event when the fragment list item is selected
    @Override
    public void onItemSelected(Classe classe) {
        Database.setClasseid(classe.getId());
        Database.startClasse(this);
    }

    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.new_classe:
                Database.setRole("teacher");
                newClasse();
                break;

            default:
        }

    }

    public void studentActivity(){
        startActivity(new Intent(this, StudentActivity.class));
    }

    public void newClasse(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Name of the Classe");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Database.addClasse(activity, input.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
