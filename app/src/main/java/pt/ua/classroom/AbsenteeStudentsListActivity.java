package pt.ua.classroom;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

public class AbsenteeStudentsListActivity extends AppCompatActivity implements  StudentListFragment.OnItemSelectedListener, View.OnClickListener{
    private static final String TAG = "AbStudentListActivity";
    private AbsenteeStudentsListActivity activity=this;
    private String studentId="";
    private String studentName="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absentee_students_list);

        AbsenteeStudentListFragment fragmentItemsList = (AbsenteeStudentListFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentAbsenteeStudentsList);
        fragmentItemsList.setActivateOnItemClick(true);

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

    @Override
    public void onItemSelected(Student i) {
        studentId = i.getId();
        studentName=i.getTitle();
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
            default:
        }
    }

}
