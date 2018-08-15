package com.example.barbara.trackkeeper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class Wait extends AppCompatActivity {

    boolean testt = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);
        setTitle("  ");

        Intent status = getIntent();
        boolean conex = status.getBooleanExtra("Status", false);



            if (conex==true){

                    //Toast.makeText(getApplicationContext(), "Test", Toast.LENGTH_SHORT).show();
                    delay(500);



            }else {

                Intent Menu1 = new Intent(Wait.this, MenuConect.class);
                startActivity(Menu1);
                testt=true;
                //break;
            }


        if (testt==true){

            finish();
        }

    }


    public void delay(final int c){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(c);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //sendData("S");           //send
            }
        }, c);

    }



}
