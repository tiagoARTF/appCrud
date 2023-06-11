package com.example.appventas2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import com.example.appventas2.DatabaseContract;
import com.example.appventas2.DatabaseHelper;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText editTextUsername;
    private EditText editTextKeyword;
    private EditText editTextNewPassword;
    private Button buttonResetPassword;
    private DatabaseHelper databaseHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);

        databaseHelper = new DatabaseHelper(this);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextKeyword = findViewById(R.id.editTextKeyword);
        editTextNewPassword = findViewById(R.id.editTextNewPassword);
        buttonResetPassword = findViewById(R.id.buttonResetPassword);

        buttonResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString().trim();
                String keyword = editTextKeyword.getText().toString().trim();
                String newPassword = editTextNewPassword.getText().toString().trim();

                if (resetPassword(username, keyword, newPassword)) {
                    // Contraseña actualizada con éxito
                    Toast.makeText(ForgotPasswordActivity.this, "Password reset successful", Toast.LENGTH_SHORT).show();
                    // Realiza alguna acción adicional, como volver a la pantalla de inicio de sesión
                    Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Error al actualizar la contraseña
                    Toast.makeText(ForgotPasswordActivity.this, "Failed to reset password. Please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean resetPassword(String username, String keyword, String newPassword) {
        // Lógica para actualizar la contraseña en la base de datos
        // Asegúrate de implementar correctamente esta lógica según tus requisitos
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.UserEntry.COLUMN_PASSWORD, newPassword);

        String selection = DatabaseContract.UserEntry.COLUMN_USERNAME + " = ? AND " +
                DatabaseContract.UserEntry.COLUMN_KEYWORD + " = ?";
        String[] selectionArgs = {username, keyword};

        int rowsAffected = db.update(DatabaseContract.UserEntry.TABLE_NAME, values, selection, selectionArgs);
        db.close();

        return rowsAffected > 0;
    }
}


