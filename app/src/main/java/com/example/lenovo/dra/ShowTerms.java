package com.example.lenovo.dra;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ShowTerms extends AppCompatActivity {
    TextView txtTitleTerms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_terms);
        txtTitleTerms = (TextView) findViewById(R.id.txtTitleTerm);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/barbar.ttf");
        txtTitleTerms.setTypeface(custom_font);
    }
}
