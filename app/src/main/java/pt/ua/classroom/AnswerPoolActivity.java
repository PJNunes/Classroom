package pt.ua.classroom;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class AnswerPoolActivity extends AppCompatActivity implements View.OnClickListener, ChoicesFragment.OnItemSelectedListener{
    private static final String TAG = "AnswerPoolActivity";
    private AnswerPoolActivity activity=this;
    private String type;
    private String choice;
    private ArrayList<String> choices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_pool);

        TextView tv= (TextView) findViewById(R.id.TextView_Question);
        tv.setText(Database.getPoolQuestion());

        ChoicesFragment fragmentList = (ChoicesFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentChoiceList);
        choice="";
        choices=new ArrayList<>();

        //Buttons
        Button buttonPost = (Button) findViewById(R.id.Button_post);
        buttonPost.setOnClickListener(this);

        type= Database.getPoolType();
        switch (type) {
            case "open_text":
                findViewById(R.id.layout_choice_type).setVisibility(View.GONE);
                findViewById(R.id.layout_open_type).setVisibility(View.VISIBLE);
                break;
            case "single":
                findViewById(R.id.layout_choice_type).setVisibility(View.VISIBLE);
                findViewById(R.id.layout_open_type).setVisibility(View.GONE);
                fragmentList.setActivateOnItemClick(ListView.CHOICE_MODE_SINGLE);
                break;
            case "multiple":
                findViewById(R.id.layout_choice_type).setVisibility(View.VISIBLE);
                findViewById(R.id.layout_open_type).setVisibility(View.GONE);
                fragmentList.setActivateOnItemClick(ListView.CHOICE_MODE_MULTIPLE);
                break;
            default:
        }

    }

    private String getText(){
        EditText editTextQuestion = (EditText) findViewById(R.id.EditText_Question);
        return editTextQuestion.getText().toString();
    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.Button_post:
                postAnswer();
                break;
            default:
        }

    }

    private void postAnswer() {
        switch (type) {
            case "open_text":
                openAnswer();
                break;
            case "single":
                singleAnswer();
                break;
            case "multiple":
                multipleAnswer();
                break;
            default:
        }
    }

    private void multipleAnswer() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (choices.size()==0){
            builder.setTitle("You need to select at least a choice!");
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
            Database.addAnswer(activity,"",choices);
        }
    }

    private void singleAnswer() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (choice.equals("")){
            builder.setTitle("You need to select a choice!");
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
            Database.addAnswer(activity,choice, choices);
        }
    }

    private void openAnswer() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String answer=getText();
        if (answer.equals("")){
            builder.setTitle("You need to answer the question!");
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
            Database.addAnswer(activity,answer, choices);
        }
    }

    @Override
    public void onItemSelected(Choices i) {
        if (type.equals("single"))
            choice=i.getTitle();
        else{
            String temp=i.getTitle();
            if(choices.contains(temp))
                choices.remove(temp);
            else
                choices.add(temp);
        }
    }
}