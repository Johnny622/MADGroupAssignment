package my.edu.utar.groupassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Subject_List extends AppCompatActivity {

    DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_list);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        retrieveAndCreateButtons();
    }

    private void retrieveAndCreateButtons() {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                LinearLayout layout = findViewById(R.id.buttons_layout);

                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    String subjectName = categorySnapshot.getKey();

                    Button button = new Button(Subject_List.this);
                    button.setText(subjectName);
                    layout.addView(button);

                    // Add click listener to each button
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Create an intent to navigate to Subject_year.java
                            Intent intent = new Intent(Subject_List.this, Subject_Year.class);
                            // Pass the subject value to the new activity
                            intent.putExtra("currentSubject", subjectName);
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }
}