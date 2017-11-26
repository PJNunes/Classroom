package pt.ua.classroom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class TeacherPoolListActivity extends AppCompatActivity implements TeacherPoolListFragment.OnItemSelectedListener,View.OnClickListener {
    private static final String TAG = "TeacherPoolListActivity";
    private TeacherPoolListActivity activity=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_pool_list);

        //Buttons
        Button buttonPool = (Button) findViewById(R.id.new_pool);
        buttonPool.setOnClickListener(this);
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
                studentActivity();
                finish();
                break;

            case R.id.default_role:
                Database.setRole("teacher");
        }

        return super.onOptionsItemSelected(item);
    }

    // Handles the event when the fragment list item is selected
    @Override
    public void onItemSelected(TeacherPool pool) {
        //Database.setClasseid(pool.getId());
        //Database.startClasse(this);
    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.new_pool:
                startActivity(new Intent(this, SimplePoolActivity.class));
            default:
        }
    }

    public void studentActivity(){
        Intent i = new Intent(this, StudentActivity.class);
        // set the new task and clear flags
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}