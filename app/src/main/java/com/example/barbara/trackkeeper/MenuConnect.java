package com.example.barbara.trackkeeper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuConnect extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_connect);

        Button intensity = (Button) findViewById(R.id.estdis);
        Button connect = (Button) findViewById(R.id.conectar);

        intensity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Intent DisIntent = new Intent(MenuConnect.this, Distancy.class);
                startActivity(DisIntent);
            }
        });

        connect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Intent mainA = new Intent(MenuConnect.this, MainActivity.class);
                startActivity(mainA);
                finish();
            }
        });
    }
}
