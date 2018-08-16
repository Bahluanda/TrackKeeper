package com.example.barbara.trackkeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class Wait extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);
        setTitle("  ");

        Intent status = getIntent();
        boolean connection = status.getBooleanExtra("Status", false);

        if (connection == true){
            delay(500);
        }else {
            Intent menu = new Intent(Wait.this, MenuConnect.class);
            startActivity(menu);
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
    }
}
