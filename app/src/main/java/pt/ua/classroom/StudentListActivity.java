package pt.ua.classroom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

public class StudentListActivity extends AppCompatActivity implements  StudentListFragment.OnItemSelectedListener{
    private static final String TAG = "StudentListActivity";
    private StudentListActivity activity=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_list);

        StudentListFragment fragmentItemsList = (StudentListFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentStudentsList);
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
        String classId = i.getId();
    }

    public void studentActivity(){
        startActivity(new Intent(this, StudentActivity.class));
    }
}