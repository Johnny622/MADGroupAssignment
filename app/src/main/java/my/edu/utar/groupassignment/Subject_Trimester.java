package my.edu.utar.groupassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Subject_Trimester extends AppCompatActivity {

    DatabaseReference mDatabase;
    TextView currentDirectory;
    String selectedSubject,selectedYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_trimester);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        currentDirectory = findViewById(R.id.currentDirectory);

        // Get the subject value passed from the previous activity
        selectedSubject = getIntent().getStringExtra("currentSubject");
        selectedYear=getIntent().getStringExtra("currentYear");
        currentDirectory.setText(selectedSubject+" > "+selectedYear);

        retrieveAndCreateButtons(selectedSubject,selectedYear);
    }

    private void retrieveAndCreateButtons(String selectedSubject, String selectedYear) {
        mDatabase.child(selectedSubject).child(selectedYear).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                LinearLayout layout = findViewById(R.id.buttons_layout);
                for(DataSnapshot trimesterSnapshot : dataSnapshot.getChildren()){
                    String trimester = trimesterSnapshot.getKey();

                    Button button= new Button(Subject_Trimester.this);
                    button.setText(trimester);
                    layout.addView(button);

                    button.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Subject_Trimester.this,past_year_discussion.class);

                            intent.putExtra("currentSubject",selectedSubject);
                            intent.putExtra("currentYear",selectedYear);
                            intent.putExtra("currentTrimester",trimester);

                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });
    }
}