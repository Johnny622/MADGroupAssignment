package my.edu.utar.groupassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Splash screen with animation
        //Switch front page to another page
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this, walk_through.class));
                finish();
            }
        }, 3000);

    }

    public void openCommentsActivity(String postKey) {
        if (postKey != null && !postKey.isEmpty()) {
            Intent intent = new Intent(this, Comments.class);
            intent.putExtra("postkey", postKey);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Post key is missing!", Toast.LENGTH_SHORT).show();
        }
    }

}