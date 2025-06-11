package com.example.finalproyectmovil.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproyectmovil.R;
import com.example.finalproyectmovil.clases.Superheroe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SuperheroeAdaptador extends RecyclerView.Adapter<SuperheroeAdaptador.ViewHolder>
        implements Filterable {

    public List<Superheroe> datos;
    public List<Superheroe> datosFiltrados;

    public SuperheroeAdaptador(List<Superheroe> datos) {
        this.datos = datos;
        this.datosFiltrados = new ArrayList<>(datos);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_superheroe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Superheroe dato = datosFiltrados.get(position);
        holder.bind(dato);
    }

    @Override
    public int getItemCount() {
        return datosFiltrados.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Superheroe> listaFiltrada = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    listaFiltrada.addAll(datos);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (Superheroe item : datos) {
                        if (item.getNombre().toLowerCase().contains(filterPattern)) {
                            listaFiltrada.add(item);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = listaFiltrada;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                datosFiltrados.clear();
                datosFiltrados.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };
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

            Picasso.get()
                    .load(dato.getImagen())
                    .into(img_superheroe);
        }
    }
}