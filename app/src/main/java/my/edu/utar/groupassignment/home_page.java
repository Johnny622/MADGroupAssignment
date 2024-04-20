package my.edu.utar.groupassignment;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class home_page extends AppCompatActivity {

    CardView cardViewSubject,cardViewUpload;

    private final OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        cardViewSubject =findViewById(R.id.past_year_discussion);

        cardViewSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(home_page.this, subject_list_main.class);
                startActivity(intent);
//                finish();
            }
        });

        cardViewUpload =findViewById(R.id.upload_past_year);

        cardViewUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(home_page.this, UploadPDF.class);
                startActivity(intent);
//                finish();
            }
        });

        onBackPressedDispatcher.addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(home_page.this, Login.class);
                startActivity(intent);

                finish();
            }
        });
    }
}