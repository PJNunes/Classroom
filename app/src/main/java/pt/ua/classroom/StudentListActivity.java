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

public class StudentListActivity extends AppCompatActivity implements  StudentListFragment.OnItemSelectedListener, View.OnClickListener{
    private static final String TAG = "StudentListActivity";
    private StudentListActivity activity=this;
    private String studentId="";
    private String studentName="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_list);

        StudentListFragment fragmentItemsList = (StudentListFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentStudentsList);
        fragmentItemsList.setActivateOnItemClick(true);

        //Buttons
        Button buttonAdd = (Button) findViewById(R.id.new_student);
        buttonAdd.setOnClickListener(this);

        Button buttonRemove = (Button) findViewById(R.id.remove_student);
        buttonRemove.setOnClickListener(this);

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

            case R.id.new_student:
                newStudent();
                break;
            case R.id.remove_student:
                removeStudent();
                break;
            default:
        }
    }

    private void newStudent() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Name of the student");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Database.addStudent(activity, input.getText().toString());
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

    private void removeStudent() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (studentName.equals("")){
            builder.setTitle("You need to select a student first!");
            // Set up the buttons
            builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        }
        else {
            builder.setTitle("Are you sure you want to remove this student: " + studentName + "?");
            // Set up the buttons
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Database.removeStudent(activity, studentId);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        }
    }

}