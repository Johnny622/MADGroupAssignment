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
    ListAdapter searchListAdapter;
    ArrayList<ListData> dataArrayList = new ArrayList<>();
    ArrayList<ListData> searchArrayList = new ArrayList<>();
    ListData listData;
    DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_list_main);
        subjectlistView = findViewById(R.id.subjectListview);

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
                            Log.d("Current Directory", subjectName );
                            Intent intent = new Intent(subject_list_main.this, semester_year_list.class);
                            intent.putExtra("name",subjectName);
                            startActivity(intent);
                        }
                    });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseDatabase", "Database operation canceled: " + error.getMessage());
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.actionbar_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.search);

        SearchView searchView = (SearchView) menuItem.getActionView();

        if (searchView != null) {
            searchView.setQueryHint("Search here");

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String newText) {
                    subjectlistView.setAdapter(null);
                    searchArrayList.clear();
                    String query = newText.toUpperCase();

                    for (ListData data : dataArrayList) {
                        if (data.getName().contains(query)) {
                            // If the item matches the query, add it to the searchArrayList
                            searchArrayList.add(data);
                        }
                    }

                    searchListAdapter = new ListAdapter(subject_list_main.this, searchArrayList);

                    // Set the adapter to the ListView to display the search results
                    subjectlistView.setAdapter(searchListAdapter);
                    subjectlistView.setClickable(true);

                    subjectlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            String subjectName = searchArrayList.get(i).getName(); // Get the clicked subject
                            Log.d("SubjectName", "Selected subject: " + subjectName );
                            Intent intent = new Intent(subject_list_main.this, semester_year_list.class);
                            intent.putExtra("name",subjectName);
                            startActivity(intent);
                        }
                    });

                    Log.d("onQueryTextSubmit",newText);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    subjectlistView.setAdapter(null);
                    searchArrayList.clear();
                    String query = newText.toUpperCase();

                    for (ListData data : dataArrayList) {
                        if (data.getName().contains(query)) {
                            // If the item matches the query, add it to the searchArrayList
                            searchArrayList.add(data);
                        }
                    }

                    searchListAdapter = new ListAdapter(subject_list_main.this, searchArrayList);

                    // Set the adapter to the ListView to display the search results
                    subjectlistView.setAdapter(searchListAdapter);
                    subjectlistView.setClickable(true);

                    subjectlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            String subjectName = searchArrayList.get(i).getName(); // Get the clicked subject
                            Log.d("SubjectName", "Selected subject: " + subjectName );
                            Intent intent = new Intent(subject_list_main.this, semester_year_list.class);
                            intent.putExtra("name",subjectName);
                            startActivity(intent);
                        }
                    });

                    Log.d("onQueryTextChange",newText);
                    return true;
                }
            });
        }

        return super.onCreateOptionsMenu(menu);
    }

}