package com.example.appventas2;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;
import android.os.Bundle;


public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
    }

    public void onRegisterButtonClick(View view) {
        Intent intent = new Intent(HomeActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    public void onLoginButtonClick(View view) {
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
