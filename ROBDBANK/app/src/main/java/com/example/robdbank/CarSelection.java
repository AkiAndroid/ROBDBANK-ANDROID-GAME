package com.example.ROBDBANK;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class CarSelection extends AppCompatActivity {

    ImageButton car,car1,car2;
    int carNo=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_selection);

        car=findViewById(R.id.car);
        car1=findViewById(R.id.car1);
        car2=findViewById(R.id.car2);


        SharedPreferences sharedPreferences=getSharedPreferences("carid",MODE_PRIVATE);
        final SharedPreferences.Editor editor= sharedPreferences.edit();



        car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              carNo=1;
              editor.putInt("car",1);
              editor.commit();
                Toast.makeText(CarSelection.this, "Car 1 Selected", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),GameMenu.class);
                startActivity(intent);
            }
        });

        car1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            carNo=2;
                editor.putInt("car",2);
                editor.commit();
                Toast.makeText(CarSelection.this, "Car 2 Selected", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),GameMenu.class);
                startActivity(intent);
            }
        });

        car2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carNo=3;
                editor.putInt("car",3);
                editor.commit();
                Toast.makeText(CarSelection.this, "Car 3 Selected", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),GameMenu.class);
                startActivity(intent);

            }
        });


    }
}