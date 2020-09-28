package com.example.itrasher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class act_login extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;

    public EditText edit_email_local;
    public EditText edit_senha_local;
    public CheckBox cb_salvar_senha_local;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = Conexao.getFirebaseAuth();
        user = Conexao.getFirebaseUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users")
                .child("pJUsJrZgIyUbziqqpq3ds5B8sjz1");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User users = dataSnapshot.getValue(User.class);
                if (users==null) finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                alert("Usuário não encontrado.");
            }
        });
        if (ref == null) finish();

        edit_email_local      = findViewById(R.id.edit_email);
        edit_senha_local      = findViewById(R.id.edit_senha);
        cb_salvar_senha_local = findViewById(R.id.cb_salvar_senha);

        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(act_login.this);
        SharedPreferences.Editor myEditor = myPreferences.edit();
        Boolean salvar = myPreferences.getBoolean("salvar", false);
        if (salvar) {
            String email = myPreferences.getString("email", "unknown");
            String senha = myPreferences.getString("senha", "unknown");
            edit_email_local.setText(email);
            edit_senha_local.setText(senha);
            cb_salvar_senha_local.setChecked(true);
        } else
            cb_salvar_senha_local.setChecked(false);

        auth = Conexao.getFirebaseAuth();
        user = Conexao.getFirebaseUser();
    }

     public void entrar(View view) {

        String email = edit_email_local.getText().toString().trim();
        String senha = edit_senha_local.getText().toString().trim();

        atualizar(email,senha);

        if ( email == "" || senha == "")
            alert("Preencha os campos E-mail e Senha.");
        else {
            auth.signInWithEmailAndPassword(email, senha)
                    .addOnCompleteListener(act_login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(act_login.this, act_telainicial.class);
                                        startActivity(intent);
                                        finish();
                                    } else
                                        alert("Usuário ou senha inválido.");
                                }
                    });
        }
    }

    public void cadastrar(View view) {

        edit_email_local      = findViewById(R.id.edit_email);
        edit_senha_local      = findViewById(R.id.edit_senha);

        String email = edit_email_local.getText().toString().trim();
        String senha = edit_senha_local.getText().toString().trim();

        atualizar(email,senha);

        if (email.equals("") || senha.equals("") )
          alert("É necessário digitar um e-mail e senha para continuar.");
        else {

            if (auth==null) alert("probema");
            auth.createUserWithEmailAndPassword(email,senha)
                    .addOnCompleteListener(act_login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                edit_email_local      = findViewById(R.id.edit_email);
                                edit_senha_local      = findViewById(R.id.edit_senha);

                                String email = edit_email_local.getText().toString().trim();
                                String senha = edit_senha_local.getText().toString().trim();

                                auth.signInWithEmailAndPassword(email,senha)
                                        .addOnCompleteListener(act_login.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    Intent intent = new Intent(act_login.this, act_alterar.class);
                                                    startActivity(intent);
                                                    finish();
                                                } else
                                                    alert("Usuário ou senha inválido.");

                                            }
                                        });

                            } else
                                alert("Usuário já cadastrado.");
                         };
                    });

        }
    }

    public void alterar(View view) {

        String email = edit_email_local.getText().toString().trim();
        String senha = edit_senha_local.getText().toString().trim();

        atualizar(email,senha);

        if (email.equals("") || senha.equals("") )
            alert("É necessário digitar um e-mail e senha para continuar.");
        else {
            auth.signInWithEmailAndPassword(email, senha)
                    .addOnCompleteListener(act_login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(act_login.this, act_alterar.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        alert("Usuário ou senha inválido.");
                                    }
                                }
                            }
                    );
        }
    }

    private void atualizar(String email, String senha) {
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(act_login.this);
        SharedPreferences.Editor myEditor = myPreferences.edit();
        myEditor.clear();
        myEditor.putBoolean("salvar", cb_salvar_senha_local.isChecked());
        myEditor.putString("email", email);
        myEditor.putString("senha", senha);
        myEditor.commit();
    }

    private void alert (String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}
