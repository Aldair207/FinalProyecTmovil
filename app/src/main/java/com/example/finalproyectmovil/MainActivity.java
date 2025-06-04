package com.example.finalproyectmovil;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.finalproyectmovil.clases.Superheroe;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView btn_navigation;
    private List<Superheroe> listaSuperheroes = new ArrayList<>();
    private static final String MARVEL_API_BASE_URL = "https://gateway.marvel.com/v1/public/";
    private static final String API_KEY = "TU_API_KEY"; // Reemplaza con tu API Key
    private static final String HASH = "TU_HASH"; // Reemplaza con tu hash
    private static final String TS = "1"; // Timestamp

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_navigation = findViewById(R.id.btn_navigation);

        // Cargar fragmento inicial
        loadFragment(new Home());

        // Navegación entre fragmentos
        btn_navigation.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    selectedFragment = new Home();
                } else if (itemId == R.id.nav_comic) {
                    selectedFragment = new Solicitar();
                } else if (itemId == R.id.nav_setting) {
                    selectedFragment = new Configuracion();
                }

                if (selectedFragment != null) {
                    loadFragment(selectedFragment);
                    return true;
                }

                return false;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frm_container, fragment)
                .commit();
    }
}
