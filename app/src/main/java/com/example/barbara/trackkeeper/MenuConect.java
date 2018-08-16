package com.example.barbara.trackkeeper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuConect extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_conect);

        Button distancia = (Button) findViewById(R.id.estdis);
        Button conec = (Button) findViewById(R.id.conectar);

        distancia.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Intent DisIntent = new Intent(MenuConect.this, Distancy.class);
                startActivity(DisIntent);

            }
        });

        conec.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Intent mainA = new Intent(MenuConect.this, MainActivity.class);
                startActivity(mainA);
                finish();
            }
        });
    }
}
