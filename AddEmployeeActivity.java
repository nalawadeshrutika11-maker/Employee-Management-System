package com.example.employeemanagementsystem;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.employeemanagementsystem.R;
import com.example.employeemanagementsystem.DatabaseHelper;
import com.example.employeemanagementsystem.Employee;

public class AddEmployeeActivity extends AppCompatActivity {

    private EditText etName, etAge, etDepartment, etSalary;
    private Button btnSubmit;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);

        dbHelper = new DatabaseHelper(this);

        etName = findViewById(R.id.etName);
        etAge = findViewById(R.id.etAge);
        etDepartment = findViewById(R.id.etDepartment);
        etSalary = findViewById(R.id.etSalary);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEmployee();
            }
        });
    }

    private void addEmployee() {
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

            Employee employee = new Employee(name, age, department, salary);
            long id = dbHelper.addEmployee(employee);

            if (id > 0) {
                Toast.makeText(this, R.string.employee_added, Toast.LENGTH_SHORT).show();
                clearFields();
            } else {
                Toast.makeText(this, "Failed to add employee", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid age or salary format", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearFields() {
        etName.setText("");
        etAge.setText("");
        etDepartment.setText("");
        etSalary.setText("");
    }
}
