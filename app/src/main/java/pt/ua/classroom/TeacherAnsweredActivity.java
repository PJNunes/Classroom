package pt.ua.classroom;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

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


    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.Button_post:
                deletePost();
                break;
            default:
        }

    }

    private void deletePost() {
        Database.deletePost(this);
    }

    @Override
    public void onItemSelected(Choices i) {}
}
