package com.rotciv.cadastroexames.activities;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.rotciv.cadastroexames.R;
import com.rotciv.cadastroexames.helper.ConfiguracaoFirebase;
import com.rotciv.cadastroexames.model.Exames;
import com.rotciv.cadastroexames.model.Usuario;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NovoExameActivity extends AppCompatActivity {

    private EditText editData;
    private Exames exame = new Exames();
    private RadioGroup radioGroup;
    private Usuario usuario;

    private long count;

    private DatabaseReference database = ConfiguracaoFirebase.getDatabase();
    private DatabaseReference exameRef;
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getAutenticacao();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_exame);

        getSupportActionBar().setTitle("Marcar Exame");

        editData = findViewById(R.id.editData);
        radioGroup = findViewById(R.id.radioGroup);
        preencherData();
        recuperarUsuario();

    }

    public void recuperarUsuario(){
        database.child("usuarios").child(autenticacao.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usuario = dataSnapshot.getValue(Usuario.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void preencherData(){
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c);
        editData.setText(formattedDate);
    }

    public void marcarExame(View view){
        if (checarCampos()){
            String dataSplit[] = editData.getText().toString().split("/");
            String newData = (dataSplit[0] + dataSplit[1] + dataSplit[2]);
            if (checarDisponibilidade(newData)){
                switch (radioGroup.getCheckedRadioButtonId()){
                    case (R.id.btSangue):
                        exame.setTipo("Sangue");
                        break;
                    case (R.id.btUrina):
                        exame.setTipo("Urina");
                        break;
                    case (R.id.btFezes):
                        exame.setTipo("Fezes");
                        break;
                }
                exame.setData(editData.getText().toString());
                exame.setNome(usuario.getNome() + " " + usuario.getSobrenome());
                Log.i("MIZERA", "???");
                exameRef.child(usuario.getId()).setValue(exame);
                Toast.makeText(NovoExameActivity.this, "Seu exame foi marcado!", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    public boolean checarCampos(){
        if (editData.getText().toString().isEmpty() || (radioGroup.getCheckedRadioButtonId() == -1)){
            Toast.makeText(NovoExameActivity.this, "Preencha todas as opções", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public boolean checarDisponibilidade(String data){
        exameRef = database.child("exames").child(data);

        exameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count = dataSnapshot.getChildrenCount();
                Log.i("MIZERA", String.valueOf(count));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return true;
    }
}
