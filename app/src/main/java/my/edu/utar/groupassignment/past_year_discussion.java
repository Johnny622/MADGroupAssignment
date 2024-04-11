package my.edu.utar.groupassignment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.github.barteksc.pdfviewer.PDFView;


public class past_year_discussion extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_year_discussion);

        // Retrieve the file name from the intent extras
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("pdfFileName")) {
            String pdfFileName = intent.getStringExtra("pdfFileName");

            // Load the PDF file based on the file name
            loadPDF(pdfFileName);
        }
    }

    private void loadPDF(String pdfFileName) {
        PDFView pdfView = findViewById(R.id.pdfView);

        // Load the PDF file from the assets folder
        pdfView.fromAsset(pdfFileName).load();
    }
}
