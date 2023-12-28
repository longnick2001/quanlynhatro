package longpdph30373.poly.qunlnhtr.activities;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;

import longpdph30373.poly.qunlnhtr.MainActivity;
import longpdph30373.poly.qunlnhtr.R;

public class Rigister extends AppCompatActivity {

    EditText email, password, name, phone;
    LinearLayout btnRigister;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    CheckBox showPasswordCheckBox;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rigister);
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        phone = findViewById(R.id.phone);
        name = findViewById(R.id.name);
        btnRigister = findViewById(R.id.btnRigister);
        progressBar = findViewById(R.id.progressBar);
        showPasswordCheckBox = findViewById(R.id.showPasswordCheckBox);

        showPasswordCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                password.setInputType(InputType.TYPE_CLASS_TEXT);
            } else {
                password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });

        btnRigister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String emailStr = String.valueOf(email.getText()),
                        passStr = String.valueOf(password.getText()),
                nameStr = String.valueOf(name.getText()),
                phoneStr = String.valueOf(phone.getText());

                if(TextUtils.isEmpty(nameStr)){
                    Toast.makeText(Rigister.this, "Enter your name !", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(emailStr)){
                    Toast.makeText(Rigister.this, "Enter your email !", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(passStr)){
                    Toast.makeText(Rigister.this, "Enter your password !", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(phoneStr)){
                    Toast.makeText(Rigister.this, "Enter your phone number !", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(emailStr, passStr)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(Rigister.this, "Account created.",
                                            Toast.LENGTH_SHORT).show();
                                    AddUser(nameStr, emailStr, phoneStr, passStr);
                                    Intent intent = new Intent(Rigister.this, Login.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.

                                    Toast.makeText(Rigister.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

            }
        });
    }

    private void AddUser(String name, String email, String phone, String passStr) {
        HashMap<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("email", email);
        user.put("phone", phone);
        user.put("pass", passStr);

        // Write a message to the database
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("managers")
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(exception -> {
                    Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show();
                });
    }

}