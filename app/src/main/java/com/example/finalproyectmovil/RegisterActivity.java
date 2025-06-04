package com.example.finalproyectmovil;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {
    private EditText etEmail, etPassword, etName, etBirthDate;
    private Button btnRegister;
    private Calendar calendar;
    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etName = findViewById(R.id.etName);
        etBirthDate = findViewById(R.id.etBirthDate);
        btnRegister = findViewById(R.id.btnRegister);

        calendar = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        // 1. Hacer el EditText no editable
        etBirthDate.setFocusable(false);
        etBirthDate.setClickable(true);

        // 2. Listener para abrir DatePickerDialog
        etBirthDate.setOnClickListener(v -> {
            DatePickerDialog dialog = new DatePickerDialog(RegisterActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        calendar.set(year, month, dayOfMonth);
                        etBirthDate.setText(dateFormatter.format(calendar.getTime()));
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            dialog.getDatePicker().setMaxDate(System.currentTimeMillis()); // No fechas futuras
            dialog.show();
        });

        btnRegister.setOnClickListener(v -> validateAndRegister());
    }

    private void validateAndRegister() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String name = etName.getText().toString().trim();
        String birthDate = etBirthDate.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            etName.setError("El nombre es requerido");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("El email es requerido");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email inválido");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("La contraseña es requerida");
            return;
        }
        if (password.length() < 6) {
            etPassword.setError("La contraseña debe tener al menos 6 caracteres");
            return;
        }
        if (TextUtils.isEmpty(birthDate)) {
            etBirthDate.setError("La fecha de nacimiento es requerida");
            return;
        }

        // Guardar usuario registrado
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("registered_email", email);
        editor.putString("registered_password", password);
        editor.putString("registered_name", name);
        editor.putString("registered_birthDate", birthDate);
        editor.apply();

        Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
        finish(); // Regresa a LoginActivity
    }
}
