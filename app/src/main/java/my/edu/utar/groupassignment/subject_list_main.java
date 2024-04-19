package my.edu.utar.groupassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import my.edu.utar.groupassignment.databinding.ActivityMainBinding;

public class subject_list_main extends AppCompatActivity {

    ListView subjectlistView;
    ListAdapter listAdapter;
    ArrayList<ListData> dataArrayList = new ArrayList<>();
    ListData listData;
    DatabaseReference mDatabase;

    TextView currentDirectory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_list_main);
        subjectlistView = findViewById(R.id.subjectListview);
        currentDirectory = findViewById(R.id.currentDirectory);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Past Year Paper");

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot categorySnapshot : snapshot.getChildren()) {
                    String subjectName = categorySnapshot.getKey();

                    listData = new ListData(subjectName);
                    dataArrayList.add(listData);
                }
                    listAdapter = new ListAdapter(subject_list_main.this, dataArrayList);

                    subjectlistView.setAdapter(listAdapter);
                    subjectlistView.setClickable(true);

                    subjectlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            String subjectName = dataArrayList.get(i).getName(); // Get the clicked subject
                            Log.d("SubjectName", "Selected subject: " + subjectName );
                            Intent intent = new Intent(subject_list_main.this, semester_year_list.class);
                            intent.putExtra("name",subjectName);
                            startActivity(intent);
                        }
                    });
                // Set current directory after adding all items
                if (!dataArrayList.isEmpty()) {
                    currentDirectory.setText(dataArrayList.get(0).getName());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.search) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}