package com.rotciv.cadastroexames.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.rotciv.cadastroexames.R;
import com.rotciv.cadastroexames.adapter.AdapterExames;
import com.rotciv.cadastroexames.helper.ConfiguracaoFirebase;
import com.rotciv.cadastroexames.model.Exames;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ExamesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdapterExames adapterExames;

    private List<Exames> exames = new ArrayList<>();

    private DatabaseReference database = ConfiguracaoFirebase.getDatabase();
    private DatabaseReference examesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exames);

        recyclerView = findViewById(R.id.recyclerView);

        //configurar adapter
        adapterExames = new AdapterExames(exames, this);
        //configurar recyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter( adapterExames );
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarExames();
    }

    public void recuperarExames(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c);
        String dataSplit[] = formattedDate.split("/");
        String data = (dataSplit[0] + dataSplit[1] + dataSplit[2]);

        examesRef = database.child("exames").child(data);

        examesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                exames.clear();

                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    Exames exame = dados.getValue(Exames.class);
                    exames.add(exame);

                }

                adapterExames.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
