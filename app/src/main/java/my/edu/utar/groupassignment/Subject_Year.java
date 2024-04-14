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

public class Subject_Year extends AppCompatActivity {

    DatabaseReference mDatabase;
    TextView currentDirectory;
    String selectedSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_year);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        currentDirectory = findViewById(R.id.currentDirectory);

        // Get the subject value passed from the previous activity
        selectedSubject = getIntent().getStringExtra("currentSubject");
        currentDirectory.setText(selectedSubject);

        retrieveAndCreateButtons(selectedSubject);
    }

    private void retrieveAndCreateButtons(String selectedSubject) {
        mDatabase.child(selectedSubject).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                LinearLayout layout = findViewById(R.id.buttons_layout);
                for(DataSnapshot yearSnapshot : dataSnapshot.getChildren()){
                    String year = yearSnapshot.getKey();

                    Button button = new Button(Subject_Year.this);
                    button.setText(year);
                    layout.addView(button);

                    // Add click listener to each button
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Create an intent to navigate to Subject_year.java
                            Intent intent = new Intent(Subject_Year.this, Subject_Trimester.class);
                            // Pass the subject value to the new activity
                            intent.putExtra("currentSubject", selectedSubject);
                            intent.putExtra("currentYear",year);
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