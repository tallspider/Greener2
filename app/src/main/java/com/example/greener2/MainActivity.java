package com.example.greener2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText emailEdit;
    private EditText passwordEdit;
    private Button submitButton;
    private Button registerButton;
    private FirebaseAuth auth;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        emailEdit = (EditText)this.findViewById(R.id.email);
        passwordEdit = (EditText)this.findViewById(R.id.password);
        submitButton = (Button)this.findViewById(R.id.button);
        submitButton.setOnClickListener(this);
        registerButton = (Button)this.findViewById(R.id.Register);
        registerButton.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v){
        if(v.getId() == submitButton.getId()){
            email = emailEdit.getText().toString();
            final String password = passwordEdit.getText().toString();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                return;
            }

            //authenticate user
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                // there was an error
                                if (password.length() < 6) {
                                    passwordEdit.setError("Wrong");
                                } else {
                                    Toast.makeText(MainActivity.this, "Wrong.", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Intent i = new Intent(MainActivity.this, MenuActivity.class);
                                i.putExtra("email", email);
                                startActivity(i);
                            }
                        }
                    });
        } else if(v.getId() == R.id.Register){
            Intent i = new Intent(this, RegisterActivity.class);
            startActivity(i);
        }
    }
}

