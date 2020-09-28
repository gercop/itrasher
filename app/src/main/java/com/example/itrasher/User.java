package com.example.itrasher;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class User {


    public String senha;
    public String nome;
    public String endereco;
    public String cep;
    public String bairro;
    public String cidade;
    public String estado;
    public String nacionalidade;
    public String telefone;
    public Integer pessoa;
    public Integer tipoplano;
    public String url_image;

    public User(){
        //Construtor padrão para chamada DataSnapshot.getValue(User.class)
    }

    public User(String senha){
        this.senha = senha;
    }

    public User( String nome, String endereco, String cep, String bairro,
                String cidade, String estado, String nacionalidade, String telefone,
                Integer pessoa, Integer tipoplano, String url_image){


        this.nome          = nome;
        this.endereco      = endereco;
        this.cep           = cep;
        this.bairro        = bairro;
        this.cidade        = cidade;
        this.estado        = estado;
        this.nacionalidade = nacionalidade;
        this.telefone      = telefone;
        this.pessoa        = pessoa;
        this.tipoplano     = tipoplano;
        this.url_image     = url_image;
    }


    public String  getNome()          { return this.nome;          }
    public String  getEndereco()      { return this.endereco;      }
    public String  getCep()           { return this.cep;           }
    public String  getBairro()        { return this.bairro;        }
    public String  getCidade()        { return this.cidade;        }
    public String  getEstado()        { return this.estado;        }
    public String  getNacionalidade() { return this.nacionalidade; }
    public String  getTelefone()      { return this.telefone;      }
    public Integer getPessoa()        { return this.pessoa;        }
    public Integer getTipoplano()     { return this.tipoplano;     }
    public String  getUrl_image()     { return this.url_image;     }

    public void addUsuario(String uid) {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();
        DatabaseReference usersRef = ref.child("users");

        usersRef.child(uid).setValue(new User(nome,endereco,cep,bairro,cidade,estado,nacionalidade,
            telefone,pessoa,tipoplano,url_image));
    }
}



