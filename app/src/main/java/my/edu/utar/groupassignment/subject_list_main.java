package my.edu.utar.groupassignment;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;
import my.edu.utar.groupassignment.databinding.ActivityMainBinding;

public class subject_list_main extends AppCompatActivity {

    ListView subjectlistView;  ////
    ListAdapter listAdapter;
    ArrayList<ListData> dataArrayList = new ArrayList<>();
    ListData listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_list_main);
        subjectlistView = findViewById(R.id.subjectListview);

        int[] imageList = {R.drawable.computer, R.drawable.english, R.drawable.language, R.drawable.maths,R.drawable.computer};
        //int[] nameList = {R.string.DC, R.string.EIT, R.string.PEP, R.string.ME};

        String[] nameList = {"UCCN1004 Data Communications and Networking", "MPU32143 English For Information Technology", "UALF1003 Introduction To French", "MPU33013 Malaysian Economy","Mobile Application Development"};

        for (int i = 0; i < imageList.length; i++) {
            listData = new ListData(nameList[i], imageList[i]);
            dataArrayList.add(listData);
        }
        listAdapter = new ListAdapter(subject_list_main.this, dataArrayList);

        subjectlistView.setAdapter(listAdapter);
        subjectlistView.setClickable(true);


        subjectlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("SubjectName", "Selected subject: " + nameList[i]);
                Intent intent = new Intent(subject_list_main.this, semester_year_list.class);
                intent.putExtra("name", nameList[i]);
                startActivity(intent);
            }
        });

    }
}