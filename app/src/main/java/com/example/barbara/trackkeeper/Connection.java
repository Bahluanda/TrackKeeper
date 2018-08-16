package com.example.barbara.trackkeeper;

import android.app.Activity;
import android.os.Bundle;

import static com.example.barbara.trackkeeper.MainActivity.retorna;

public class Connection extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setResult(RESULT_OK,retorna);

        finish();
    }

}
