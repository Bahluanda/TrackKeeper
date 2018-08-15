package com.example.barbara.trackkeeper;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;


public class Alertamenu extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTheme(android.R.style.Theme_Material_Light_Dialog);
        setTitle("Alertar:");
        setTheme(android.R.style.Theme_DeviceDefault_Light_Dialog);


        ArrayAdapter<String> ArrayMenu = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);

        ArrayMenu.add("Alerta Perda de Conexão");
        ArrayMenu.add("Distância Estimada");

        setListAdapter(ArrayMenu);
    }

}
