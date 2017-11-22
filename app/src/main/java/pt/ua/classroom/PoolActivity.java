package pt.ua.classroom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PoolActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pool);


        //Buttons
        Button buttonNewPool = (Button) findViewById(R.id.new_pool);
        buttonNewPool.setOnClickListener(this);

    }

    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.new_pool:
                simplePoolActivity();
                finish();
                break;
            default:
        }

    }

    public void simplePoolActivity(){
        startActivity(new Intent(this, SimplePoolActivity.class));
    }

}


