package com.example.itrasher;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Lancamento {

    String uid;
    String oferta;
    String descricao;
    String quantidade;
    String unidade;
    String valor;
    String data;
    String chaves;
    String url;
    String email;

    public Lancamento(){
    }

    public Lancamento(String uid, String oferta, String descricao, String quantidade, String unidade,
                      String valor, String data, String chaves, String url_imagem, String email){

        this.uid        = uid;
        this.oferta     = oferta;
        this.descricao  = descricao;
        this.quantidade = quantidade;
        this.unidade    = unidade;
        this.valor      = valor;
        this.data       = data;
        this.chaves     = chaves;
        this.url        = url_imagem;
        this.email      = email;
    }

    public String getUid(){  return this.uid; }

    public String getOferta(){
        return this.oferta;
    }

    public String getDescricao(){
        return this.descricao;
    }

    public String getUnidade(){
        return this.unidade;
    }

    public String getQuantidade(){
        return this.quantidade;
    }

    public String getData(){
        return this.data;
    }

    public String getValor(){
        return this.valor;
    }

    public String getChaves(){
        return this.chaves;
    }

    public String getUrl(){
        return this.url;
    }

    public String getEmail(){
        return this.email;
    }

    public void addLancamento(String uid, String chave) {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();

        DatabaseReference usersRef = ref.child("lancamentos");
        this.uid = uid;
        usersRef.child(chave).setValue(new Lancamento(uid,oferta,descricao,quantidade,unidade,valor,data,chaves,url,email));

    }


}
