package com.example.employeemanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.employeemanagementsystem.R;
import com.example.employeemanagementsystem.EmployeeAdapter;
import com.example.employeemanagementsystem.DatabaseHelper;
import com.example.employeemanagementsystem.Employee;
import java.util.List;

public class ViewAllEmployeesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EmployeeAdapter adapter;
    private DatabaseHelper dbHelper;
    private List<Employee> employeeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_employees);

        dbHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadEmployees();
    }

    private void loadEmployees() {
        employeeList = dbHelper.getAllEmployees();
        adapter = new EmployeeAdapter(this, employeeList, new EmployeeAdapter.OnEmployeeClickListener() {
            @Override
            public void onEmployeeClick(Employee employee) {
                Intent intent = new Intent(ViewAllEmployeesActivity.this, UpdateEmployeeActivity.class);
                intent.putExtra("EMPLOYEE_ID", employee.getId());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadEmployees(); // Refresh list when returning
    }
}
