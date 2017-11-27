package pt.ua.classroom;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class TeacherAnsweredActivity extends AppCompatActivity implements View.OnClickListener, ChoicesFragment.OnItemSelectedListener{
    private static final String TAG = "AnswerPoolActivity";
    private TeacherAnsweredActivity activity=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_answered);

        TextView tv= (TextView) findViewById(R.id.TextView_Question);
        tv.setText(Database.getPoolQuestion());

        //Buttons
        Button buttonPost = (Button) findViewById(R.id.Button_post);
        buttonPost.setOnClickListener(this);

    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.Button_post:
                deletePost();
                break;
            default:
        }

    }

    private void deletePost() {
    }

    @Override
    public void onItemSelected(Choices i) {}
}
