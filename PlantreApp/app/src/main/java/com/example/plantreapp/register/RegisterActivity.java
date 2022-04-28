package com.example.plantreapp.register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.plantreapp.R;
import com.example.plantreapp.api.APIClient;
import com.example.plantreapp.api.User;
import com.example.plantreapp.login.LoginActivity;

import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;

public class RegisterActivity extends AppCompatActivity {
    private Button buttonRegister;
    private APIClient apiClient;

    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextUsername;
    private EditText editTextFirstname;
    private EditText editTextLastname;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set layout
        setContentView(R.layout.activity_register);

        // Create API client
        APIClient.Companion.invoke(getApplicationContext());
        apiClient = new APIClient(getApplicationContext());

        // Get views
        buttonRegister = findViewById(R.id.buttonRegister);
        editTextEmail = findViewById(R.id.textEmail);
        editTextFirstname = findViewById(R.id.textFirstname);
        editTextLastname = findViewById(R.id.textLastname);
        editTextUsername = findViewById(R.id.textUsername);
        editTextPassword = findViewById(R.id.textPassword);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User(editTextUsername.getText().toString(), editTextFirstname.getText().toString(), editTextLastname.getText().toString(), editTextEmail.getText().toString(), editTextPassword.getText().toString());
                apiClient.registerUser(user, new Continuation<User>() {
                    @NonNull
                    @Override
                    public CoroutineContext getContext() {
                        return EmptyCoroutineContext.INSTANCE;
                    }

                    @Override
                    public void resumeWith(@NonNull Object o) {
                        if (o != null) {
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    }
                });
            }
        });
    }
}
