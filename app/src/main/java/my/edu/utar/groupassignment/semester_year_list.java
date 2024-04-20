package my.edu.utar.groupassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class semester_year_list extends AppCompatActivity {

    DatabaseReference mDatabase;
    ListView yearListView;
    YearAdapter yearAdapter;
    YearData yearData;
    ArrayList<YearData> yearArrayList = new ArrayList<>();
    String subjectName;

    TextView currentDirectory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semester_year_list);
        yearListView = findViewById(R.id.yearListview);
        currentDirectory = findViewById(R.id.currentDirectory);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Past Year Paper");

        subjectName = getIntent().getStringExtra("name");

        currentDirectory.setText(subjectName + " > ");

        Log.d("SubjectName", "Subject Name: " + subjectName);

        mDatabase.child(subjectName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot yearSnapshot : dataSnapshot.getChildren()) {
                    String year = yearSnapshot.getKey();

                    yearData = new YearData(year);
                    yearArrayList.add(yearData);
                }

                yearAdapter = new YearAdapter(semester_year_list.this, yearArrayList);

                yearListView.setAdapter(yearAdapter);
                yearListView.setClickable(true);

                yearListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String subjectYear = yearArrayList.get(i).getYear(); // Get the clicked subject
                        Log.d("Current Directory", subjectName + " > " + subjectYear );
                        Intent intent = new Intent(semester_year_list.this, semester_intake_list.class);
                        intent.putExtra("name", subjectName);
                        intent.putExtra("year", subjectYear);
                        startActivity(intent);
                    }

                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
                Log.e("FirebaseDatabase", "Database operation canceled: " + databaseError.getMessage());
            }
        });

        //not needed?
//        if (subjectName != null) {
//            retrieveSemesterDataForSubject(subjectName);
//        } else {
//            showErrorAndFinish("Subject name is null");
//        }

    }




//    private void showErrorAndFinish(String errorMessage) {
//        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
//        finish();
//    }

}
