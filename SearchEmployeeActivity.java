package com.example.employeemanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.employeemanagementsystem.R;
import com.example.employeemanagementsystem.R;
import com.example.employeemanagementsystem.Employee;

public class SearchEmployeeActivity extends AppCompatActivity {

    private EditText etSearchId;
    private Button btnSearch, btnUpdate;
    private TextView tvEmployeeId, tvName, tvAge, tvDepartment, tvSalary;
    private LinearLayout llResultContainer;
    private DatabaseHelper dbHelper;
    private Employee currentEmployee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_employee);

        dbHelper = new DatabaseHelper(this);

        etSearchId = findViewById(R.id.etSearchId);
        btnSearch = findViewById(R.id.btnSearch);
        btnUpdate = findViewById(R.id.btnUpdate);
        llResultContainer = findViewById(R.id.llResultContainer);
        tvEmployeeId = findViewById(R.id.tvEmployeeId);
        tvName = findViewById(R.id.tvName);
        tvAge = findViewById(R.id.tvAge);
        tvDepartment = findViewById(R.id.tvDepartment);
        tvSalary = findViewById(R.id.tvSalary);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEmployee();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentEmployee != null) {
                    Intent intent = new Intent(SearchEmployeeActivity.this, UpdateEmployeeActivity.class);
                    intent.putExtra("EMPLOYEE_ID", currentEmployee.getId());
                    startActivity(intent);
                }
            }
        });
    }

    private void searchEmployee() {
        String idStr = etSearchId.getText().toString().trim();

        if (idStr.isEmpty()) {
            Toast.makeText(this, "Please enter Employee ID", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            currentEmployee = dbHelper.getEmployee(id);

            if (currentEmployee != null) {
                displayEmployee(currentEmployee);
                llResultContainer.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(this, R.string.employee_not_found, Toast.LENGTH_SHORT).show();
                llResultContainer.setVisibility(View.GONE);
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid Employee ID", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayEmployee(Employee employee) {
        tvEmployeeId.setText("ID: " + employee.getId());
        tvName.setText("Name: " + employee.getName());
        tvAge.setText("Age: " + employee.getAge());
        tvDepartment.setText("Department: " + employee.getDepartment());
        tvSalary.setText("Salary: ₹" + String.format("%.2f", employee.getSalary()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh display if returning from update
        if (currentEmployee != null) {
            Employee refreshed = dbHelper.getEmployee(currentEmployee.getId());
            if (refreshed != null) {
                currentEmployee = refreshed;
                displayEmployee(currentEmployee);
            } else {
                llResultContainer.setVisibility(View.GONE);
            }
        }
    }
}
