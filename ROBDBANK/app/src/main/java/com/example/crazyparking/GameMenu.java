package com.example.crazyparking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class GameMenu extends AppCompatActivity {

    TextView totalcash;
    ImageButton play_button,car_select_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_menu);

        totalcash=findViewById(R.id.TotalCashtext);

        SharedPreferences sharedPreferences=getSharedPreferences("scoredata",MODE_PRIVATE);
        totalcash.setText("TOTAL CASH : $" + sharedPreferences.getInt("score",0));

        play_button=findViewById(R.id.PlaygameButton);
        car_select_button=findViewById(R.id.Carsbutton);

        play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });

        car_select_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),CarSelection.class);
                startActivity(intent);
            }
        });
    }
}