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
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class TeacherMenuActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "TeacherMenuActivity";
    private TeacherMenuActivity activity=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_menu);

        TextView tv= (TextView) findViewById(R.id.classeName);
        tv.setText(Database.getClasseName());

        //Buttons
        Button buttonTeacher = (Button) findViewById(R.id.Button_delete_class);
        buttonTeacher.setOnClickListener(this);

        Button buttonList = (Button) findViewById(R.id.Button_list_students);
        buttonList.setOnClickListener(this);
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
            default:
        }
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
