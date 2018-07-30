package com.example.steven.macedittext;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private MacEditText macEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        macEditText = findViewById(R.id.mac_edit);
    }

    public void onClick(View view) {
        String macAdd = macEditText.getMacAddress();
        if(macAdd==null){
            Toast.makeText(this, "This is not a valid Mac Address", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, macAdd, Toast.LENGTH_SHORT).show();
        }
    }
}
