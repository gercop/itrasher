package com.example.itrasher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;



import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static android.widget.Toast.LENGTH_SHORT;

public class act_info extends AppCompatActivity {



    //Implementação do ratingBar #1
    private FirebaseDatabase database2;
    private RatingBar ratingBar;
    //#1


    //início1 setting do chat
    ListView chat;
    ArrayList<String> listOfChat = new ArrayList<String>();
    ArrayAdapter arrayAdpt;
    public String uidVendedor,SelectedChat, uidUser,nomeUsuario, uidComprador;

    private DatabaseReference dbr = FirebaseDatabase.getInstance().getReference().getRoot();
    //limítrofe1 de setting chat

    private FirebaseAuth auth;
    private FirebaseUser user;

    private StorageReference mStorageRef;
    FirebaseDatabase infoFireDataBase;

    public float nota,cte;

    public TextView emailInfo;
    public TextView nomeInfo;
    public TextView telefonelInfo;

    private List<User> mUser;
    private FirebaseRecyclerAdapter<User, ViewHolder> mAdapter;

    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter adapter;
    public String desc_auxiliar;
    public String url_auxiliar;
    public String var_auxiliar;
    public String extra_uid;
    public String extra_email;
    public String intencao;


    private List<User> userList;

    public float valuation, nota2;
    public String valuacao;
    public float score;

    Button avaliar;
    //Button correcao;

    public int a, b,c,N,n;

    RecyclerView mRecyclerView;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference dbUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_info);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Informações do vendedor");

        //Recebe dados da act_detalhesDoProduto
        String desc = getIntent().getStringExtra("descric");
        desc_auxiliar = desc;
        String url_0 = getIntent().getStringExtra("url");
        url_auxiliar = url_0;
        String uid_0 = getIntent().getStringExtra("uid");
        extra_uid = uid_0;
        String email_0 = getIntent().getStringExtra("email");
        extra_email = email_0;
        infoFireDataBase = FirebaseDatabase.getInstance();
        user = Conexao.getFirebaseUser();
        userList = new ArrayList<>();



        //início2 setting chat
        chat = (ListView) findViewById(R.id.chat);
        arrayAdpt =  new ArrayAdapter(this, android.R.layout.simple_list_item_1, listOfChat);
        chat.setAdapter(arrayAdpt);
        uidVendedor = extra_uid;
        uidComprador =  user.getUid();
        //Limítrofe2 setting chat



        //Implementação do ratingBar #2
        database2 = FirebaseDatabase.getInstance();
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        //correcao = (Button) findViewById(R.id.button3);
        avaliar = (Button) findViewById(R.id.button2);
        //correcao.setEnabled(false);

        if (uidComprador.equals(extra_uid) ) {
            Button avaliar = (Button) findViewById(R.id.button2);
            avaliar.setEnabled(false);
            avaliar.setClickable(false);
            avaliar.setVisibility(View.INVISIBLE);
            ratingBar.setVisibility(View.INVISIBLE);
        }

        testRatingBar();


        //#2

        }


    //Implementação do ratingBar #3
    private void testRatingBar() {



        final DatabaseReference ref = database2.getReference("users").child(extra_uid).child("ratingBar").child(user.getUid());


        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot != null && dataSnapshot.getValue() != null) {

                    float rating = Float.parseFloat(dataSnapshot.getValue().toString());
                    ratingBar.setRating(rating);
                    valuation = rating;
                    ratingBar.setEnabled(false);
                    //correcao.setVisibility(View.INVISIBLE);
                    //correcao.setEnabled(false);
                    ratingBar.setVisibility(View.INVISIBLE);
                    c= 1;
                }
                else {
                    c = 0;
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {


                if (dataSnapshot2 != null && dataSnapshot2.getValue() != null) {

                    float rating = Float.parseFloat(dataSnapshot2.getValue().toString());
                    ratingBar.setRating(rating);
                    valuation = rating;
                    ratingBar.setEnabled(false);
                    correcao.setEnabled(true);

                    //ratingBar.setVisibility(View.INVISIBLE);

                }
                else {

                    alert("vazio");
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if(b){
                    ref.setValue(v);

                }
            }
        });



    }

    //#3


    ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        User user1 = snapshot.getValue(User.class);
                        userList.add(user1);
                    }
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };


    @Override
    protected void onStart() {
        super.onStart();

        auth = Conexao.getFirebaseAuth();
        user = Conexao.getFirebaseUser();


        System.out.println(extra_uid);

        Query query = FirebaseDatabase.getInstance().getReference("users").child(extra_uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User users = dataSnapshot.getValue(User.class);

                emailInfo = findViewById(R.id.email_info);
                telefonelInfo = findViewById(R.id.telefone_info);
                nomeInfo = findViewById(R.id.nome_info);

                emailInfo.setText(extra_email);
                telefonelInfo.setText(users.telefone);
                nomeInfo.setText(users.nome);

                var_auxiliar = users.nome;
                SelectedChat = desc_auxiliar + (": Vendedor-") + var_auxiliar;



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //A partir daqui os dados do usuário são coletados
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                nomeUsuario = user.nome;
                uidUser = nomeUsuario;


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                alert("Usuário não encontrado.");
            }

        });
        //Aqui acaba implementação para coletar dados do usuario, é preciso selecionar o nome dele, porém ainda não deu




    }



    private void alert(String msg) {
        Toast.makeText(this, msg, LENGTH_SHORT).show();
    }

    public void chatear(View view) {
        a=0;
        if (a == 1) SelectedChat = extra_uid;
        else SelectedChat= desc_auxiliar+(":Vendedor-")+var_auxiliar;
        intencao = "";

        vaiChat();
    }

    public void intecaoCompra(View view) {
        //aqui a intenção de compra será enviada para um chat no qual somente o vendedor tem acesso
        a=1;

        //condição para que ou o chat publico seja selecionado ou mensagem de inteção de compra seja enviada ao vendedor em um
        //chat particular ao qual só o vendedor tem acesso
        if (a == 1) SelectedChat = extra_uid;
        if (uidComprador.equals(extra_uid) ) {
            a= 0;
            intencao = "";
            vaiChat();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText msgInicial = new EditText(this);
        builder.setView(msgInicial);
        builder.setTitle("Escreva sua intenção de compra e um número de contato com (ddd)");
        builder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                intencao = msgInicial.getText().toString();
                vaiChat();
                alert("Sua intenção de compra foi enviada, aguarde retorno do vendedor");


            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alert("Operação cancelada");

            }
        });
        builder.show();

    }

    private void vaiChat() {

        Intent e = new Intent(getApplicationContext(),DiscussionActivity.class);
        e.putExtra("selected_topic", SelectedChat);
        e.putExtra("userComprador", uidUser);
        e.putExtra("descrição do chat", desc_auxiliar);
        e.putExtra("msgm", intencao);
        e.putExtra("var", a);
        startActivity(e);
        finish();

    }


    public void avaliar(View view) {

        ratingBar.setEnabled(false);

        final DatabaseReference refTwo = database2.getReference("users").child(extra_uid).child("ratingBar");

        refTwo.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                n= (int) dataSnapshot.getChildrenCount();
                N =n;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });

        final DatabaseReference ref2 = database2.getReference("users").child(extra_uid).child("ratingBar").child("ratingAverage");

        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot != null && dataSnapshot.getValue() != null) {

                    nota2 = Float.parseFloat(dataSnapshot.getValue().toString());
                    //nota = valuation;

                    ratingBar.setEnabled(false);
                    ratingBar.setVisibility(View.INVISIBLE);
                    ref2.setValue(valuation);

                    if (valuation == 0){
                        alert ("sua nota foi enviada");


                    }
                    else {
                        mostraValor();
                    }
                }
                else {

                    ref2.setValue(valuation);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //O método de baixo implica em uma mudança contínua e nunca acaba de perceber as "changes" no valor lido
        /*ref2.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot3) {
                if (dataSnapshot3 != null && dataSnapshot3.getValue() != null) {

                    float nota2 = Float.parseFloat(dataSnapshot3.getValue().toString());
                    nota = nota2;
                    //nota = 3+1;
                    ratingBar.setEnabled(false);
                    ratingBar.setVisibility(View.INVISIBLE);
                    //ref2.setValue(valuation);
                    ref2.setValue(nota);

                }
                else {

                    nota = 2;
                    //ref2.setValue(valuation);
                    ref2.setValue(nota);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
             }
        });*/


        avaliar.setEnabled(false);
        //correcao.setVisibility(View.INVISIBLE);

        mediaRating();

    }

    private void mostraValor() {

            String sr = String.valueOf(valuation);
            if (valuation > 1){
            Toast.makeText(this,"Você já avaliou este vendedor com " + sr + " estrelas", Toast.LENGTH_LONG).show();}
            else {
                {
                    Toast.makeText(this,"Você já avaliou este vendedor com " + sr + " estrela", Toast.LENGTH_LONG).show();
                }
            }

    }

//    public void corrigir(View view) {
//        ratingBar.setEnabled(true);
//    }

    private void mediaRating(){


        final DatabaseReference media = database2.getReference("users").child(extra_uid).child("somaRating");


        media.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot != null && dataSnapshot.getValue() != null) {

                    cte = Float.parseFloat(dataSnapshot.getValue().toString());
                    if (valuation != 0 && nota2 != 0){
                        score = cte;
                    }
                    else {
                        score = valuation+cte;
                    }

                    media.setValue(score);
                    calculoMedia();

                }
                else {
                media.setValue(valuation);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });


    }

    private void calculoMedia(){

        float media = (score)/(N-1);
//        String sr = String.valueOf(media);
//        Toast.makeText(this,"mediaaaaa" + sr + " estrelas", Toast.LENGTH_LONG).show();
        final DatabaseReference mediavdd = database2.getReference("users").child(extra_uid).child("Media");
        mediavdd.setValue(media);
    }



}



