package com.example.itrasher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class act_ajuda extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_ajuda);



    }

    public void telaAjuda2(View view) {
        Intent intent = new Intent(act_ajuda.this, act_ajuda2.class);
        startActivity(intent);
        finish();
    }
}
