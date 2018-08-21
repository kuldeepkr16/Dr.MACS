package com.example.lenovo.dra.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.lenovo.dra.R;

public class OnClickingNoti extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_clicking_noti);

        TextView txtNotiData = (TextView) findViewById(R.id.txtNotiData);

        String dataMessage = getIntent().getStringExtra("dataMessage");
        String fromName = getIntent().getStringExtra("fromName");

        txtNotiData.setText(" FROM: " + fromName + "\n" + "MESSAGE: "+dataMessage);

        //back btn will take to main activity
        Button btnNotiBack = (Button) findViewById(R.id.btnNotiBack);
        btnNotiBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OnClickingNoti.this, MainActivity.class));
                finish();
            }
        });
    }
}
