package my.edu.utar.groupassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView switchPageTesting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        switchPageTesting = findViewById(R.id.switchPage);

        switchPageTesting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pyDiscussion = new Intent(MainActivity.this, past_year_discussion.class);
                pyDiscussion.putExtra("py", "PY Discussion");
                startActivity(pyDiscussion);
            }
        });

    }
}