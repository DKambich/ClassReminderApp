package edu.purdue.dkambich.classreminderapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InputActivity extends AppCompatActivity {
    EditText name, startTime, location;
    Button infobutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        name = (EditText) findViewById(R.id.classNameInput);
        startTime = (EditText) findViewById(R.id.classLocationInput);
        location = (EditText) findViewById(R.id.classStartTimeInput);

        infobutton = (Button) findViewById(R.id.printInfo);

        infobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getBaseContext(), name.getText().toString() + ". " + startTime.getText().toString() + ". " + location.getText().toString(), Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }
}
