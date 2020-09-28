package com.example.itrasher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class act_alterar extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private TextView text_email_local;
    private TextView textview_url_local;

    private EditText edit_nome_local;
    private EditText edit_endereco_local;
    private EditText edit_bairro_local;
    private EditText edit_cidade_local;
    private EditText edit_estado_local;
    private EditText edit_cep_local;
    private EditText edit_nacionalidade_local;
    private EditText edit_telefone_local;

    private CheckBox pessoa_juridica_local;
    private CheckBox pessoa_fisica_local;

    private Spinner tipos_pgto_local;

    private ImageView image_foto_local;

    private ProgressBar mProgressBar;

    private Button btn_alterar_local;
    private Button btn_ajudar_local;

    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;

    public Uri mImageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar);

        pessoa_fisica_local      = findViewById(R.id.pessoa_fisica);
        pessoa_juridica_local    = findViewById(R.id.pessoa_juridica);
        text_email_local         = findViewById(R.id.edit_email);
        edit_nome_local          = findViewById(R.id.edit_nome);
        edit_endereco_local      = findViewById(R.id.edit_endereco);
        edit_bairro_local        = findViewById(R.id.edit_bairro);
        edit_cidade_local        = findViewById(R.id.edit_cidade);
        edit_estado_local        = findViewById(R.id.edit_estado);
        edit_cep_local           = findViewById(R.id.edit_cep);

        SimpleMaskFormatter smf1 = new SimpleMaskFormatter("NNNNN-NNN");
        MaskTextWatcher mtw1     = new MaskTextWatcher(edit_cep_local, smf1);
        edit_cep_local.addTextChangedListener(mtw1);

        edit_nacionalidade_local = findViewById(R.id.edit_nacionalidade);

        edit_telefone_local      = findViewById(R.id.edit_telefone);
        SimpleMaskFormatter smf2 = new SimpleMaskFormatter("(NN) NNNNN-NNNN");
        MaskTextWatcher mtw2 = new MaskTextWatcher(edit_telefone_local, smf2);
        edit_telefone_local.addTextChangedListener(mtw2);

        tipos_pgto_local = (Spinner) findViewById(R.id.spinner_tp);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,R.array.tipos_pagamento,android.R.layout.simple_spinner_item);
        tipos_pgto_local.setAdapter(adapter);

        textview_url_local = findViewById(R.id.textview_url);
        image_foto_local = findViewById(R.id.image_logo_al);

        btn_alterar_local = findViewById(R.id.btn_confirmar);
        btn_ajudar_local = findViewById(R.id.btn_ajudar);

        mProgressBar = findViewById(R.id.progressBar_foto_al);
        mStorageRef  = FirebaseStorage.getInstance().getReference("fotos/");
    }

    @Override
    protected void onStart(){
        super.onStart();

        auth = Conexao.getFirebaseAuth();
        user = Conexao.getFirebaseUser();
        if (user == null)
            finish();
        else {
            text_email_local = findViewById(R.id.edit_email);
            text_email_local.setText(user.getEmail());

            atualiza_cadastro();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null )
        {
            mImageUri = data.getData();
            if (mImageUri != null) {

                uploadFile(mImageUri);

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User users = dataSnapshot.getValue(User.class);
                        image_foto_local = findViewById(R.id.image_foto);

                        if ( users!=null && !users.url_image.trim().equals("") )
                            Picasso.with(act_alterar.this).load(users.url_image).into(image_foto_local);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        }
    }

    public void pega_imagem(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    public void atualiza_cadastro(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
        if (ref != null) {
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User users = dataSnapshot.getValue(User.class);

                    if (users != null) {

                        edit_nome_local.setText(users.nome);
                        edit_endereco_local.setText(users.endereco);
                        edit_bairro_local.setText(users.bairro);
                        edit_cidade_local.setText(users.cidade);
                        edit_estado_local.setText(users.estado);
                        edit_cep_local.setText(users.cep);
                        edit_nacionalidade_local.setText(users.nacionalidade);
                        edit_telefone_local.setText(users.telefone);
                        tipos_pgto_local.setSelection(users.tipoplano);
                        textview_url_local.setText(users.url_image);

                        image_foto_local = findViewById(R.id.image_foto);
                        if ( users!=null && !users.url_image.trim().equals("") )
                            Picasso.with(act_alterar.this).load(users.url_image).into(image_foto_local);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    alert("Usuário não encontrado.");
                }
            });
        }
    }

    public void checkObjeto(View view){

        boolean checked = ((CheckBox) view).isChecked();

        switch (view.getId()){
            case R.id.pessoa_fisica:
                if (checked){
                    pessoa_juridica_local = findViewById(R.id.pessoa_juridica);
                    pessoa_juridica_local.setChecked(false);
                    edit_nome_local = (EditText) findViewById(R.id.edit_nome);
                    edit_nome_local.setHint(getString(R.string.text_nome));
                }
                break;
            case R.id.pessoa_juridica:
                if (checked){
                    pessoa_fisica_local = findViewById(R.id.pessoa_fisica);
                    pessoa_fisica_local.setChecked(false);
                    edit_nome_local = findViewById(R.id.edit_nome);
                    edit_nome_local.setHint(getString(R.string.text_razao));
                }
                break;
        }
    }

    public void confirmar(View view) {

        Integer pessoa;
        if (pessoa_fisica_local.isChecked()) pessoa = 1; else pessoa = 2;
        int tp_pgto = tipos_pgto_local.getSelectedItemPosition();
        User users = new User(

                edit_nome_local.getText().toString(),
                edit_endereco_local.getText().toString(),
                edit_cep_local.getText().toString(),
                edit_bairro_local.getText().toString(),
                edit_cidade_local.getText().toString(),
                edit_estado_local.getText().toString(),
                edit_nacionalidade_local.getText().toString(),
                edit_telefone_local.getText().toString(),
                pessoa,tp_pgto,
                textview_url_local.getText().toString().trim());
        users.addUsuario(auth.getCurrentUser().getUid());

        Intent intent = new Intent(act_alterar.this, act_telainicial.class);
        startActivity(intent);
        finish();
    }

    private void uploadFile(Uri mImageUri){

        btn_alterar_local.setEnabled(false);
        btn_ajudar_local.setEnabled(false);

        user = Conexao.getFirebaseUser();
        if (mImageUri != null && user !=null) {
            StorageReference fileReference = mStorageRef.child(user.getUid());

            fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            if (taskSnapshot.getMetadata() != null) {
                                if (taskSnapshot.getMetadata().getReference() != null) {
                                    Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {

                                            textview_url_local.setText(uri.toString());
                                            Picasso.with(act_alterar.this).load(uri.toString()).into(image_foto_local);
                                            btn_alterar_local.setEnabled(true);
                                            btn_ajudar_local.setEnabled(true);
                                        }
                                    });
                                }
                            }
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            },500);

                            Toast.makeText(act_alterar.this,"Carregamento com sucesso",Toast.LENGTH_LONG).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(act_alterar.this, e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                        }
                    });
        } else
            Toast.makeText(this,"Arquivo não selecionado",Toast.LENGTH_SHORT).show();
    }

    private void alert (String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    public void mudaTelaAjuda1(View view) {
        Intent intent = new Intent(act_alterar.this, act_ajuda.class);
        startActivity(intent);
        finish();
    }
}
