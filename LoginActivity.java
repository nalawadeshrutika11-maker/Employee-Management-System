package com.example.employeemanagementsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.employeemanagementsystem.R;
import com.example.employeemanagementsystem.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private CheckBox cbRememberMe;
    private TextView tvForgotPassword, tvTerms;
    private DatabaseHelper dbHelper;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvTerms = findViewById(R.id.tvTerms);

        // Check if user is already logged in
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            navigateToMain();
        }

        // Load saved credentials if remember me was checked
        if (sharedPreferences.getBoolean("rememberMe", false)) {
            etUsername.setText(sharedPreferences.getString("username", ""));
            etPassword.setText(sharedPreferences.getString("password", ""));
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showForgotPasswordDialog();
            }
        });

        tvTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTermsDialog();
            }
        });
    }

    private void loginUser() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check credentials with database
        if (dbHelper.checkUser(username, password)) {
            // Save login state
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLoggedIn", true);
            editor.putString("currentUser", username);
            editor.apply();

            Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
            navigateToMain();
        } else {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showForgotPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Forgot Password");
        builder.setMessage("Please contact system administrator to reset your password.\n\nEmail: admin@company.com");
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    private void showTermsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Terms & Conditions");
        builder.setMessage("By using this Employee Management System, you agree to:\n\n" +
                "1. Maintain confidentiality of employee data\n" +
                "2. Use the system responsibly\n" +
                "3. Report any security issues\n" +
                "4. Follow company policies");
        builder.setPositiveButton("Accept", null);
        builder.setNegativeButton("Decline", null);
        builder.show();
    }
}
