package com.example.finalproyectmovil.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.finalproyectmovil.R;
import com.example.finalproyectmovil.adaptadores.SuperheroeAdaptador;
import com.example.finalproyectmovil.clases.Superheroe;
import com.example.finalproyectmovil.utils.MarvelUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private RecyclerView rcv_lista_superheroes;
    private EditText etSearch;
    private List<Superheroe> listaSuperheroes = new ArrayList<>();
    private SuperheroeAdaptador adaptador;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        rcv_lista_superheroes = view.findViewById(R.id.rcv_lista_superheroes);
        etSearch = view.findViewById(R.id.etSearch);
        
        rcv_lista_superheroes.setLayoutManager(new LinearLayoutManager(getContext()));
        adaptador = new SuperheroeAdaptador(listaSuperheroes);
        rcv_lista_superheroes.setAdapter(adaptador);

        // Configurar el listener para el filtro de búsqueda
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adaptador.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        cargarSuperheroes();

        return view;
    }

    private void cargarSuperheroes() {
        String url = MarvelUtils.MARVEL_API_BASE_URL + "characters"
                + "?ts=" + MarvelUtils.TS
                + "&apikey=" + MarvelUtils.PUBLIC_KEY
                + "&hash=" + MarvelUtils.generateHash()
                + "&limit=50";

        Log.d(TAG, "URL de la petición: " + url);

        StringRequest myRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d(TAG, "Respuesta recibida: " + response.substring(0, Math.min(500, response.length())));
                            procesarRespuesta(new JSONObject(response));
                        } catch (JSONException e) {
                            Log.e(TAG, "Error al procesar respuesta: " + e.getMessage());
                            mostrarError();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error en la petición: " + error.getMessage());
                        mostrarError();
                    }
                });

        RequestQueue rq = Volley.newRequestQueue(requireContext());
        rq.add(myRequest);
    }

    private void procesarRespuesta(JSONObject respuesta) {
        try {
            JSONObject data = respuesta.getJSONObject("data");
            JSONArray results = data.getJSONArray("results");
            
            Log.d(TAG, "Número de superhéroes recibidos: " + results.length());

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
                
                Log.d(TAG, "Superhéroe agregado: " + nombre + " - Imagen: " + imagen);
            }

            adaptador.notifyDataSetChanged();

        } catch (JSONException e) {
            Log.e(TAG, "Error al procesar JSON: " + e.getMessage());
            mostrarError();
        }
    }

    private void mostrarError() {
        if (getContext() != null) {
            Toast.makeText(getContext(), 
                "Error en el servidor, intente mas tarde", 
                Toast.LENGTH_LONG).show();
        }
    }
} 