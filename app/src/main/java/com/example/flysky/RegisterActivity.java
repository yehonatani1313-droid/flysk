package com.example.flysky;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText phoneInput, emailInput, passwordInput, confirmPasswordInput;
    Button continueBtn;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // אינסטנס Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // חיבור שדות מה־XML
        phoneInput = findViewById(R.id.phone);
        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPassword);
        continueBtn = findViewById(R.id.continueBtn);

        continueBtn.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String phone = phoneInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString();
        String confirm = confirmPasswordInput.getText().toString();

        // בדיקת שדות ריקים
        if (phone.isEmpty() || email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            Toast.makeText(this, "נא למלא את כל השדות", Toast.LENGTH_SHORT).show();
            return;
        }

        // בדיקת התאמת סיסמאות
        if (!password.equals(confirm)) {
            Toast.makeText(this, "הסיסמאות לא תואמות", Toast.LENGTH_SHORT).show();
            return;
        }

        // יצירת משתמש ב-Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    Toast.makeText(this, "נרשמת בהצלחה", Toast.LENGTH_SHORT).show();

                    // שמירת פרטי המשתמש ב-Firestore
                    Map<String, Object> user = new HashMap<>();
                    user.put("email", email);
                    user.put("phone", phone);

                    // UID ייחודי לכל משתמש
                    db.collection("users")
                            .document(mAuth.getCurrentUser().getUid())
                            .set(user)
                            .addOnSuccessListener(aVoid ->
                                    Toast.makeText(this, "פרטי המשתמש נשמרו ב-Firestore", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e ->
                                    Toast.makeText(this, "שגיאה בשמירה: " + e.getMessage(), Toast.LENGTH_SHORT).show());

                    // חזרה למסך ההתחברות
                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "שגיאה: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
