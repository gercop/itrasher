package com.example.itrasher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class act_ajuda2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_ajuda2);
    }

    public void telaAjuda1(View view) {
        Intent intent = new Intent(act_ajuda2.this, act_ajuda.class);
        startActivity(intent);
        finish();
    }

    public void telaAjuda3(View view) {
        Intent intent = new Intent(act_ajuda2.this, act_ajuda3.class);
        startActivity(intent);
        finish();
    }
}
