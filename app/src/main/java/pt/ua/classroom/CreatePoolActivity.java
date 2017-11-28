package pt.ua.classroom;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreatePoolActivity extends AppCompatActivity implements View.OnClickListener, ChoicesFragment.OnItemSelectedListener{
    private static final String TAG = "CreatePoolActivity";
    private ChoicesFragment fragmentList;
    private String type;
    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pool);
        type="";
        text="";

        fragmentList = (ChoicesFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentChoiceList);

        //Edit Text
        EditText editTextQuestion = (EditText) findViewById(R.id.EditText_Question);

        //Buttons
        Button buttonPost = (Button) findViewById(R.id.Button_post);
        buttonPost.setOnClickListener(this);
        Button buttonOpen = (Button) findViewById(R.id.Button_open_text);
        buttonOpen.setOnClickListener(this);
        Button buttonMC = (Button) findViewById(R.id.Button_multiple_choice);
        buttonMC.setOnClickListener(this);
        Button buttonSC = (Button) findViewById(R.id.Button_single_choice);
        buttonSC.setOnClickListener(this);
        Button buttonBack = (Button) findViewById(R.id.Button_back);
        buttonBack.setOnClickListener(this);
        Button buttonAddOp = (Button) findViewById(R.id.add_choice);
        buttonAddOp.setOnClickListener(this);
        Button buttonRemoveOp = (Button) findViewById(R.id.remove_choice);
        buttonRemoveOp.setOnClickListener(this);

    }

    private String getText(){
        EditText editTextQuestion = (EditText) findViewById(R.id.EditText_Question);
        return editTextQuestion.getText().toString();
    }

    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.Button_post:
                postPool();
                break;
            case R.id.Button_back:
                type="";
                setVisibilitySelect();
                break;
            case R.id.Button_open_text:
                type="open_text";
                setVisibilityOpen();
                break;
            case R.id.Button_multiple_choice:
                Choices.purge();
                type="multiple";
                setVisibilityChoice();
                break;
            case R.id.Button_single_choice:
                Choices.purge();
                type="single";
                setVisibilityChoice();
                break;
            case R.id.add_choice:
                addChoice();
                break;
            case R.id.remove_choice:
                removeChoice();
                break;

            default:
        }

    }

    private void postPool() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String question=getText();
        if (question.equals("")){
            builder.setTitle("You need to insert a question!");
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
            if (!type.equals("open_text") && Choices.getChoices().size()==0){
                builder = new AlertDialog.Builder(this);
                builder.setTitle("You need to insert at least one choice!");
                // Set up the buttons
                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                }
                    });

                builder.show();
            }
            else
                Database.addPool(this, question, type);
        }
    }

    private void setVisibilitySelect() {
        findViewById(R.id.layout_select_type).setVisibility(View.VISIBLE);
        findViewById(R.id.layout_open_type).setVisibility(View.GONE);
        findViewById(R.id.layout_choice_type).setVisibility(View.GONE);
        findViewById(R.id.Button_back).setVisibility(View.GONE);
        findViewById(R.id.Button_post).setVisibility(View.GONE);
        Choices.purge();
    }

    private void setVisibilityOpen() {
        ableToPost();
        findViewById(R.id.layout_open_type).setVisibility(View.VISIBLE);
    }

    private void setVisibilityChoice() {
        ableToPost();
        findViewById(R.id.layout_choice_type).setVisibility(View.VISIBLE);
    }


    private void removeChoice() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (text.equals("")){
            builder.setTitle("You need to select the option to remove!");
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
            Choices.removeChoice(text);
            text="";
            fragmentList.notifyAdapter();
        }
    }

    private void addChoice() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Option: ");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Choices.addChoice(input.getText().toString());
                fragmentList.notifyAdapter();
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

    private void ableToPost(){
        findViewById(R.id.layout_select_type).setVisibility(View.GONE);
        findViewById(R.id.Button_back).setVisibility(View.VISIBLE);
        findViewById(R.id.Button_post).setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemSelected(Choices i) {
        text=i.getTitle();
    }
}
