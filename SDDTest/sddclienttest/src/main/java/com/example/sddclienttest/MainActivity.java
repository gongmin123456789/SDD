package com.example.sddclienttest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.gm.sdd.client.SDDClient;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Button button = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initContent();
    }

    private void initContent() {
        Log.i(TAG, "<initContent> start");

        button = (Button) findViewById(R.id.okButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "<button:onClick> start");

                SDDClient sddClient = new SDDClient(getApplicationContext(), "gongmin");
                sddClient.start();
            }
        });
    }
}
