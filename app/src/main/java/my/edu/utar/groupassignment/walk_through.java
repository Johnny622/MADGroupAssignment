package my.edu.utar.groupassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import javax.security.auth.Subject;

import my.edu.utar.groupassignment.Adapters.OnBoardingAdapter;
import my.edu.utar.groupassignment.Helpers.SaveState;


public class walk_through extends AppCompatActivity {

    CardView nextCard;
    LinearLayout dotsLayout;
    ViewPager viewPager;

    TextView[] dots;
    int currentPosition;

    SaveState saveState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_through);

        nextCard = findViewById(R.id.nextCard);
        dotsLayout = findViewById(R.id.dotsLayout);
        viewPager = findViewById(R.id.slider);
        dotsFunction(0);
        saveState = new SaveState(walk_through.this, "0B");
        if (saveState.getState() == 1) {

           // Intent i = new Intent(walk_through.this,Login.class);

            Intent i = new Intent(walk_through.this, Login.class);

//            Intent i = new Intent(walk_through.this, Login.class);

            startActivity(i);
            finish();
        }

        OnBoardingAdapter adapter = new OnBoardingAdapter(this);
        viewPager.setAdapter(adapter);
        nextCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(currentPosition + 1, true);
            }
        });

        viewPager.setOnPageChangeListener(onPageChangeListener);
    }

    private void dotsFunction(int pos) {
        dots = new TextView[4];
        dotsLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(".");
            dots[i].setTextColor(getColor(R.color.white));
            dots[i].setTextSize(30);

            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[pos].setTextColor(getColor(R.color.teal_700));
            dots[pos].setTextSize(40);
        }
    }

    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            dotsFunction(position);
            currentPosition = position;
            if (currentPosition <= 2) {
                nextCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(currentPosition + 1);
                    }
                });
            } else {
                nextCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        saveState.setState(1);
                        Intent i = new Intent(walk_through.this, Login.class);
                        startActivity(i);
                        finish();
                    }
                });
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}