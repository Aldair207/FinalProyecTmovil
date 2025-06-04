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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.finalproyectmovil.adaptadores.SuperheroeAdaptador;
import com.example.finalproyectmovil.clases.Superheroe;
import com.example.finalproyectmovil.utils.MarvelUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rcv_lista_superheroes;
    private List<Superheroe> listaSuperheroes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rcv_lista_superheroes = findViewById(R.id.rcv_lista_superheroes);
        rcv_lista_superheroes.setLayoutManager(new LinearLayoutManager(this));

        cargarSuperheroes();
    }

    private void cargarSuperheroes() {
        String url = MarvelUtils.MARVEL_API_BASE_URL + "characters"
                + "?ts=" + MarvelUtils.TS
                + "&apikey=" + MarvelUtils.PUBLIC_KEY
                + "&hash=" + MarvelUtils.generateHash()
                + "&limit=50";

        StringRequest myRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            procesarRespuesta(new JSONObject(response));
                        } catch (JSONException e) {
                            Toast.makeText(MainActivity.this,
                                    "Error en el servidor, intente mas tarde",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,
                                "Error en el servidor, intente mas tarde" + error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
        rq.add(myRequest);
    }

    public void procesarRespuesta(JSONObject respuesta) {
        try {
            JSONObject data = respuesta.getJSONObject("data");
            JSONArray results = data.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject hero = results.getJSONObject(i);

                String nombre = hero.getString("name");
                String descripcion = hero.getString("description");
                if (descripcion.isEmpty()) {
                    descripcion = "No hay descripción disponible";
                }

                JSONObject thumbnail = hero.getJSONObject("thumbnail");
                String imagen = thumbnail.getString("path") + "." + thumbnail.getString("extension");

                JSONObject comics = hero.getJSONObject("comics");
                String comicsInfo = "Comics disponibles: " + comics.getInt("available");

                JSONObject series = hero.getJSONObject("series");
                String poderes = "Apariciones en series: " + series.getInt("available");

                Superheroe superheroe = new Superheroe(nombre, descripcion, imagen, comicsInfo, poderes);
                listaSuperheroes.add(superheroe);

                rcv_lista_superheroes.setLayoutManager(new LinearLayoutManager(this));
                rcv_lista_superheroes.setAdapter(new SuperheroeAdaptador(listaSuperheroes));
            }
        } catch (JSONException e) {
            Toast.makeText(MainActivity.this,
                    "Error en el servidor, intente mas tarde",
                    Toast.LENGTH_LONG).show();
        }
    }
}