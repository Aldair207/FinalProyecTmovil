package com.example.finalproyectmovil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Configuracion extends Fragment {

    private TextView txtNombre, txtCorreo, txtFechaNacimiento;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment__configuracion, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtNombre = view.findViewById(R.id.txtNombre);
        txtCorreo = view.findViewById(R.id.txtCorreo);
        txtFechaNacimiento = view.findViewById(R.id.txtFechaNacimiento); // Asegúrate de tener este TextView en el XML

        Button btnLogout = view.findViewById(R.id.btnLogout);

        SharedPreferences prefs = requireActivity().getSharedPreferences("user_prefs", getContext().MODE_PRIVATE);
        String correo = prefs.getString("registered_email", null);

        if (correo != null) {
            obtenerDatosUsuario(correo);
        } else {
            Toast.makeText(getContext(), "Usuario no encontrado", Toast.LENGTH_SHORT).show();
        }

        btnLogout.setOnClickListener(v -> {
            prefs.edit().clear().apply();

            Intent intent = new Intent(requireActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    private void obtenerDatosUsuario(String correo) {
        new Thread(() -> {
            try {
                URL url = new URL("https://apiuser2-production.up.railway.app/api/user");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoOutput(true);

                // Enviar el correo en el body
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("correo", correo);

                OutputStream os = conn.getOutputStream();
                os.write(jsonBody.toString().getBytes("UTF-8"));
                os.close();

                int responseCode = conn.getResponseCode();
                InputStream is = (responseCode == 200) ? conn.getInputStream() : conn.getErrorStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                String responseText = sb.toString();
                Log.e("API_RESPONSE", "Respuesta: " + responseText);

                JSONObject response = new JSONObject(responseText);
                JSONObject usuario = response.getJSONObject("usuario");

                String nombre = usuario.optString("nombre", "Nombre no disponible");
                String correoUsuario = usuario.optString("correo", "Correo no disponible");
                String fechaNacimiento = usuario.optString("fecha_nacimiento", "Fecha no disponible");

                requireActivity().runOnUiThread(() -> {
                    txtNombre.setText("Nombre: " + nombre);
                    txtCorreo.setText("Correo: " + correoUsuario);
                    txtFechaNacimiento.setText("Fecha de nacimiento: " + fechaNacimiento);
                });

                conn.disconnect();
            } catch (Exception e) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Error al obtener datos: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
            }
        }).start();
    }
}