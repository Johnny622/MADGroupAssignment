package my.edu.utar.groupassignment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;


public class past_year_discussion extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_year_discussion);

        PDFView pdfView = findViewById(R.id.pdfView);

        pdfView.fromAsset("MPU32143_MAY2022.pdf").load();

    }
}