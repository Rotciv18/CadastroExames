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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.rotciv.cadastroexames.R;
import com.rotciv.cadastroexames.helper.ConfiguracaoFirebase;
import com.rotciv.cadastroexames.model.Usuario;

public class CadastrarActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getAutenticacao();
    private DatabaseReference database = ConfiguracaoFirebase.getDatabase();

    private EditText editNome, editSobrenome, editEmail, editSenha;

    Usuario usuario = new Usuario();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

        getSupportActionBar().setTitle("Cadastrar");

        editEmail = findViewById(R.id.editEmail);
        editNome = findViewById(R.id.editNome);
        editSobrenome = findViewById(R.id.editSobrenome);
        editSenha = findViewById(R.id.editSenha);
    }

    public void cadastrarUsuario(View view){
        if (verificaCampos()){
            autenticacao.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        usuario.setId(autenticacao.getUid());
                        database.child("usuarios").child(usuario.getId()).setValue(usuario);
                        Toast.makeText(CadastrarActivity.this, "Cadastro realizado!", Toast.LENGTH_LONG).show();
                        //startActivity(new Intent(CadastrarActivity.this, UsuarioActivity.class));
                        finish();
                    } else {
                        String excecao;
                        try {
                            throw task.getException();
                        } catch(FirebaseAuthWeakPasswordException e) {
                            excecao = "Digite uma senha mais forte!";
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            excecao = "Digite um email válido!";
                        } catch (FirebaseAuthUserCollisionException e){
                            excecao = "Já existe um usuário com este email!";
                        } catch (Exception e){
                            excecao = "Erro: " + e.getMessage();
                            e.printStackTrace();
                        }

                        Toast.makeText(CadastrarActivity.this, excecao, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    public boolean verificaCampos(){
        usuario.setNome(editNome.getText().toString());
        usuario.setSobrenome(editSobrenome.getText().toString());
        usuario.setSenha(editSenha.getText().toString());
        usuario.setEmail(editEmail.getText().toString());

        if (usuario.getNome().isEmpty() || usuario.getEmail().isEmpty() || usuario.getSenha().isEmpty() || usuario.getSobrenome().isEmpty()) {
            Toast.makeText(CadastrarActivity.this, "Preencha todos os campos", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
}
