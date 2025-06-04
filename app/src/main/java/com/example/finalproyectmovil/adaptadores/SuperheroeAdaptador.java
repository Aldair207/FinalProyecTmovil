package com.example.finalproyectmovil.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproyectmovil.DetalleSuperheroeActivity;
import com.example.finalproyectmovil.R;
import com.example.finalproyectmovil.clases.Superheroe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SuperheroeAdaptador extends RecyclerView.Adapter<SuperheroeAdaptador.SuperheroeViewHolder> 
    implements Filterable {

    private List<Superheroe> listaSuperheroes;
    private List<Superheroe> listaCompleta;

    public SuperheroeAdaptador(List<Superheroe> superheroes) {
        this.listaSuperheroes = superheroes;
        this.listaCompleta = new ArrayList<>(superheroes);
    }

    @NonNull
    @Override
    public SuperheroeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_superheroe, parent, false);
        return new SuperheroeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuperheroeViewHolder holder, int position) {
        Superheroe superheroe = listaSuperheroes.get(position);
        holder.bind(superheroe);

        // Configurar el click listener
        holder.itemView.setOnClickListener(v -> {
            Context context = holder.itemView.getContext();
            Intent intent = new Intent(context, DetalleSuperheroeActivity.class);
            intent.putExtra(DetalleSuperheroeActivity.EXTRA_NOMBRE, superheroe.getNombre());
            intent.putExtra(DetalleSuperheroeActivity.EXTRA_DESCRIPCION, superheroe.getDescripcion());
            intent.putExtra(DetalleSuperheroeActivity.EXTRA_IMAGEN, superheroe.getImagen());
            intent.putExtra(DetalleSuperheroeActivity.EXTRA_COMICS, superheroe.getComics());
            intent.putExtra(DetalleSuperheroeActivity.EXTRA_PODERES, superheroe.getPoderes());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return listaSuperheroes.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Superheroe> listaFiltrada = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    listaFiltrada.addAll(listaCompleta);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (Superheroe superheroe : listaCompleta) {
                        if (superheroe.getNombre().toLowerCase().contains(filterPattern)) {
                            listaFiltrada.add(superheroe);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = listaFiltrada;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listaSuperheroes.clear();
                listaSuperheroes.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };
    }

    static class SuperheroeViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivSuperheroe;
        private final TextView tvNombre;
        private final TextView tvDescripcion;

        public SuperheroeViewHolder(@NonNull View itemView) {
            super(itemView);
            ivSuperheroe = itemView.findViewById(R.id.iv_superheroe);
            tvNombre = itemView.findViewById(R.id.tv_nombre);
            tvDescripcion = itemView.findViewById(R.id.tv_descripcion);
        }

        public void bind(Superheroe superheroe) {
            tvNombre.setText(superheroe.getNombre());
            tvDescripcion.setText(superheroe.getDescripcion());
            
            if (superheroe.getImagen() != null && !superheroe.getImagen().isEmpty()) {
                Picasso.get()
                        .load(superheroe.getImagen())
                        .into(ivSuperheroe);
            }
        }
    }
} 