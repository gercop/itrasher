package com.example.itrasher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class act_detalhesDoProduto extends AppCompatActivity implements View.OnClickListener{


    DatabaseReference mRef;
    FirebaseDatabase mFirebaseDatabase;
    private StorageReference mStorageRef;
    private FirebaseUser user;
    private FirebaseAuth auth;
    public String url_copia;
    private DatabaseReference mPostReference;


    TextView rDescricao_1, rQuantidade_1, rUnidade_1, rValor_1, rData_1, urlSave;
    ImageView rImageView_1;
    public String urlImag;
    public String desc_auxiliar;
    public String extra_uid;
    public String extra_email;

    public int controle = 0;

    //Implementação do ratingBar #1
    private FirebaseDatabase database2;
    private RatingBar ratingBar;
    //#1


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_detalhes_do_produto);



        Button buttonComprar = findViewById(R.id.comprar);
        buttonComprar.setOnClickListener(this);
        Button buttonAlterar = findViewById(R.id.alterar);
        buttonAlterar.setOnClickListener(this);

        //Send Query to Firebase DataBase

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        user = Conexao.getFirebaseUser();
        mRef = mFirebaseDatabase.getReference("lancamentos");
        mStorageRef = FirebaseStorage.getInstance().getReference("fotos");
        auth = Conexao.getFirebaseAuth();

        //Action bar
        ActionBar actionBar = getSupportActionBar();
        //ActionBar title
        actionBar.setTitle("Detalhes do produto");
        //set back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //Initialize views
        rDescricao_1 = findViewById(R.id.rDescricao_0);
        rData_1 = findViewById(R.id.rData_1);
        rQuantidade_1 = findViewById(R.id.rQuantidade_1);
        rUnidade_1 = findViewById(R.id.rUnidade_1);
        rValor_1 = findViewById(R.id.rValor_1);
        rImageView_1 = findViewById(R.id.rImageView_1);

        //get data from intent !!**aos 18 min de video("Android Firebase 03: RecyclerView - Handle Item Clilcks") é ensinado a colocar os dados abaixo para imagem também**!!


        String desc = getIntent().getStringExtra("descricao");
        String quant = getIntent().getStringExtra("quantidade");
        String uni = getIntent().getStringExtra("unidade");
        String val = getIntent().getStringExtra("valor");
        String dat = getIntent().getStringExtra("data");
        String urlImagem = getIntent().getStringExtra("url");
        String Uid = getIntent().getStringExtra("Uid");
        String Email = getIntent().getStringExtra("Email");

        //set data to views

        rDescricao_1.setText(desc);
        rData_1.setText(dat);
        rValor_1.setText(val);
        rUnidade_1.setText(uni);
        rQuantidade_1.setText(quant);

        //Estabelecendo novas Strings (que serão ou enviadas ou utilizadas) para variáveis recebidas da act_listagem
        desc_auxiliar = desc;
        urlImag = urlImagem;
        extra_uid = Uid;
        extra_email = Email;

        //Implementação do ratingBar #2
        database2 = FirebaseDatabase.getInstance();
        ratingBar = (RatingBar) findViewById(R.id.ratingBar2);
        ratingBar.setEnabled(false);
        //

        preencherRatingBar();

    }

    private void preencherRatingBar() {

        final TextView ratng = (TextView) findViewById(R.id.textView10);

        final DatabaseReference medie = database2.getReference("users").child(extra_uid).child("Media");
        medie.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot != null && dataSnapshot.getValue() != null) {



                    float rating = Float.parseFloat(dataSnapshot.getValue().toString());
                    ratingBar.setRating(rating);
                    ratng.setText("       Reputação média = "+(int) rating+" estrelas");


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if(b){
                    medie.setValue(v);

                }
            }
        });




    }


    //handle onBackPressed (go to previous activity)

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    protected void onStart() {

        super.onStart();

        auth = Conexao.getFirebaseAuth();
        user = Conexao.getFirebaseUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User users = dataSnapshot.getValue(User.class);
                url_copia = users.url_image;

                rImageView_1 = findViewById(R.id.rImageView_1);
                //urlSave = findViewById(R.id.url_save);

                Picasso.with(act_detalhesDoProduto.this)
                        .load(urlImag)
                        .fit()
                        .centerInside()
                        .into(rImageView_1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


           }



    private void alert(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    /*public void alterar(View view) {

        if (urlImag.equals(url_copia)) {
            alert("urls iguais");
        }
        else alert ("urls diferentes");

    }*/

    @Override
    public void onClick(View view) {

        switch (view.getId())

        {
            case R.id.alterar:

                if (urlImag.equals(url_copia)) {

                    Intent intent = new Intent(view.getContext(),act_lancamentos.class);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    byte[] bytes = stream.toByteArray();

                    intent.putExtra("descric", desc_auxiliar);
                    intent.putExtra("url", urlImag);
                    intent.putExtra("controle", controle);

                    startActivity(intent);
                    finish();


                } else alert("Este produto não é seu, você não pode alterá-lo");

                break;


            case R.id.comprar:

                /*if (urlImag.equals(url_copia)) {
                    alert("Este produto já é seu, você só pode alterá-lo");

                }
                else{
                    //Intent intent = new Intent(view.getContext(),act_chat.class);
                    Intent intent = new Intent(view.getContext(),act_info.class);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    byte[] bytes = stream.toByteArray();

                    intent.putExtra("descric", desc_auxiliar);
                    intent.putExtra("url", urlImag);
                    intent.putExtra("uid",extra_uid);
                    intent.putExtra("email",extra_email);


                    startActivity(intent);
                    finish();}*/
                Intent intent = new Intent(view.getContext(),act_info.class);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                byte[] bytes = stream.toByteArray();

                intent.putExtra("descric", desc_auxiliar);
                intent.putExtra("url", urlImag);
                intent.putExtra("uid",extra_uid);
                intent.putExtra("email",extra_email);
                startActivity(intent);
                finish();
                break;

        }

    }
}

