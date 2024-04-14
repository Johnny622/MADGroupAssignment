package my.edu.utar.groupassignment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class past_year_discussion extends AppCompatActivity {

    DatabaseReference mDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_year_discussion);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Get subject name, year, and month from previous activity
        String subjectName = getIntent().getStringExtra("currentSubject");
        String year = getIntent().getStringExtra("currentYear");
        String trimester = getIntent().getStringExtra("currentTrimester");

        // Set current directory text
        TextView currentDirectory = findViewById(R.id.currentDirectory);
        currentDirectory.setText(subjectName + " > " + year + " > " + trimester);

        // Retrieve URL from Firebase and display it
        retrieveURL(subjectName, year, trimester);

        // Retrieve the file name from the intent extras
//        Intent intent = getIntent();
//        if (intent != null && intent.hasExtra("pdfFileName")) {
//            String pdfFileName = intent.getStringExtra("pdfFileName");
//
//            // Load the PDF file based on the file name
//            loadPDF(pdfFileName);
//        }
    }
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });
    }

//    private void loadPDF(String pdfFileName) {
//        PDFView pdfView = findViewById(R.id.pdfView);
//
//        // Load the PDF file from the assets folder
////        pdfView.fromAsset(pdfFileName).load();
//    }

    private void loadPDF(String url){
        PDFView pdfView = findViewById(R.id.pdfView);

        pdfView.fromUri(Uri.parse(url)).load();
    }
}
