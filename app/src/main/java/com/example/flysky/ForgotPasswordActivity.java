package com.example.flysky;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText emailInput;
    Button resetBtn;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();
        emailInput = findViewById(R.id.emailInput);
        resetBtn = findViewById(R.id.resetBtn);

        resetBtn.setOnClickListener(v -> sendResetEmail());
    }

    private void sendResetEmail() {
        String email = emailInput.getText().toString().trim();

        if(email.isEmpty()) {
            Toast.makeText(this, "נא להזין את האימייל", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(unused ->
                        Toast.makeText(this, "נשלח אימייל לאיפוס סיסמה", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "שגיאה: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
