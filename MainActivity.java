package com.example.employeemanagementsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.employeemanagementsystem.R;

public class MainActivity extends AppCompatActivity {

    private Button btnAddEmployee, btnSearchEmployee, btnViewAllEmployees;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);

        // Initialize buttons
        btnAddEmployee = findViewById(R.id.btnAddEmployee);
        btnSearchEmployee = findViewById(R.id.btnSearchEmployee);
        btnViewAllEmployees = findViewById(R.id.btnViewAllEmployees);

        // Show welcome message in action bar
        String username = sharedPreferences.getString("currentUser", "User");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Welcome, " + username);
        }

        // Add Employee button click
        btnAddEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEmployeeActivity.class);
                startActivity(intent);
            }
        });

        // Search Employee button click
        btnSearchEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchEmployeeActivity.class);
                startActivity(intent);
            }
        });

        // View All Employees button click
        btnViewAllEmployees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewAllEmployeesActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu - this adds items to the action bar
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle menu item clicks
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        // Clear login session
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.remove("currentUser");
        editor.apply();

        // Navigate back to login screen
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        // Prevent going back to login screen after successful login
        // User must use logout button
        moveTaskToBack(true);
    }
}