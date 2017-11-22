package pt.ua.classroom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class StudentActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "StudentActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        //Buttons
        Button buttonClasses = (Button) findViewById(R.id.listClasses);
        buttonClasses.setOnClickListener(this);
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

    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.listClasses:
                Database.getAttendingClasses(this);
            /*case R.id.pools:
                poolAcvitity();
                finish();
                break;
                */
            default:
        }

    }
    /*
    public void poolAcvitity(){
        startActivity(new Intent(this, PoolActivity.class));
    }
*/
}


