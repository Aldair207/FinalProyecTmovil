package com.example.finalproyectmovil;

import android.app.DatePickerDialog;
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
    private EditText etName;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etBirthDate;
    private Button btnRegister;
    private Calendar calendar;
    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initializeViews();
        setupCalendar();
        setupListeners();
    }

    private void initializeViews() {
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etBirthDate = findViewById(R.id.etBirthDate);
        btnRegister = findViewById(R.id.btnRegister);
    }

    private void setupCalendar() {
        calendar = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        DatePickerDialog.OnDateSetListener date = (view, year, month, day) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            updateLabel();
        };

        etBirthDate.setOnClickListener(v -> {
            DatePickerDialog dialog = new DatePickerDialog(RegisterActivity.this, date,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            dialog.show();
        });
    }

    private void setupListeners() {
        btnRegister.setOnClickListener(v -> validateAndRegister());
    }

    private void updateLabel() {
        etBirthDate.setText(dateFormatter.format(calendar.getTime()));
    }

    private void validateAndRegister() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
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

        // Aquí iría la llamada al servicio de registro
        performRegistration(name, email, password, birthDate);
    }

    private void performRegistration(String name, String email, String password, String birthDate) {
        // TODO: Implementar la llamada al servicio de registro
        // Por ahora, simulamos una respuesta exitosa
        Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
        finish();
    }
} 