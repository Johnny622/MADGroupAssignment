package my.edu.utar.groupassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class semester_intake_list extends AppCompatActivity {

    DatabaseReference mDatabase;
    ListView intakeListView;
    IntakeAdapter intakeAdapter;
    IntakeData intakeData;
    ArrayList<IntakeData> intakeArrayList = new ArrayList<>();
    String subjectName, subjectYear;

    TextView currentDirectory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semester_intake_list);
        intakeListView = findViewById(R.id.intakeListview);
        currentDirectory = findViewById(R.id.currentDirectory);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Past Year Paper");

        subjectName = getIntent().getStringExtra("name");
        subjectYear = getIntent().getStringExtra("year");

        mDatabase.child(subjectName).child(subjectYear).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot trimesterSnapshot : snapshot.getChildren()) {
                    String month = trimesterSnapshot.getKey();

                    intakeData = new IntakeData(month);
                    intakeArrayList.add(intakeData);
                }
                intakeAdapter = new IntakeAdapter(semester_intake_list.this, intakeArrayList);

                intakeListView.setAdapter(intakeAdapter);
                intakeListView.setClickable(true);

                intakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String subjectIntake = intakeArrayList.get(i).getMonth(); // Get the clicked subject
                        Log.d("SubjectName", "Selected subject: " + subjectName);
                        Intent intent = new Intent(semester_intake_list.this, past_year_discussion.class);
                        intent.putExtra("name", subjectName);
                        intent.putExtra("year", subjectYear);
                        intent.putExtra("month", subjectIntake);
                        startActivity(intent);
                    }

                });
                if (!intakeArrayList.isEmpty()) {
                    currentDirectory.setText(subjectName + " > " + subjectYear + " > " + intakeArrayList.get(0).getMonth());
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}