package com.example.lenovo.dra;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.lenovo.dra.Activities.LoginActivity;

public class IntroActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    Button skip;
    Button next;
    TextView textView;
    private int[] layouts;
    private FirstTime firstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firstTime = new FirstTime(this);
        if(!firstTime.isFirstTimeLaunch()){
            LaunchHomeScreen();
            finish();
        }

        setContentView(R.layout.welcome_layout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        mViewPager.setAdapter(new IntroAdapter(getSupportFragmentManager()));

        mViewPager.setPageTransformer(false, new IntroPageTransformer());
        skip = (Button) findViewById(R.id.btn_skip);
        next = (Button) findViewById(R.id.btn_next);
        layouts = new int[]{R.layout.welcom_frag1, R.layout.welcome_frag2, R.layout.welcome_frag3};
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 LaunchHomeScreen();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int current = getItem(+1);
                if (current < layouts.length) {
                    mViewPager.setCurrentItem(current);
                } else {
                    LaunchHomeScreen();
                }

            }
        });
    }
    private int getItem(int i) {
        return mViewPager.getCurrentItem() + i;
    }
    private void LaunchHomeScreen(){
        firstTime.setFirstTimeLaunch(false);
        startActivity(new Intent(IntroActivity.this, LoginActivity.class));
        finish();
    }

}

