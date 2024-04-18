package my.edu.utar.groupassignment;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.EmojiTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class semester_year_list extends AppCompatActivity {

    DatabaseReference mDatabase;

    ListView semesterListView;

    SemesterAdapter listAdapter;


    ArrayList<SemesterData> semesterDataArrayList = new ArrayList<>();
    String subjectName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semester_year_list);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        subjectName = getIntent().getStringExtra("name");
        Log.d("SubjectName", "Subject Name: " + subjectName);

       semesterListView = findViewById(R.id.semesterListview);



        if (subjectName != null) {
            retrieveSemesterDataForSubject(subjectName);
        } else {
            showErrorAndFinish("Subject name is null");
        }

    }


    private void retrieveSemesterDataForSubject(String subjectName) {
        semesterDataArrayList.clear();

        if (subjectName.equals(getString(R.string.DC))) {
            semesterDataArrayList.add(new SemesterData("September", 2023));
            semesterDataArrayList.add(new SemesterData("May", 2023));
            semesterDataArrayList.add(new SemesterData("September", 2022));
            semesterDataArrayList.add(new SemesterData("May", 2022));
        } else if (subjectName.equals(getString(R.string.EIT))) {
            semesterDataArrayList.add(new SemesterData("May", 2022));
            semesterDataArrayList.add(new SemesterData("December", 2021));
            semesterDataArrayList.add(new SemesterData("September", 2021));
        } else if (subjectName.equals(getString(R.string.FR))) {
            semesterDataArrayList.add(new SemesterData("May", 2022));
            semesterDataArrayList.add(new SemesterData("December", 2021));
            semesterDataArrayList.add(new SemesterData("September", 2021));
        } else if (subjectName.equals(getString(R.string.ME))) {
            semesterDataArrayList.add(new SemesterData("December", 2023));
            semesterDataArrayList.add(new SemesterData("May", 2023));
            semesterDataArrayList.add(new SemesterData("December", 2022));
            semesterDataArrayList.add(new SemesterData("May", 2022));
        }

        // Create adapter and set it to ListView
        //listAdapter = new SemesterAdapter(getApplicationContext(), semesterDataArrayList);
        listAdapter = new SemesterAdapter(semester_year_list.this, semesterDataArrayList);
        semesterListView.setAdapter(listAdapter);

        // Set item click listener
        semesterListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SemesterData selectedSemester = semesterDataArrayList.get(i);
                Intent intent = new Intent(semester_year_list.this, past_year_discussion.class);
                intent.putExtra("pdfFileName", getPDFFileName(selectedSemester));
                startActivity(intent);
            }
        });
    }

    private String getPDFFileName(SemesterData semesterData) {

        // Logic to determine the PDF file name based on the semester and year
        return subjectName + "_" + semesterData.semester + "_" + semesterData.year + ".pdf";
    }

    private void showErrorAndFinish(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        finish();
    }
}
