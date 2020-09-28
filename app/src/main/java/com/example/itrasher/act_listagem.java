package com.example.itrasher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class act_listagem extends AppCompatActivity {

    private CheckBox filtroCheck;
    private List<Lancamento> mLancamentos;
    private FirebaseRecyclerAdapter<Lancamento, ViewHolder> mAdapter;

    RecyclerView mRecyclerView;
    FirebaseDatabase mFirebaseDatabase;
    private FirebaseUser user;
    DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_listagem);


        ActionBar actionBar = getSupportActionBar();

        //Set Title

        actionBar.setTitle("Produtos à venda");

        //Recycler View
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        //Set layout as linear layout

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Send Query to Firebase DataBase

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        user = Conexao.getFirebaseUser();
        //alert(user.getUid());
        mRef = mFirebaseDatabase.getReference("lancamentos");
    }

    private void firebaseSearch (String searchText){


       filtroCheck = findViewById(R.id.checkFiltro);

        Query firebaseSearchQuery;

        mLancamentos = new ArrayList<>();

        if (filtroCheck.isChecked()) {
            user = Conexao.getFirebaseUser();
            String chavesearch = user.getUid().trim() + searchText ;

            firebaseSearchQuery = mRef.orderByChild("chaves").startAt(chavesearch).endAt(chavesearch+ "\uf8ff");
        } else

            firebaseSearchQuery = mRef.orderByChild("descricao").startAt(searchText).endAt(searchText + "\uf8ff");
            FirebaseRecyclerAdapter<Lancamento, ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Lancamento, ViewHolder>(

                Lancamento.class,
                R.layout.row,
                ViewHolder.class,
                firebaseSearchQuery
            )
            {
            @Override
            protected void populateViewHolder(ViewHolder viewHolder, Lancamento lancamento, int position) {

                viewHolder.setDetails(getApplicationContext(), lancamento.getDescricao(), lancamento.getUnidade(),
                        lancamento.getQuantidade(),lancamento.getValor(),lancamento.getData(),lancamento.getUrl(),
                        lancamento.getUid(),lancamento.getEmail() );
            }

            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);

                viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        //view
                        TextView mDescricao = view.findViewById(R.id.rDescricao);
                        TextView mQuantidade  = view.findViewById(R.id.rQuantidade);
                        TextView mUnidade = view.findViewById(R.id.rUnidade);
                        TextView mValor = view.findViewById(R.id.rValor);
                        TextView mData = view.findViewById(R.id.rData);
                        TextView mUrlImagem= view.findViewById(R.id.rTextUrlImagem);
                        TextView mUid= view.findViewById(R.id.textview_uid);
                        TextView mEmail= view.findViewById(R.id.textview_email);


                        //Get data from views
                        String Descricao = mDescricao.getText().toString();
                        String Quantidade = mQuantidade.getText().toString();
                        String Unidade = mUnidade.getText().toString();
                        String Valor = mValor.getText().toString();
                        String Data = mData.getText().toString();
                        String UrlImagem = mUrlImagem.getText().toString();
                        String Uid = mUid.getText().toString();
                        String Email = mEmail.getText().toString();

                        //pass this data to new activity
                        Intent intent = new Intent(view.getContext(),act_detalhesDoProduto.class);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        byte[] bytes = stream.toByteArray();
                        intent.putExtra("descricao", Descricao);
                        intent.putExtra("quantidade", Quantidade);
                        intent.putExtra("unidade", Unidade);
                        intent.putExtra("valor", Valor);
                        intent.putExtra("data", Data);
                        intent.putExtra("url", UrlImagem);
                        intent.putExtra("Uid", Uid);
                        intent.putExtra("Email", Email);


                        startActivity(intent);

                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                        //todo fazer qualquer outra implementaçaão

                    }
                });

                return viewHolder;
            }


        };

        //set adapter to recyclerView

        mRecyclerView.setAdapter(firebaseRecyclerAdapter);

    }


    //Carregar dados no recycler view onStart
    @Override
    protected void onStart() {
        super.onStart();

        //user = Conexao.getFirebaseUser();
        //Query firebaseSearchQuery = mRef.orderByChild("uid").equalTo(user.getUid().toString().trim()) ;


        Toast.makeText(this, "Todos lançamentos sem filtro", Toast.LENGTH_SHORT).show();

        FirebaseRecyclerAdapter <Lancamento, ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Lancamento, ViewHolder>(

                Lancamento.class,
                R.layout.row,
                ViewHolder.class,
                mRef

        ) {
            @Override
            protected void populateViewHolder(ViewHolder viewHolder, Lancamento lancamento, int position) {

                viewHolder.setDetails(getApplicationContext(), lancamento.getDescricao(), lancamento.getUnidade(),
                        lancamento.getQuantidade(),lancamento.getValor(),lancamento.getData(),lancamento.getUrl(),
                        lancamento.getUid(), lancamento.getEmail());
            }


            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);

                viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        //view
                        TextView mDescricao = view.findViewById(R.id.rDescricao);
                        TextView mQuantidade  = view.findViewById(R.id.rQuantidade);
                        TextView mUnidade = view.findViewById(R.id.rUnidade);
                        TextView mValor = view.findViewById(R.id.rValor);
                        TextView mData = view.findViewById(R.id.rData);
                        TextView mUrlImagem= view.findViewById(R.id.rTextUrlImagem);
                        TextView mUid= view.findViewById(R.id.textview_uid);
                        TextView mEmail= view.findViewById(R.id.textview_email);

                        String Descricao = mDescricao.getText().toString();
                        String Quantidade = mQuantidade.getText().toString();
                        String Unidade = mUnidade.getText().toString();
                        String Valor = mValor.getText().toString();
                        String Data = mData.getText().toString();
                        String UrlImagem = mUrlImagem.getText().toString();
                        String Uid = mUid.getText().toString();
                        String Email = mEmail.getText().toString();

                        //pass this data to new activity
                        Intent intent = new Intent(view.getContext(),act_detalhesDoProduto.class);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        byte[] bytes = stream.toByteArray();
                        intent.putExtra("descricao", Descricao);
                        intent.putExtra("quantidade", Quantidade);
                        intent.putExtra("unidade", Unidade);
                        intent.putExtra("valor", Valor);
                        intent.putExtra("data", Data);
                        intent.putExtra("url", UrlImagem);
                        intent.putExtra("Uid", Uid);
                        intent.putExtra("Email", Email);
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                        //todo fazer qualquer outra implementaçaão

                    }
                });

                return viewHolder;
            }
        };


        //set adapter to recyclerView

        mRecyclerView.setAdapter(firebaseRecyclerAdapter);

        filtroCheck = findViewById(R.id.checkFiltro);



    }

    private void alert (String msg){
        Toast.makeText(this,"Conheça os produtos!",Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //inflate the menu; this adds items to the action bar if it present
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        android.widget.SearchView searchView = (android.widget.SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                firebaseSearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                //Filtra enquanto digita
                firebaseSearch(s);

                return false;

            }
        });



        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        //gerencia outros cliques de action bar aqui

        if (id == R.id.action_settings){

            //TODO

            return true;

        }

        return super.onOptionsItemSelected(item);
    }





    private void semFiltro() {


        //Toast.makeText(this, "Todos lançamentos sem filtro", Toast.LENGTH_SHORT).show();

            //Abaixo o filtro é ativado quando a checkBox está selecionada, assim, só os lançamentos do usuário aparecem

            FirebaseRecyclerAdapter<Lancamento, ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Lancamento, ViewHolder>(

                    Lancamento.class,
                    R.layout.row,
                    ViewHolder.class,
                    mRef
            ) {
                @Override
                protected void populateViewHolder(ViewHolder viewHolder, Lancamento lancamento, int position) {

                    viewHolder.setDetails(getApplicationContext(), lancamento.getDescricao(),
                            lancamento.getUnidade(), lancamento.getQuantidade(),lancamento.getValor(),
                            lancamento.getData(),lancamento.getUrl(),lancamento.getUid(),lancamento.getEmail());

                }
                @Override
                public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                    ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);

                    viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {

                            TextView mDescricao = view.findViewById(R.id.rDescricao);
                            TextView mQuantidade  = view.findViewById(R.id.rQuantidade);
                            TextView mUnidade = view.findViewById(R.id.rUnidade);
                            TextView mValor = view.findViewById(R.id.rValor);
                            TextView mData = view.findViewById(R.id.rData);
                            TextView mUrlImagem= view.findViewById(R.id.rTextUrlImagem);
                            TextView mUid= view.findViewById(R.id.textview_uid);
                            TextView mEmail= view.findViewById(R.id.textview_email);

                            String Descricao = mDescricao.getText().toString();
                            String Quantidade = mQuantidade.getText().toString();
                            String Unidade = mUnidade.getText().toString();
                            String Valor = mValor.getText().toString();
                            String Data = mData.getText().toString();
                            String UrlImagem = mUrlImagem.getText().toString();
                            String Uid = mUid.getText().toString();
                            String Email = mEmail.getText().toString();

                            //pass this data to new activity
                            Intent intent = new Intent(view.getContext(),act_detalhesDoProduto.class);
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            byte[] bytes = stream.toByteArray();
                            intent.putExtra("descricao", Descricao);
                            intent.putExtra("quantidade", Quantidade);
                            intent.putExtra("unidade", Unidade);
                            intent.putExtra("valor", Valor);
                            intent.putExtra("data", Data);
                            intent.putExtra("url", UrlImagem);
                            intent.putExtra("Uid", Uid);
                            intent.putExtra("Email", Email);
                            startActivity(intent);
                            finish();

                        }

                        @Override
                        public void onItemLongClick(View view, int position) {

                            //todo fazer qualquer outra implementaçaão

                        }
                    });

                    return viewHolder;
                }
            };

            //set adapter to recyclerView

            mRecyclerView.setAdapter(firebaseRecyclerAdapter);


    }



    private void filtrando() {

         {

            Toast.makeText(this, "Apenas lançamentos do usuário", Toast.LENGTH_SHORT).show();


            //Abaixo o filtro é ativado quando a checkBox está selecionada, assim, só os lançamentos do usuário aparecem
            Query firebaseSearchQuery = mRef.orderByChild("uid").equalTo(user.getUid().toString().trim());
            FirebaseRecyclerAdapter<Lancamento, ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Lancamento, ViewHolder>(

                    Lancamento.class,
                    R.layout.row,
                    ViewHolder.class,
                    firebaseSearchQuery


            ) {
                @Override
                protected void populateViewHolder(ViewHolder viewHolder, Lancamento lancamento, int position) {

                    viewHolder.setDetails(getApplicationContext(), lancamento.getDescricao(),
                            lancamento.getUnidade(), lancamento.getQuantidade(),lancamento.getValor(),
                            lancamento.getData(),lancamento.getUrl(), lancamento.getUid(),lancamento.getEmail());

                }
                @Override
                public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                    ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);

                    viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {

                            //view
                            TextView mDescricao = view.findViewById(R.id.rDescricao);
                            TextView mQuantidade  = view.findViewById(R.id.rQuantidade);
                            TextView mUnidade = view.findViewById(R.id.rUnidade);
                            TextView mValor = view.findViewById(R.id.rValor);
                            TextView mData = view.findViewById(R.id.rData);
                            TextView mUrlImagem= view.findViewById(R.id.rTextUrlImagem);
                            TextView mUid = view.findViewById(R.id.textview_uid);
                            TextView mEmail= view.findViewById(R.id.textview_email);

                            //Get data from views, ***!!!No minuto 10 do video de referencia (Android Firebase 03: RecyclerView - Handle Item Clilcks) é ensinado a mandar imagem***!!!

                            String Descricao = mDescricao.getText().toString();
                            String Quantidade = mQuantidade.getText().toString();
                            String Unidade = mUnidade.getText().toString();
                            String Valor = mValor.getText().toString();
                            String Data = mData.getText().toString();
                            String UrlImagem = mUrlImagem.getText().toString();
                            String Uid = mUid.getText().toString();
                            String Email = mEmail.getText().toString();


                            //pass this data to new activity
                            Intent intent = new Intent(view.getContext(),act_detalhesDoProduto.class);
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            byte[] bytes = stream.toByteArray();
                            intent.putExtra("descricao", Descricao);
                            intent.putExtra("quantidade", Quantidade);
                            intent.putExtra("unidade", Unidade);
                            intent.putExtra("valor", Valor);
                            intent.putExtra("data", Data);
                            intent.putExtra("url", UrlImagem);
                            intent.putExtra("Uid",Uid);
                            intent.putExtra("Email", Email);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onItemLongClick(View view, int position) {
                            //todo fazer qualquer outra implementaçaão
                        }
                    });

                    return viewHolder;
                }




            };

            //set adapter to recyclerView

            mRecyclerView.setAdapter(firebaseRecyclerAdapter);

        }
    }


    //Implementação da seleção da check box para filtrar pesquisa

    public void checkObjeto1(View view) {

        boolean checked = ((CheckBox) view).isChecked();


        switch (view.getId()) {
            case R.id.checkFiltro:

                if (checked) {
                    filtrando();
                }
                else semFiltro();

                break;

                }



            }
}
