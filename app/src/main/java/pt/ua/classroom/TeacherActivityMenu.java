package pt.ua.classroom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class TeacherActivityMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_menu);

        TextView tv= (TextView) findViewById(R.id.classeName);
        tv.setText(Database.getClasseName());
    }
}
