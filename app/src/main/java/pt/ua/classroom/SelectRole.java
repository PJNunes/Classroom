package pt.ua.classroom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
