package com.example.employeemanagementsystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.employeemanagementsystem.Employee;
import com.example.employeemanagementsystem.User;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "EmployeeDB";
    private static final int DATABASE_VERSION = 2; // Increased version for new table

    // Employees Table
    private static final String TABLE_EMPLOYEES = "employees";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_AGE = "age";
    private static final String COLUMN_DEPARTMENT = "department";
    private static final String COLUMN_SALARY = "salary";

    // Users Table
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_EMAIL = "email";

    // Create Employees Table
    private static final String CREATE_EMPLOYEES_TABLE = "CREATE TABLE " + TABLE_EMPLOYEES + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_NAME + " TEXT,"
            + COLUMN_AGE + " INTEGER,"
            + COLUMN_DEPARTMENT + " TEXT,"
            + COLUMN_SALARY + " REAL"
            + ")";

    // Create Users Table
    private static final String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USERNAME + " TEXT UNIQUE,"
            + COLUMN_PASSWORD + " TEXT,"
            + COLUMN_EMAIL + " TEXT"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_EMPLOYEES_TABLE);
        db.execSQL(CREATE_USERS_TABLE);

        // Insert default admin user
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, "admin");
        values.put(COLUMN_PASSWORD, "admin123");
        values.put(COLUMN_EMAIL, "admin@company.com");
        db.insert(TABLE_USERS, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // ==================== USER AUTHENTICATION METHODS ====================

    // Add new user (for registration)
    public long addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, user.getUsername());
        values.put(COLUMN_PASSWORD, user.getPassword());
        values.put(COLUMN_EMAIL, user.getEmail());

        long id = db.insert(TABLE_USERS, null, values);
        db.close();
        return id;
    }

    // Check if user exists and credentials are correct
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_USER_ID},
                COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{username, password},
                null, null, null);

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    // Check if username already exists
    public boolean checkUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_USER_ID},
                COLUMN_USERNAME + "=?",
                new String[]{username},
                null, null, null);

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    // Get user by username
    public User getUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_USER_ID, COLUMN_USERNAME, COLUMN_PASSWORD, COLUMN_EMAIL},
                COLUMN_USERNAME + "=?",
                new String[]{username},
                null, null, null);

        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            user = new User(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3)
            );
            cursor.close();
        }
        db.close();
        return user;
    }

    // Update user password
    public int updateUserPassword(String username, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, newPassword);

        int rowsAffected = db.update(TABLE_USERS, values,
                COLUMN_USERNAME + "=?",
                new String[]{username});
        db.close();
        return rowsAffected;
    }

    // ==================== EMPLOYEE METHODS (UNCHANGED) ====================

    public long addEmployee(Employee employee) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, employee.getName());
        values.put(COLUMN_AGE, employee.getAge());
        values.put(COLUMN_DEPARTMENT, employee.getDepartment());
        values.put(COLUMN_SALARY, employee.getSalary());

        long id = db.insert(TABLE_EMPLOYEES, null, values);
        db.close();
        return id;
    }

    public Employee getEmployee(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EMPLOYEES,
                new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_AGE, COLUMN_DEPARTMENT, COLUMN_SALARY},
                COLUMN_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null);

        Employee employee = null;
        if (cursor != null && cursor.moveToFirst()) {
            employee = new Employee(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.getString(3),
                    cursor.getDouble(4)
            );
            cursor.close();
        }
        db.close();
        return employee;
    }

    public List<Employee> getAllEmployees() {
        List<Employee> employeeList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_EMPLOYEES;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Employee employee = new Employee(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getString(3),
                        cursor.getDouble(4)
                );
                employeeList.add(employee);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return employeeList;
    }

    public int updateEmployee(Employee employee) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, employee.getName());
        values.put(COLUMN_AGE, employee.getAge());
        values.put(COLUMN_DEPARTMENT, employee.getDepartment());
        values.put(COLUMN_SALARY, employee.getSalary());

        int rowsAffected = db.update(TABLE_EMPLOYEES, values,
                COLUMN_ID + "=?",
                new String[]{String.valueOf(employee.getId())});
        db.close();
        return rowsAffected;
    }

    public void deleteEmployee(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EMPLOYEES, COLUMN_ID + "=?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public int getEmployeeCount() {
        String countQuery = "SELECT * FROM " + TABLE_EMPLOYEES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }
}