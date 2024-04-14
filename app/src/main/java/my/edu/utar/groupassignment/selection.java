package my.edu.utar.groupassignment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

public class selection extends AppCompatActivity {

    Button btnUpload, btnDiscuss;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selection);

        btnUpload=findViewById(R.id.upload);
        btnDiscuss=findViewById(R.id.discuss);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the upload activity
                Intent intent = new Intent(selection.this, UploadPDF.class);
                startActivity(intent);
            }
        });

        btnDiscuss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the upload activity
                Intent intent = new Intent(selection.this, Subject_List.class);
                startActivity(intent);
            }
        });

    }

}
