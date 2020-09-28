package com.example.itrasher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class act_pesquisar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisar);
    }

    public void procura(View view) {
        Intent intent = new Intent(act_pesquisar.this, act_listagem.class);
        startActivity(intent);
    }
}
