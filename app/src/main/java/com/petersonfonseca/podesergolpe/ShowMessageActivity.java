package com.petersonfonseca.podesergolpe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ShowMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_message);

        TextView msmAlert = findViewById(R.id.msm_alert);

        Intent msgFromIntent = getIntent();
        String msg_from = msgFromIntent.getStringExtra("numberTelephone");
        msmAlert.setText("A mensagem recebida de " + msg_from +  " parece ser uma tentativa de golpe. Fique alerta.");
    }
}