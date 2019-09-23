package com.pkg.tintincustomer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText mobileno;
    Button verify;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mobileno = findViewById(R.id.login_mobileno);
        verify = findViewById(R.id.login_button);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = mobileno.getText().toString().trim();
                if (number.isEmpty() || number.length() < 10 || number.length() >13) {
                    mobileno.setError("Valid number is required");
                    mobileno.requestFocus();
                    return;
                }
                String phonenumber = number;
                Intent intent = new Intent(LoginActivity.this, VerifyActivity.class);
                intent.putExtra("phonenumber",phonenumber);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

}
