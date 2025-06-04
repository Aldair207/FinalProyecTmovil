package com.example.finalproyectmovil;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.squareup.picasso.Picasso;

public class DetalleSuperheroeActivity extends AppCompatActivity {

    public static final String EXTRA_NOMBRE = "extra_nombre";
    public static final String EXTRA_DESCRIPCION = "extra_descripcion";
    public static final String EXTRA_IMAGEN = "extra_imagen";
    public static final String EXTRA_COMICS = "extra_comics";
    public static final String EXTRA_PODERES = "extra_poderes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_superheroe);

        // Configurar Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Obtener referencias de las vistas
        ImageView ivSuperheroe = findViewById(R.id.iv_superheroe);
        TextView tvNombre = findViewById(R.id.tv_nombre);
        TextView tvDescripcion = findViewById(R.id.tv_descripcion);
        TextView tvComics = findViewById(R.id.tv_comics);
        TextView tvPoderes = findViewById(R.id.tv_poderes);

        // Obtener datos del intent
        String nombre = getIntent().getStringExtra(EXTRA_NOMBRE);
        String descripcion = getIntent().getStringExtra(EXTRA_DESCRIPCION);
        String imagen = getIntent().getStringExtra(EXTRA_IMAGEN);
        String comics = getIntent().getStringExtra(EXTRA_COMICS);
        String poderes = getIntent().getStringExtra(EXTRA_PODERES);

        // Establecer los datos en las vistas
        tvNombre.setText(nombre);
        tvDescripcion.setText(descripcion);
        tvComics.setText(comics);
        tvPoderes.setText(poderes);

        // Cargar imagen con Picasso
        if (imagen != null && !imagen.isEmpty()) {
            Picasso.get()
                    .load(imagen)
                    .into(ivSuperheroe);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
} 