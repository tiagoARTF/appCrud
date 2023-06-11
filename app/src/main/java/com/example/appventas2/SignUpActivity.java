package com.example.appventas2;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.database.Cursor;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {
    private EditText editTextFullName;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextKeyword;
    private Button buttonSignUp;
    private TextView textViewLogin;
    private DatabaseHelper databaseHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        databaseHelper = new DatabaseHelper(this);

        editTextFullName = findViewById(R.id.editTextFullName);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextKeyword = findViewById(R.id.editTextKeyword);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        textViewLogin = findViewById(R.id.textViewLogin);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = editTextFullName.getText().toString().trim();
                String username = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String keyword = editTextKeyword.getText().toString().trim();

                if (fullName.isEmpty() || username.isEmpty() || password.isEmpty() || keyword.isEmpty()) {
                    // Si algún campo está vacío, muestra un mensaje de error
                    Toast.makeText(SignUpActivity.this, "Por favor llenar todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    if (createUser(username, password, keyword)) {
                        // Registro exitoso
                        Toast.makeText(SignUpActivity.this, "Registro Exitoso", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error en el registro
                        Toast.makeText(SignUpActivity.this, "Registro fallido. El usuario ya existe", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean createUser(String username, String password, String keyword) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        boolean usernameTaken = isUsernameTaken(db, username); // Verificar si el nombre de usuario ya está registrado

        if (usernameTaken) {
            db.close();
            return false;
        }

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.UserEntry.COLUMN_USERNAME, username);
        values.put(DatabaseContract.UserEntry.COLUMN_PASSWORD, password);
        values.put(DatabaseContract.UserEntry.COLUMN_KEYWORD, keyword);

        long newRowId = db.insert(DatabaseContract.UserEntry.TABLE_NAME, null, values);
        db.close();

        return newRowId != -1;
    }

    private boolean isUsernameTaken(SQLiteDatabase db, String username) {
        String[] projection = {
                DatabaseContract.UserEntry.COLUMN_USERNAME
        };

        String selection = DatabaseContract.UserEntry.COLUMN_USERNAME + " = ?";
        String[] selectionArgs = {username};

        Cursor cursor = db.query(
                DatabaseContract.UserEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        boolean usernameTaken = cursor.getCount() > 0;
        cursor.close();

        return usernameTaken;
    }
}