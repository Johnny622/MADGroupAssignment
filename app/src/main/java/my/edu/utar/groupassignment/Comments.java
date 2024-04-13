package my.edu.utar.groupassignment;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import my.edu.utar.groupassignment.Adapters.CommentAdapter;
import my.edu.utar.groupassignment.Model.Comment;

public class Comments extends AppCompatActivity {


    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private CommentAdapter adapter;
    private List<Comment> comments;
    String postKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize your adapter
        List<Comment> comments = new ArrayList<>();  // This would normally come from a data source
        CommentAdapter adapter = new CommentAdapter(comments);
        recyclerView.setAdapter(adapter);

        EditText etComment = findViewById(R.id.comment);
        ImageButton btnSendComment = findViewById(R.id.send);


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Comment");

        //Intent intent = getIntent();
        postKey = getIntent().getStringExtra("postkey");
        if (postKey == null) {
            Toast.makeText(this, "Error: No post key provided", Toast.LENGTH_LONG).show();
            return; // Exit the method if no post key found
        }



        btnSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnSendComment.setVisibility(View.INVISIBLE);
                String content = etComment.getText().toString();
                if(content.isEmpty()){
                    showMessage("Comment cannot be empty");
                    return; // do not proceed with adding the comment
                }

                DatabaseReference commentRef = firebaseDatabase.getReference("Comment").child(postKey).push();
                commentRef.setValue(content)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(Comments.this, "Comment added successfully", Toast.LENGTH_SHORT).show();
                            comments.add(new Comment(content));
                            adapter.notifyDataSetChanged();
                            etComment.setText(""); // Clear the text field after submitting
                        }).addOnFailureListener(e -> Toast.makeText(Comments.this, "Failed to add comment: "+ e.getMessage(), Toast.LENGTH_LONG).show());
            }
        });

        Intent intent = new Intent(Comments.this, Comments.class);
        intent.putExtra("postkey", postKey);
        startActivity(intent);
    }



    private void showMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    public static void openCommentsActivity(AppCompatActivity activity, String postKey) {
        if (postKey != null && !postKey.isEmpty()) {
            Intent intent = new Intent(activity, Comments.class);
            intent.putExtra("postkey", postKey);
            activity.startActivity(intent);
        } else {
            Toast.makeText(activity, "Post key is missing!", Toast.LENGTH_SHORT).show();
        }
    }
}