package com.example.itrasher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class act_splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread thread = new Thread()
        {

            @Override
            public void run()
            {
                try
                {
                    sleep(2000);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                } finally
                {

                    Intent mainIntent = new Intent(act_splash.this, act_login.class);
                    startActivity(mainIntent);
                    finish();
                }
            }
        };
        thread.start();


    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }


    }

