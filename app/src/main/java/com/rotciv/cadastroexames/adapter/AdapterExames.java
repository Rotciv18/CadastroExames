package com.rotciv.cadastroexames.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rotciv.cadastroexames.R;
import com.rotciv.cadastroexames.model.Exames;

import java.util.List;

public class AdapterExames extends RecyclerView.Adapter<AdapterExames.MyViewHolder> {

    List<Exames> exames;
    Context context;

    public AdapterExames(List<Exames> exames, Context context) {
        this.exames = exames;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemLista = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_exames, viewGroup, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Log.i("MIZERA", String.valueOf(exames.size()));
        Exames exame = exames.get(i);
        myViewHolder.nome.setText(exame.getNome());
        myViewHolder.tipo.setText(exame.getTipo());
    }

    @Override
    public int getItemCount() {
        Log.i("MIZERA2", String.valueOf(exames.size()));
        return exames.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nome, tipo;

        public MyViewHolder(View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.textNome);
            tipo = itemView.findViewById(R.id.textTipo);
        }

    }

}
