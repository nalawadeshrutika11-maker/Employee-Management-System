package com.example.employeemanagementsystem;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.employeemanagementsystem.R;
import com.example.employeemanagementsystem.DatabaseHelper;
import com.example.employeemanagementsystem.Employee;

public class UpdateEmployeeActivity extends AppCompatActivity {

    private EditText etEmployeeId, etName, etAge, etDepartment, etSalary;
    private Button btnUpdate, btnDelete;
    private DatabaseHelper dbHelper;
    private int employeeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_employee);

        dbHelper = new DatabaseHelper(this);

        etEmployeeId = findViewById(R.id.etEmployeeId);
        etName = findViewById(R.id.etName);
        etAge = findViewById(R.id.etAge);
        etDepartment = findViewById(R.id.etDepartment);
        etSalary = findViewById(R.id.etSalary);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        // Get employee ID from intent
        employeeId = getIntent().getIntExtra("EMPLOYEE_ID", -1);

        if (employeeId != -1) {
            loadEmployeeData();
        } else {
            Toast.makeText(this, "Error loading employee", Toast.LENGTH_SHORT).show();
            finish();
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEmployee();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDelete();
            }
        });
    }

    private void loadEmployeeData() {
        Employee employee = dbHelper.getEmployee(employeeId);
        if (employee != null) {
            etEmployeeId.setText(String.valueOf(employee.getId()));
            etName.setText(employee.getName());
            etAge.setText(String.valueOf(employee.getAge()));
            etDepartment.setText(employee.getDepartment());
            etSalary.setText(String.valueOf(employee.getSalary()));
        }
    }

    private void updateEmployee() {
        String name = etName.getText().toString().trim();
        String ageStr = etAge.getText().toString().trim();
        String department = etDepartment.getText().toString().trim();
        String salaryStr = etSalary.getText().toString().trim();

        if (name.isEmpty() || ageStr.isEmpty() || department.isEmpty() || salaryStr.isEmpty()) {
            Toast.makeText(this, R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int age = Integer.parseInt(ageStr);
            double salary = Double.parseDouble(salaryStr);

            Employee employee = new Employee(employeeId, name, age, department, salary);
            int rowsAffected = dbHelper.updateEmployee(employee);

            if (rowsAffected > 0) {
                Toast.makeText(this, R.string.employee_updated, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to update employee", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid age or salary format", Toast.LENGTH_SHORT).show();
        }
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Employee")
                .setMessage("Are you sure you want to delete this employee?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    dbHelper.deleteEmployee(employeeId);
                    Toast.makeText(this, R.string.employee_deleted, Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
