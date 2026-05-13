package com.example.employeemanagementsystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.employeemanagementsystem.R;
import com.example.employeemanagementsystem.Employee;
import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {

    private Context context;
    private List<Employee> employeeList;
    private OnEmployeeClickListener listener;

    public interface OnEmployeeClickListener {
        void onEmployeeClick(Employee employee);
    }

    public EmployeeAdapter(Context context, List<Employee> employeeList, OnEmployeeClickListener listener) {
        this.context = context;
        this.employeeList = employeeList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_employee, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
        Employee employee = employeeList.get(position);
        holder.tvEmployeeId.setText("ID: " + employee.getId());
        holder.tvName.setText("Name: " + employee.getName());
        holder.tvDepartment.setText("Department: " + employee.getDepartment());
        holder.tvSalary.setText("Salary: ₹" + String.format("%.2f", employee.getSalary()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onEmployeeClick(employee);
            }
        });
    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }

    static class EmployeeViewHolder extends RecyclerView.ViewHolder {
        TextView tvEmployeeId, tvName, tvDepartment, tvSalary;

        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmployeeId = itemView.findViewById(R.id.tvEmployeeId);
            tvName = itemView.findViewById(R.id.tvName);
            tvDepartment = itemView.findViewById(R.id.tvDepartment);
            tvSalary = itemView.findViewById(R.id.tvSalary);
        }
    }
}
