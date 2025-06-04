package com.example.finalproyectmovil;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.finalproyectmovil.adaptadores.SuperheroeAdaptador;
import com.example.finalproyectmovil.clases.Superheroe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rcv_lista_superheroes;
    private List<Superheroe> listaSuperheroes = new ArrayList<>();
    private static final String MARVEL_API_BASE_URL = "https://gateway.marvel.com/v1/public/";
    private static final String API_KEY = "TU_API_KEY"; // Reemplazar con tu API key de Marvel
    private static final String HASH = "TU_HASH"; // Reemplazar con tu hash MD5
    private static final String TS = "1"; // Timestamp

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rcv_lista_superheroes = findViewById(R.id.rcv_lista_superheroes);
        rcv_lista_superheroes.setLayoutManager(new LinearLayoutManager(this));

        cargarSuperheroes();
    }

    private void cargarSuperheroes() {
        String url = MARVEL_API_BASE_URL + "characters"
                + "?ts=" + TS
                + "&apikey=" + API_KEY
                + "&hash=" + HASH
                + "&limit=50"; // Limitamos a 50 superhéroes por carga

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        procesarRespuesta(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, 
                            "Error al cargar los superhéroes: " + error.getMessage(), 
                            Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void procesarRespuesta(JSONObject response) {
        try {
            JSONObject data = response.getJSONObject("data");
            JSONArray results = data.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject hero = results.getJSONObject(i);
                
                String nombre = hero.getString("name");
                String descripcion = hero.getString("description");
                if (descripcion.isEmpty()) {
                    descripcion = "No hay descripción disponible";
                }

                // Construir la URL de la imagen
                JSONObject thumbnail = hero.getJSONObject("thumbnail");
                String imagen = thumbnail.getString("path") + "." + thumbnail.getString("extension");

                // Obtener comics
                JSONObject comics = hero.getJSONObject("comics");
                String comicsInfo = "Comics disponibles: " + comics.getInt("available");

                // Para los poderes, usaremos la lista de series como ejemplo
                JSONObject series = hero.getJSONObject("series");
                String poderes = "Apariciones en series: " + series.getInt("available");

                Superheroe superheroe = new Superheroe(nombre, descripcion, imagen, comicsInfo, poderes);
                listaSuperheroes.add(superheroe);
            }

            rcv_lista_superheroes.setAdapter(new SuperheroeAdaptador(listaSuperheroes));

        } catch (JSONException e) {
            Toast.makeText(MainActivity.this, 
                "Error al procesar los datos: " + e.getMessage(), 
                Toast.LENGTH_LONG).show();
        }
    }
}