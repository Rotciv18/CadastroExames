package com.rotciv.cadastroexames.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.rotciv.cadastroexames.R;
import com.rotciv.cadastroexames.helper.ConfiguracaoFirebase;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getAutenticacao();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
    }

    public void goCadastrarActivity(View view){
        startActivity (new Intent(this, CadastrarActivity.class));
    }

    public void goLoginActivity (View view) {
        startActivity (new Intent(this, LoginActivity.class));
    }

    public void checarLogin(){
        if (autenticacao.getCurrentUser() != null){
            if (autenticacao.getUid().equals("peViGHVI6fNuDons4ndYEyt4lkw1")){
                startActivity(new Intent(this, AdminActivity.class));
                finish();
            } else {
                startActivity(new Intent(this, UsuarioActivity.class));
                finish();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        checarLogin();
    }
}
