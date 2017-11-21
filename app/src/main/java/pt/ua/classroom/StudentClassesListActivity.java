package pt.ua.classroom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

public class StudentClassesListActivity extends AppCompatActivity implements  StudentClassesListFragment.OnItemSelectedListener{
    private static final String TAG = "StudentClassesListActivity";
    private StudentClassesListActivity activity=this;
    private String classId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_classes_list);
        classId="";

        StudentClassesListFragment fragmentItemsList = (StudentClassesListFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentStudentClassesList);
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
                Database.getTeachingClasses(this);
                break;

            case R.id.default_role:
                Database.setRole("student");
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(StudentClasse i) {
        classId=i.getId();
    }
}