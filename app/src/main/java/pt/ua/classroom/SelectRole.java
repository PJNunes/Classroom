package pt.ua.classroom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class SelectRole extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SelectRole";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_role);

        //Buttons
        Button buttonTeacher = (Button) findViewById(R.id.button_teacher);
        buttonTeacher.setOnClickListener(this);


        Button buttonStudent = (Button) findViewById(R.id.button_student);
        buttonStudent.setOnClickListener(this);

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
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.button_teacher:
                Database.setRole("teacher");
                teacherActivity();
                finish();
                break;

            case R.id.button_student:
                Database.setRole("student");
                studentActivity();
                finish();
                break;

            default:
        }

    }


    public void teacherActivity(){
        Database.getTeachingClasses(this);
    }

    public void studentActivity(){
        startActivity(new Intent(this, StudentActivity.class));
    }

}
