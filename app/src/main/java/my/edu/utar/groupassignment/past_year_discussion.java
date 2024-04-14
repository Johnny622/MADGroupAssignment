package my.edu.utar.groupassignment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import my.edu.utar.groupassignment.Adapters.DiscussionAdapter;


public class past_year_discussion extends AppCompatActivity {

    DatabaseReference mDatabase;
    String currentDir, paperID, selectedSubject, selectedYear, selectedTrimester;
    TextView currentDirectory;
    Button postBtn;
    EditText discussionText;
    List<PaperDiscussion> discussionList;
    ArrayAdapter<PaperDiscussion> adapter;
    ListView listViewDiscussions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_year_discussion);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Get subject name, year, and month from previous activity
        selectedSubject = getIntent().getStringExtra("currentSubject");
        selectedYear = getIntent().getStringExtra("currentYear");
        selectedTrimester = getIntent().getStringExtra("currentTrimester");

        // Assign the xml view to java
        listViewDiscussions = findViewById(R.id.listViewDiscussions);
        discussionList = new ArrayList<>();
        adapter = new DiscussionAdapter(this, R.layout.activity_past_year_discussion, discussionList);
        listViewDiscussions.setAdapter(adapter);

        // Set current directory text
        currentDirectory = findViewById(R.id.currentDirectory);
        currentDir = selectedSubject + " > " + selectedYear + " > " + selectedTrimester;
        currentDirectory.setText(currentDir);

        // Retrieve URL from Firebase and display it
        retrieveURL(selectedSubject, selectedYear, selectedTrimester);

        postBtn = findViewById(R.id.postBtn);
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Retrive and add The discussion comment
                addDiscussion(selectedSubject, selectedYear, selectedTrimester, paperID);
            }
        });

        // Retrieve discussions from Firebase
//        retrieveDiscussions();

    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        // Initialize ListView and adapter
//        discussionList = new ArrayList<>();
//        adapter = new DiscussionAdapter(this, R.layout.activity_past_year_discussion, discussionList);
//        listViewDiscussions.setAdapter(adapter);
//
//        // Retrieve and display discussions
//        retrieveDiscussions();
//    }

    private void retrieveURL(String subjectName, String year, String month) {
        DatabaseReference databaseRef = mDatabase.child(subjectName).child(year).child(month);

        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String url = dataSnapshot.child("url").getValue(String.class);
                    if (url != null) {
                        loadPDF(url);
                    } else {
                        // Handle case where URL is not found
                    }
                } else {
                    // Handle case where the subject/year/month node does not exist
                }

                for (DataSnapshot idSnapshot : dataSnapshot.getChildren()) {
                    paperID = idSnapshot.getKey();
                    currentDir = currentDir + "[ " + paperID + " ]";

                    currentDirectory.setText(currentDir);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });
    }

    private void loadPDF(String url) {
        PDFView pdfView = findViewById(R.id.pdfView);

        pdfView.fromUri(Uri.parse(url)).load();
    }

    private void addDiscussion(String selectedSubject, String selectedYear, String selectedTrimester, String paperID) {
        DatabaseReference discussionRef = FirebaseDatabase.getInstance().getReference()
                .child(selectedSubject).child(selectedYear).child(selectedTrimester).child(paperID);

        discussionText = findViewById(R.id.commentEditText);

        // Generate a unique key for the discussion entry
        String discussionId = discussionRef.push().getKey();

        currentDir = currentDir + "[ " + paperID + " ]";

        currentDirectory.setText(currentDir);

        // Create a HashMap to store the discussion data
        HashMap<String, Object> discussionData = new HashMap<>();
        discussionData.put("user", "User1"); // Set the user (you can change it dynamically)
        discussionData.put("discussion", discussionText.getText().toString());

        discussionRef.child("Discussions").child(discussionId).setValue(discussionData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Discussion added successfully
                        Toast.makeText(past_year_discussion.this, "Discussion added successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to add discussion
                        Toast.makeText(past_year_discussion.this, "Failed to add discussion: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void retrieveDiscussions(){
        DatabaseReference discussionsRef = FirebaseDatabase.getInstance().getReference()
                .child(selectedSubject)
                .child(selectedYear)
                .child(selectedTrimester)
                .child(paperID)
                .child("Discussions");

        discussionsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                discussionList.clear();
                for (DataSnapshot discussionSnapshot : dataSnapshot.getChildren()) {
                    String discussionText = discussionSnapshot.child("discussion").getValue(String.class);
                    String user = discussionSnapshot.child("user").getValue(String.class);
                   PaperDiscussion discussion = new PaperDiscussion(user,discussionText);
                   discussionList.add(discussion);
                }
                adapter.notifyDataSetChanged();

                // Create an adapter and set it to the ListView
//                DiscussionAdapter adapter = new DiscussionAdapter(past_year_discussion.this, discussionsList);
//                listViewDiscussions.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }
}
