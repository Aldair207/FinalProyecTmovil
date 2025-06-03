package com.example.finalproyectmovil.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproyectmovil.R;
import com.example.finalproyectmovil.clases.Superheroe;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SuperheroeAdaptador extends RecyclerView.Adapter<SuperheroeAdaptador.ViewHolder> {

    private List<Superheroe> datos;

    public SuperheroeAdaptador(List<Superheroe> datos) {
        this.datos = datos;
    }

    @NonNull
    @Override
    public SuperheroeAdaptador.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_superheroe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuperheroeAdaptador.ViewHolder holder, int position) {
        Superheroe dato = datos.get(position);
        holder.bind(dato);
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_nombre, txt_descripcion, txt_comics, txt_poderes;
        ImageView img_superheroe;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_nombre = itemView.findViewById(R.id.txt_nombre);
            txt_descripcion = itemView.findViewById(R.id.txt_descripcion);
            txt_comics = itemView.findViewById(R.id.txt_comics);
            txt_poderes = itemView.findViewById(R.id.txt_poderes);
            img_superheroe = itemView.findViewById(R.id.img_superheroe);
        }

        public void bind(Superheroe dato) {
            txt_nombre.setText(dato.getNombre());
            txt_descripcion.setText(dato.getDescripcion());
            txt_comics.setText(dato.getComics());
            txt_poderes.setText(dato.getPoderes());
            Picasso.get().load(dato.getImagen()).into(img_superheroe);
        }
    }
} 