package com.rotciv.cadastroexames.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.rotciv.cadastroexames.R;
import com.rotciv.cadastroexames.helper.ConfiguracaoFirebase;

public class LoginActivity extends AppCompatActivity {

    private EditText editEmail, editSenha;
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getAutenticacao();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("Fazer Login");

        editEmail = findViewById(R.id.editNom);
        editSenha = findViewById(R.id.editSenha);
    }

    public void login(View view) {

        if (validaCampos()){
            autenticacao.signInWithEmailAndPassword(editEmail.getText().toString(), editSenha.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(LoginActivity.this, "Login realizado!", Toast.LENGTH_LONG).show();
                        //startActivity(new Intent(LoginActivity.this, UsuarioActivity.class));
                        finish();
                    } else {
                        try {
                            throw (task.getException());
                        } catch (FirebaseAuthInvalidUserException e){
                            Toast.makeText(LoginActivity.this, "Email inválido!", Toast.LENGTH_LONG).show();
                        } catch (FirebaseAuthInvalidCredentialsException e){
                            Toast.makeText(LoginActivity.this, "Senha inválida!", Toast.LENGTH_LONG).show();
                        } catch (Exception e){

                        }
                    }
                }
            });
        }
    }

    public boolean validaCampos(){
        if (editSenha.getText().toString().isEmpty() || editEmail.getText().toString().isEmpty()){
            Toast.makeText(LoginActivity.this, "Preencha todos os campos!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
