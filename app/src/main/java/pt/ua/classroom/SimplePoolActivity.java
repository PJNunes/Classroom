package pt.ua.classroom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SimplePoolActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_pool);

        //Edit Text
        EditText editTextQuestion = (EditText) findViewById(R.id.EditText_Question);

        //Buttons
        Button buttonPost = (Button) findViewById(R.id.Button_post);
        buttonPost.setOnClickListener(this);

    }

    private String getText(){
        EditText editTextQuestion = (EditText) findViewById(R.id.EditText_Question);
        return editTextQuestion.getText().toString();
    }

    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.Button_post:
                String question = getText();
                Database.addPool(this, question, "simple");
                break;
            default:
        }

    }

}
