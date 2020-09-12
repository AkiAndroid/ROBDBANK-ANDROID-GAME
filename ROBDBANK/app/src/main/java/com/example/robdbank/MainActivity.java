package com.example.crazyparking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public  static MediaPlayer cash_sound,engine_reviving_sound,sire_sound,engine_sound,escape_sound;
    public static ImageView escape_window,caught_window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GameBoard gameBoard=findViewById(R.id.game_canvas);
        final TextView ScoreView = findViewById(R.id.scoreView);
        cash_sound= MediaPlayer.create(this,R.raw.coinsound);
        engine_reviving_sound=MediaPlayer.create(this,R.raw.carstartsound);
        sire_sound=MediaPlayer.create(this,R.raw.sirenimage);
        engine_sound=MediaPlayer.create(this,R.raw.enginesound);
        escape_window=findViewById(R.id.escape_window);
        caught_window=findViewById(R.id.caught_window);
        escape_sound=MediaPlayer.create(this,R.raw.escapesound);

        gameBoard.setGamEinfo(new GAMEinfo() {
            @Override
            public void UpdateScore(int Score) {
                ScoreView.setText("Cash : $" + Score);

                SharedPreferences sharedPreferences=getSharedPreferences("scoredata",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("score",sharedPreferences.getInt("score",0) + 100);
                editor.commit();
            }
        });
    }

}