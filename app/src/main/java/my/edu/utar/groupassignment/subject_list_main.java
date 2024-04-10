package my.edu.utar.groupassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;

import my.edu.utar.groupassignment.databinding.ActivityMainBinding;

public class subject_list_main extends AppCompatActivity {

    ActivityMainBinding binding;
    ListAdapter listAdapter;
    ArrayList<ListData> dataArrayList = new ArrayList<>();
    ListData listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        int[] subjectPDFList = {1, 2, 3, 4}; //put pdf here
        String[] nameList = {"MPU32143 English For Information Technology", "UCCM2233 Statistics", "UCCN1004 Data Communications and Networking", "MPU33013 Malaysian Economy"};

        for (int i = 0; i < nameList.length; i++) {
            listData = new ListData(nameList[i], subjectPDFList[i]);
            dataArrayList.add(listData);
        }

        listAdapter = new ListAdapter(subject_list_main.this, dataArrayList);
//        binding.listview.setAdapter(listAdapter);
//        binding.listview.
    }
}