package com.example.greener2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText emailEditReg;
    private EditText passwordEditReg;
    private EditText usernameEditReg;
    private Button submitButtonReg;
    private Button loginButton;

    private FirebaseAuth auth;
    private FirebaseDatabase fbdb;
    private DatabaseReference tempres, tempmapres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        emailEditReg = (EditText)this.findViewById(R.id.email_register);
        passwordEditReg = (EditText)this.findViewById(R.id.password_register);
        usernameEditReg = (EditText)this.findViewById(R.id.username_register);
        submitButtonReg = (Button)this.findViewById(R.id.button_register);
        submitButtonReg.setOnClickListener(this);
        loginButton = (Button)this.findViewById(R.id.Login);
        loginButton.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();
        fbdb = FirebaseDatabase.getInstance();
    }

    @Override
    public void onClick(View v){
        if(v.getId() == submitButtonReg.getId()){
            final String email = emailEditReg.getText().toString().trim();
            final String username = usernameEditReg.getText().toString().trim();
            String password = passwordEditReg.getText().toString().trim();

            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Toast.makeText(RegisterActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                            if (!task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Authentication failed." + task.getException(),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Success!",
                                        Toast.LENGTH_SHORT).show();

                                //now add: name, email, score to fbdb, update usercount

                                //1. update count
                               tempres = fbdb.getReference("usercount");
                                tempres.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        int usercount = Integer.parseInt(dataSnapshot.getValue().toString()) + 1;
                                        tempres.setValue(usercount + "");

                                        tempmapres = fbdb.getReference();
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("user" + usercount + "email", email);
                                        map.put("user" + usercount + "name", username);
                                        map.put("user" + usercount + "score", new Integer(0));
                                        tempmapres.updateChildren(map);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                //2. add name

                                //3. add email

                            }
                            Intent i = new Intent(RegisterActivity.this, MenuActivity.class);
                            i.putExtra("email", email);
                            startActivity(i);
                        }
                    });
        } else if(v.getId() == R.id.Login){
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
    }
}

