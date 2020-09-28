/*package com.example.itrasher;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class CHAT {
    String msg;
    String user;
    String ident;
    String usuarioValue;

    public CHAT() {}


    public CHAT(String msg, String user, String ident, String usuarioValue){

        this.msg        = msg;
        this.user     = user;
        this.ident     = ident;
        this.usuarioValue     = usuarioValue;


    }

    public String getMsg(){
        return this.msg;
    }

    public String getUser(){
        return this.user;
    }

    public String getIdent(){
        return this.ident;
    }

    public String getUsuarioValue(){
        return this.usuarioValue;
    }


    public void addChat(String ident, String chave) {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();

        DatabaseReference usersRef = ref.child(usuarioValue);
        this.ident = ident;
        usersRef.child(chave).setValue(new CHAT(ident,msg,user,usuarioValue));

    }

}
*/