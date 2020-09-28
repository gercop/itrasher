package com.example.itrasher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class act_chat extends AppCompatActivity {


    private FirebaseAuth auth;
    private FirebaseUser user;

    public String uidUserComprador, SelectedTopic,desc_auxiliar;




    ListView chat;
    ArrayList<String> listOfChat = new ArrayList<String>();
    ArrayAdapter arrayAdpt;
    public String uidVendedor;

    private DatabaseReference dbr = FirebaseDatabase.getInstance().getReference().getRoot().child("comunicacao");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_chat);

        chat = (ListView) findViewById(R.id.chat);
        arrayAdpt =  new ArrayAdapter(this, android.R.layout.simple_list_item_1, listOfChat);
        chat.setAdapter(arrayAdpt);

        uidUserComprador = getIntent().getExtras().get("userComprador").toString();
        SelectedTopic = getIntent().getExtras().get("selected_topic").toString();
        desc_auxiliar = getIntent().getExtras().get("descrição do chat").toString();



        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<String>();
                Iterator i = dataSnapshot.getChildren().iterator();

                while(i.hasNext()){
                    set.add(((DataSnapshot)i.next()).getKey());

                }
                arrayAdpt.clear();
                arrayAdpt.addAll(set);
                arrayAdpt.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        chat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent e = new Intent(getApplicationContext(),DiscussionActivity.class);
                //e.putExtra("selected_topic", ((TextView)view).getText().toString());
                e.putExtra("selected_topic", SelectedTopic);
                e.putExtra("userComprador", uidUserComprador);
                e.putExtra("descrição do chat", desc_auxiliar);
                startActivity(e);
            }
        });




    }
}
