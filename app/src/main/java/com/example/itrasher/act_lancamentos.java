package com.example.itrasher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.EditText;
import android.widget.Button;

import android.app.DatePickerDialog;
import android.text.InputType;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class act_lancamentos extends AppCompatActivity implements View.OnClickListener {

    private Spinner tipos_unidade;
    private CheckBox checkBox_oferta_local;
    private EditText edit_descricao_local;
    private EditText edit_quantidade_local;
    private EditText edit_valor_local;
    private EditText edit_data_local;
    private Button btn_data_local;
    private Button btn_remover;
    private Spinner spinner_tu_local;
    private TextView textview_urlimagem_local;
    private TextView textview_uid_local;
    private TextView textview_alterar_local;

    private Calendar c;
    private DatePickerDialog dpd;
    public String desc_auxiliar;
    public String url_auxiliar;
    public int controle_auxiliar;

    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lancamentos);

        tipos_unidade = (Spinner) findViewById(R.id.spinner_tu);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.tipos_unidades,
                android.R.layout.simple_spinner_item);
        tipos_unidade.setAdapter(adapter);

        checkBox_oferta_local = findViewById(R.id.checkBox_oferta);
        edit_descricao_local  = findViewById(R.id.edit_senha);
        edit_quantidade_local = findViewById(R.id.edit_quantidade);

        edit_valor_local = findViewById(R.id.edit_valor);
        edit_valor_local.addTextChangedListener(new MascaraMonetaria(edit_valor_local));

        edit_data_local = findViewById(R.id.edit_data);
        edit_data_local.setInputType(InputType.TYPE_NULL);
        edit_data_local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int dia = c.get(Calendar.DAY_OF_MONTH);
                int mes = c.get(Calendar.MONTH);
                int ano = c.get(Calendar.YEAR);

                dpd = new DatePickerDialog(act_lancamentos.this,
                      new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int ano, int mes, int dia) {
                                edit_data_local.setText(dia + "/" + (mes + 1) + "/" + ano);
                            }
                        }, ano, mes, dia);
                dpd.show();
            }
        });

        btn_data_local = findViewById(R.id.btn_data);
        btn_remover = findViewById(R.id.btn_Remover);
        btn_data_local.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        c = Calendar.getInstance();
                        int ano = c.get(Calendar.YEAR);
                        int mes = c.get(Calendar.MONTH);
                        int dia = c.get(Calendar.DAY_OF_MONTH);

                        dpd = new DatePickerDialog(act_lancamentos.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int ano, int mes, int dia) {
                                edit_data_local.setText(dia + "/" + (mes+1) +"/" + ano);
                            }
                        }, ano, mes, dia);
                        dpd.show();
                    }
        });

        spinner_tu_local         = findViewById(R.id.spinner_tu);
        textview_urlimagem_local = findViewById(R.id.textview_urlimagem);
        textview_uid_local       = findViewById(R.id.textview_uid);
        textview_alterar_local   = findViewById(R.id.textview_alterar);

        String desc = getIntent().getStringExtra("descric");
        desc_auxiliar = desc;
        String url_0 = getIntent().getStringExtra("url");
        url_auxiliar = url_0;
        Integer controle_0 = getIntent().getIntExtra("controle",0);
        controle_auxiliar = controle_0;
        Integer controle_1 = getIntent().getIntExtra("controle",10);
        controle_auxiliar = controle_1;

        if (controle_auxiliar == 0){
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle("Alterar Lançamento: " + desc_auxiliar);}
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (controle_auxiliar == 10){
            btn_remover.setClickable(false);
            btn_remover.setEnabled(false);
        }

        user = Conexao.getFirebaseUser();

        if (controle_auxiliar == 0) {

             Query ref = FirebaseDatabase.getInstance().getReference().child("lancamentos").orderByChild("descricao");
             ref.addValueEventListener(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     Lancamento lancV;
                     for (DataSnapshot child : dataSnapshot.getChildren()) {
                         Lancamento lanc = child.getValue(Lancamento.class);
                         if (lanc.descricao.equals(desc_auxiliar) &&
                             lanc.url.equals(url_auxiliar)){

                             edit_descricao_local.setText(lanc.descricao);
                             edit_quantidade_local.setText(lanc.quantidade);
                             edit_valor_local.setText(lanc.valor);
                             edit_data_local.setText(lanc.data);
                             textview_alterar_local.setText(child.getKey());

                             int tp = 0;
                             if (lanc.unidade.trim().equals("Grama"))           tp = 0;
                             else if (lanc.unidade.trim().equals("Quilograma")) tp = 1;
                             else if (lanc.unidade.trim().equals("Tonelada"))   tp = 2;
                             else if (lanc.unidade.trim().equals("Unidade"))    tp = 3;

                             spinner_tu_local.setSelection(tp);
                         }
                     }
                 }

                 @Override
                 public void onCancelled(@NonNull DatabaseError databaseError) {
                 }
             });
         }


        else {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
            if (ref != null) {
                ref.addValueEventListener(new ValueEventListener() {
                   @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                       User users = dataSnapshot.getValue(User.class);

                        textview_urlimagem_local.setText(users.url_image);
                        textview_uid_local = findViewById(R.id.textview_uid);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        alert("Usuário não encontrado.");
                    }
                });
            }
        }

        if (user == null)
            finish();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.btn_lancar:
                if ( edit_descricao_local.getText().toString().equals("")   ||
                     edit_quantidade_local.getText().toString().equals("")  ||
                     edit_valor_local.getText().toString().equals("")   )
                        alert("Preencha os campos obrigatórios (*).");
                else {
                    openLancamentos();
                    voltar_tela_inicial();
                }
                break;

            case R.id.btn_cancelar:
                voltar_tela_inicial();
                break;
            case R.id.btn_data:
                break;
            case R.id.btn_Remover:
                String chave_secundaria = textview_alterar_local.getText().toString().trim();
                DatabaseReference Ref = FirebaseDatabase.getInstance().getReference().child("lancamentos").child(chave_secundaria);
                Ref.removeValue();
                voltar_tela_inicial();
                break;
        }
    }

    public void openLancamentos(){

        String oferta;
        String chave;

        if (controle_auxiliar==0)
            openLancamentos_alterar();
        else {

            SimpleDateFormat formataData = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat formataTime = new SimpleDateFormat("HHmmss");
            Date data = new Date();
            String dataFormatada = formataData.format(data);
            String horaFormatada = formataTime.format(data);

            chave = user.getUid().trim() + dataFormatada + horaFormatada;

            String chave_secundaria = user.getUid().trim() + edit_descricao_local.getText().toString().trim();
            if (checkBox_oferta_local.isChecked()) oferta = "oferta";
            else oferta = "procura";

            Lancamento lanc = new Lancamento(
                    user.getUid().trim(),
                    oferta,
                    edit_descricao_local.getText().toString().trim(),
                    edit_quantidade_local.getText().toString().trim(),
                    spinner_tu_local.getSelectedItem().toString().trim(),
                    edit_valor_local.getText().toString().trim(),
                    edit_data_local.getText().toString().trim(),
                    chave_secundaria,
                    textview_urlimagem_local.getText().toString().trim(),
                    user.getEmail().trim());
            lanc.addLancamento(user.getUid(), chave);
        }
     }

    private void openLancamentos_alterar() {

        String oferta;

        if (checkBox_oferta_local.isChecked()) oferta = "oferta";
        else oferta = "procura";

        String chave_secundaria = textview_alterar_local.getText().toString().trim();

        DatabaseReference Ref = FirebaseDatabase.getInstance().getReference().child("lancamentos").child(chave_secundaria);

//        Ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Lancamento lancV = dataSnapshot.getValue(Lancamento.class);
//
//                String strkey = dataSnapshot.getKey();
//                String struid = lancV.uid;
//                String strchaves = lancV.uid+edit_descricao_local.getText().toString().trim();
//                String strurl = lancV.url;
//                String stremail = lancV.email;
//
//                Lancamento lanc = new Lancamento(
//                        struid,
//                        "procura",
//                        edit_descricao_local.getText().toString().trim(),
//                        edit_quantidade_local.getText().toString().trim(),
//                        spinner_tu_local.getSelectedItem().toString().trim(),
//                        edit_valor_local.getText().toString().trim(),
//                        edit_data_local.getText().toString().trim(),
//                        strchaves,
//                        strurl,
//                        stremail);
//
//                lanc.addLancamento(struid,strkey);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        });

        Ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Lancamento lancV = dataSnapshot.getValue(Lancamento.class);

                String strkey = dataSnapshot.getKey();
                String struid = lancV.uid;
                String strchaves = lancV.uid+edit_descricao_local.getText().toString().trim();
                String strurl = lancV.url;
                String stremail = lancV.email;

                Lancamento lanc = new Lancamento(
                        struid,
                        "procura",
                        edit_descricao_local.getText().toString().trim(),
                        edit_quantidade_local.getText().toString().trim(),
                        spinner_tu_local.getSelectedItem().toString().trim(),
                        edit_valor_local.getText().toString().trim(),
                        edit_data_local.getText().toString().trim(),
                        strchaves,
                        strurl,
                        stremail);

                lanc.addLancamento(struid,strkey);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void checkObjeto(View view) {

        boolean checked = ((CheckBox) view).isChecked();
        CheckBox oferta, procura;

        switch (view.getId()) {
            case R.id.checkBox_oferta:

                if (checked) {
                    procura = findViewById(R.id.checkBox_procura);
                    procura.setChecked(false);
                }
                break;
            case R.id.checkBox_procura:
                if (checked) {
                    oferta = findViewById(R.id.checkBox_oferta);
                    oferta.setChecked(false);
                }
                break;
        }
    }

    private void voltar_tela_inicial(){
        Intent intent = new Intent(act_lancamentos.this, act_telainicial.class);
        startActivity(intent);
        finish();
    }

    private void alert (String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}

