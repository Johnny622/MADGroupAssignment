package my.edu.utar.groupassignment;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;
public class subject_list_main extends AppCompatActivity {

    ListView listView;
    ListAdapter listAdapter;
    ArrayList<ListData> dataArrayList = new ArrayList<>();
    ListData listData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_list_main);
        listView = findViewById(R.id.listview);

        int[] imageList = {R.drawable.computer, R.drawable.english, R.drawable.etika, R.drawable.maths};
        //int [] pdfList ={};
        int[] nameList = {R.string.DC, R.string.EIT, R.string.PEP, R.string.ME};

        for (int i = 0; i < imageList.length; i++){
            listData = new ListData(nameList[i], imageList[i]);
            dataArrayList.add(listData);
        }
        listAdapter = new ListAdapter(subject_list_main.this, dataArrayList);

        listView.setAdapter(listAdapter);
        listView.setClickable(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(subject_list_main.this, past_year_discussion.class);
//                intent.putExtra("name", nameList[i]);
//                intent.putExtra("image", imageList[i]);
                startActivity(intent);
            }
        });
    }
}