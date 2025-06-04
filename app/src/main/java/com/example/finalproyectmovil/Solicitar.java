package com.example.finalproyectmovil;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.finalproyectmovil.utils.MarvelUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Solicitar extends Fragment {

    private Spinner spinnerComics;
    private Button btnSolicitar;
    private List<String> listaComics = new ArrayList<>();
    private ArrayAdapter<String> adaptador;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_solicitar, container, false);

        // Inicializar vistas
        spinnerComics = view.findViewById(R.id.spinner_comics);
        btnSolicitar = view.findViewById(R.id.btn_solicitar);

        // Configurar adaptador para el spinner
        adaptador = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, listaComics);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerComics.setAdapter(adaptador);

        // Cargar los comics
        cargarComics();

        // Configurar el botón
        btnSolicitar.setOnClickListener(v -> validarYSolicitar());

        return view;
    }

    private void cargarComics() {
        String url = MarvelUtils.MARVEL_API_BASE_URL + "comics"
                + "?ts=" + MarvelUtils.TS
                + "&apikey=" + MarvelUtils.PUBLIC_KEY
                + "&hash=" + MarvelUtils.generateHash()
                + "&limit=50";

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject data = jsonObject.getJSONObject("data");
                            JSONArray results = data.getJSONArray("results");

                            listaComics.clear();
                            listaComics.add("Selecciona un cómic"); // Opción por defecto

                            for (int i = 0; i < results.length(); i++) {
                                JSONObject comic = results.getJSONObject(i);
                                String titulo = comic.getString("title");
                                listaComics.add(titulo);
                            }

                            adaptador.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            mostrarError("Error al procesar los datos");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mostrarError("Error al cargar los cómics");
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        queue.add(request);
    }

    private void validarYSolicitar() {
        int posicionSeleccionada = spinnerComics.getSelectedItemPosition();

        if (posicionSeleccionada == 0) {
            // No se ha seleccionado ningún cómic (está en la opción por defecto)
            mostrarError("Por favor, selecciona un cómic");
            return;
        }

        // Si llegamos aquí, se ha seleccionado un cómic válido
        String comicSeleccionado = spinnerComics.getSelectedItem().toString();
        mostrarMensaje("Has solicitado el cómic: " + comicSeleccionado);
        // Aquí puedes agregar la lógica adicional para procesar la solicitud
    }

    private void mostrarError(String mensaje) {
        if (getContext() != null) {
            Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
        }
    }

    private void mostrarMensaje(String mensaje) {
        if (getContext() != null) {
            Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show();
        }
    }
}