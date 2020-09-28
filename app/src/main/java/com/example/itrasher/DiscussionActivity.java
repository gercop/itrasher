package com.example.itrasher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;



import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DiscussionActivity extends AppCompatActivity {

    Button btnSendMsg;
    EditText etMsg;
    ListView lvDiscussion;
    ArrayList<String> listConversation = new ArrayList<String>();
    ArrayAdapter arrayAdpt;
    private FirebaseUser user;
    private FirebaseAuth auth;

    String uidUserComprador, SelectedTopic,user_msg_key,nomeUsuario,desc_auxiliar,uidUserVendedor,msgInicial;

    public int a;

    private DatabaseReference dbr,outro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion);

        btnSendMsg = (Button) findViewById(R.id.btnSendMsg);
        etMsg = (EditText) findViewById(R.id.etMessage);
        lvDiscussion = (ListView) findViewById(R.id.lvConversation);
        arrayAdpt = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listConversation);
        lvDiscussion.setAdapter(arrayAdpt);

        user = Conexao.getFirebaseUser();
        uidUserVendedor = user.getUid();

        Intent mIntent = getIntent();

        uidUserComprador = getIntent().getExtras().get("userComprador").toString();
        SelectedTopic = getIntent().getExtras().get("selected_topic").toString();
        desc_auxiliar = getIntent().getExtras().get("descrição do chat").toString();
        msgInicial = getIntent().getExtras().get("msgm").toString();
        a = mIntent.getIntExtra("var", 0);

        setTitle("Produto :" + desc_auxiliar);

        outro = FirebaseDatabase.getInstance().getReference().child("comunicacao").child(SelectedTopic);
        if (a==1) msgmAutomatica();

        btnSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String, Object> map  = new HashMap<String, Object>();
                user_msg_key = outro.push().getKey();
                outro.updateChildren(map);
                DatabaseReference dbr2 = outro.child(user_msg_key);
                Map<String, Object> map2 = new HashMap<String, Object>();
                map2.put("msg", etMsg.getText().toString());
                map2.put("user", uidUserComprador);
                dbr2.updateChildren(map2);
                etMsg.setText("");
            }
        });


        outro.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                updateConversation(dataSnapshot);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                updateConversation(dataSnapshot);


            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        if (user.getUid().equals(SelectedTopic)) {
            //etMsg.setVisibility(View.INVISIBLE);
            etMsg.setEnabled(false);
            //btnSendMsg.setVisibility(View.INVISIBLE);
            btnSendMsg.setEnabled(false);
        }




    }

    private void msgmAutomatica() {

        user = Conexao.getFirebaseUser();
        uidUserVendedor = user.getUid();
        outro = FirebaseDatabase.getInstance().getReference().child("comunicacao").child(SelectedTopic);
        Map<String, Object> map  = new HashMap<String, Object>();
        user_msg_key = outro.push().getKey();
        outro.updateChildren(map);
        DatabaseReference dbr2 = outro.child(user_msg_key);
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("msg", msgInicial);
        map2.put("user", uidUserComprador);
        dbr2.updateChildren(map2);
        etMsg.setText("");
        finish();


    }

    public void updateConversation(DataSnapshot dataSnapshot){
        String msg,user,conversation;
        Iterator i = dataSnapshot.getChildren().iterator();
        while (i.hasNext()){

            msg = (String)((DataSnapshot)i.next()).getValue();
            user = (String) ((DataSnapshot)i.next()).getValue();
            conversation = user + ":" + msg;
            arrayAdpt.insert(conversation, 0);
            arrayAdpt.notifyDataSetChanged();
        }


    }

    private void alert(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
