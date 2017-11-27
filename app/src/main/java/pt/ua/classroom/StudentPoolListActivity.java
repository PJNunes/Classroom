package pt.ua.classroom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class StudentPoolListActivity extends AppCompatActivity implements StudentPoolListFragment.OnItemSelectedListener{
    private static final String TAG = "StudentPoolListActivity";
    private StudentPoolListActivity activity=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_pool_list);
    }

    @Override
    public void onResume(){
        super.onResume();
        if(Database.getPoolRecreate()){
            Database.setPoolRecreate(false);
            recreate();
        }
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

    // Handles the event when the fragment list item is selected
    @Override
    public void onItemSelected(StudentPool pool) {
        Database.getToAnswerPool(this,pool.getId());
    }
}