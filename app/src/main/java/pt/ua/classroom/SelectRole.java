package pt.ua.classroom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class SelectRole extends AppCompatActivity implements View.OnClickListener {

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
                teacherActivity();
                Log.d("Role --> teacher", "se está ejecutando");
                finish();
                break;

            case R.id.button_student:
                studentActivity();
                Log.d("Role --> student", "se está ejecutando");
                finish();
                break;

            default:
                break;
        }

    }


    public void teacherActivity(){
        startActivity(new Intent(this, TeacherActivity.class));
    }

    public void studentActivity(){
        startActivity(new Intent(this, StudentActivity.class));
    }

}
