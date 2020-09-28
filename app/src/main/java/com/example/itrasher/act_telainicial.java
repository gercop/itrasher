package com.example.itrasher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class act_telainicial extends AppCompatActivity implements View.OnClickListener{

    private Button buttonLancamentos;
    private Button buttonPesquisa;
    //private Button buttonDicas;
    private Button buttonAjuda;
    //private Button buttonChat;

    private TextView textview_nome_local;
    private TextView textview_email_local;
    private ImageView image_foto_local;

    private FirebaseAuth auth;
    private FirebaseUser user;
    private StorageReference mStorageRef;

    private Uri mImageUri;
    public String url_image_local;
    private ProgressBar mProgressBar;
    public int controle=10;

    //Implementação do ratingBar #1
    private FirebaseDatabase database2;
    private RatingBar ratingBar;
    //#1

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telainicial);

        buttonLancamentos = findViewById(R.id.lancamentos);
        buttonPesquisa = findViewById(R.id.pesquisa);
        //buttonDicas = findViewById(R.id.dicas);
        buttonAjuda = findViewById(R.id.btn_ajuda);
        //buttonChat = findViewById(R.id.btn_chat);


        buttonLancamentos.setOnClickListener(this);
        buttonPesquisa.setOnClickListener(this);
        //buttonDicas.setOnClickListener(this);
        buttonAjuda.setOnClickListener(this);
        //buttonChat.setOnClickListener(this);


        //mProgressBar = findViewById(R.id.progressBar_foto_ti);
        textview_nome_local = findViewById(R.id.textview_nome_ti);
        mStorageRef  = FirebaseStorage.getInstance().getReference("fotos");

        //Implementação do ratingBar #2
        database2 = FirebaseDatabase.getInstance();
        ratingBar = (RatingBar) findViewById(R.id.ratingBar3);
        ratingBar.setEnabled(false);
        //


    }

    private void preencherRatingBar() {
        final TextView ratng = (TextView) findViewById(R.id.textView32);

        final DatabaseReference medie = database2.getReference().child("users").child(user.getUid()).child("Media");

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

    @Override
    protected void onStart(){
        super.onStart();

        auth = Conexao.getFirebaseAuth();
        user = Conexao.getFirebaseUser();
        if (user == null)
            finish();
        else {

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
            if (ref != null) {
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User users = dataSnapshot.getValue(User.class);

                        textview_email_local = findViewById(R.id.textview_nome_ti);
                        textview_email_local = findViewById(R.id.edit_email);
                        textview_nome_local.setText(users.nome);
                        //textview_email_local.setText(user.getEmail());
                        textview_email_local.setText(user.getEmail());

                        url_image_local = users.url_image;
                        image_foto_local = findViewById(R.id.imageView_user);
                        Picasso.with(act_telainicial.this)
                                .load(url_image_local)
                                .fit()
                                .centerInside()
                                .into(image_foto_local);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        alert("Usuário não encontrado.");
                    }
                });
            }
        }
        preencherRatingBar();
    }

    @Override
    public void onClick(View view) {

        //Abaixo é verificado qual dos botóes foi pressionado no aplicativo
        switch (view.getId())
        {
            case R.id.lancamentos:

                //Envia valor para variavel de controle na tela lancamentos
                Intent intent = new Intent(view.getContext(),act_lancamentos.class);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                byte[] bytes = stream.toByteArray();
                intent.putExtra("controle", controle);
                startActivity(intent);
                //finish();
                //openLancamentos();
                break;
            case R.id.pesquisa:
                openPequisa();
                break;
            case R.id.btn_cancelar:
                openActivityAjuda();
                break;
            case R.id.btn_ajuda:
                openActivityAjuda();
                break;
            /*case R.id.btn_chat:
                openChat();
                break;*/
        }



    }

    private void openActivityAjuda() {
        Intent intent = new Intent(act_telainicial.this, act_ajuda.class);
        startActivity(intent);
        //finish();
    }

    private void openChat() {
        Intent intent = new Intent(act_telainicial.this, act_chat.class);
        startActivity(intent);
        //finish();
    }


    private void openPequisa() {

        Intent intent = new Intent(act_telainicial.this, act_listagem.class);
        startActivity(intent);
        //finish();
    }

    private void openLancamentos() {

        //Intent intent = new Intent(act_telainicial.this, act_lancamentos.class);
        //startActivity(intent);
        //finish();
    }

    private void alert (String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}
