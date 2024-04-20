package my.edu.utar.groupassignment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;
import com.vanniktech.emoji.EmojiPopup;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import my.edu.utar.groupassignment.Adapters.DiscussionAdapter;

public class past_year_discussion extends AppCompatActivity {

    DatabaseReference mDatabase;
    StorageReference storageReference;
    Button postBtn;
    EditText discussionText;
    ImageView btEmoji;
    List<pdfClass> uploadPdf;
    String subjectName, subjectYear, subjectIntake;

    private ListView listViewDiscussions;
    private DiscussionAdapter adapter;
    private List<PaperDiscussion> discussionList;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // Get the currently logged-in user
    FirebaseAuth auth;

    String TAG = "PastYearDiscussion";

    private final OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_year_discussion);

        uploadPdf = new ArrayList<>();

        subjectName = getIntent().getStringExtra("name");
        subjectYear = getIntent().getStringExtra("year");
        subjectIntake = getIntent().getStringExtra("month");

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Past Year Paper").child(subjectName).child(subjectYear).child(subjectIntake);

        storageReference = FirebaseStorage.getInstance().getReference();

        try {
            retrievePdfUrlFromDatabase();
        } catch (Exception ex) {
            Log.d("Error ", ex.getMessage());
        }

        postBtn = findViewById(R.id.postBtn);
        discussionText = findViewById(R.id.commentEditText);
        btEmoji = findViewById(R.id.bt_emoji);
        listViewDiscussions = findViewById(R.id.listViewDiscussions);


        discussionList = new ArrayList<>();
        adapter = new DiscussionAdapter(this, R.layout.activity_past_year_discussion, discussionList);
        listViewDiscussions.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        retrieveDiscussions();
        //emoji popup
        EmojiPopup popup = EmojiPopup.Builder.fromRootView(
                findViewById(R.id.listViewDiscussions)
        ).build(discussionText);

        btEmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toggle between text and emoji
                popup.toggle();

            }
        });

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Retrive and add The discussion comment
                addDiscussion();

                addEmojiText();
                retrieveDiscussions();
            }
        });



        onBackPressedDispatcher.addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(past_year_discussion.this, home_page.class);
                startActivity(intent);

                finish();
            }
        });
    }

    private void addEmojiText() {
        String username = auth.getCurrentUser().getDisplayName(); // Get the user's name from their Google profile
        //String userId = user.getUid();
        String text = discussionText.getText().toString();
        if (!text.isEmpty()) {
            discussionList.add(new PaperDiscussion(username, text));  // Assuming PaperDiscussion can be initialized this way
            adapter.notifyDataSetChanged();  // Notify the adapter that the data has changed
            discussionText.getText().clear();
        }
    }

    private void addDiscussion() {

        auth = FirebaseAuth.getInstance();
        String comment = discussionText.getText().toString();

        if (user != null && !comment.isEmpty()) {
            String username = auth.getCurrentUser().getDisplayName(); // Get the user's name from their Google profile

            if (username == null || username.isEmpty()) {
                username = "Anonymous"; // Fallback username if name is not available
            }

            PaperDiscussion newDiscussion = new PaperDiscussion(username, comment);

            // Get a reference to the 'Comments' node and push the new discussion
            DatabaseReference commentsRef = mDatabase.child("Comments").push();
            commentsRef.setValue(newDiscussion)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(past_year_discussion.this, "Comment added!", Toast.LENGTH_SHORT).show();
                        discussionText.setText(""); // Clear the text field after posting
                        retrieveDiscussions(); // Optionally retrieve discussions to update the list
                    })
                    .addOnFailureListener(e -> Toast.makeText(past_year_discussion.this, "Failed to add comment: " + e.getMessage(), Toast.LENGTH_LONG).show());
        } else {
            Toast.makeText(this, "User is not authenticated.", Toast.LENGTH_SHORT).show();
        }

    }
    private void retrieveDiscussions() {
        DatabaseReference discussionRef = mDatabase.child("Comments");
        discussionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                discussionList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    PaperDiscussion discussion = snap.getValue(PaperDiscussion.class);
                    if (discussion != null) {
                        discussionList.add(discussion);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(past_year_discussion.this, "Error loading discussions: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void retrievePdfUrlFromDatabase() {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (!snapshot.getKey().equals("Comments")) {
                            // Retrieve the key
                            String key = snapshot.getKey();
                            Log.d(TAG, "Key: " + key);
                            // Now you can use the key as needed

                            mDatabase.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                        String url = snapshot1.child("url").getValue(String.class);
                                        if (url != null) {
                                            Log.d("URL", url);
                                            Toast.makeText(past_year_discussion.this, "Url : " + url, Toast.LENGTH_SHORT).show();
                                            downloadAndDisplayPdf(url);
                                        }
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                } catch (Exception ex) {
                    Log.d("Error", ex.getMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Failed to retrieve PDF URLs: " + databaseError.getMessage());
            }
        });
    }

    private void downloadAndDisplayPdf(String pdfUrl) {
        // Download PDF file from Firebase Storage
        try {
            PDFView pdfView = findViewById(R.id.pdfView);
            File localFile = File.createTempFile("temp", "pdf");
            File cacheDir = getCacheDir();
            Log.d("File dir",cacheDir.toString());

            String[] parts = pdfUrl.split("/");
            String lastPart = parts[parts.length - 1];
            String[] filenameParts = lastPart.split("\\?");
            String Fullfilename = filenameParts[0];
            String [] filename = Fullfilename.split("%2F");

            Log.d("Current Path is",filename[1]);

            // Construct the full path to the PDF file in Firebase Storage
            String storagePath = "Uploads/" + filename[1];
//            String storagePath = "Uploads/";

            storageReference.child(storagePath).getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            // PDF file has been downloaded successfully
                            // Display the downloaded PDF file using PDFView
                            pdfView.fromFile(localFile).load();

                            Log.d("Success get Pdf","Success!!");                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "Failed to download PDF: " + e.getMessage());
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Error creating temporary file: " + e.getMessage());
        }
    }
}