package com.example.greener2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener{

    private Button tidbitButton, gameButton, leaderboardButton, sleepButton, logoutButton;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Intent intent = getIntent();
        email = intent.getStringExtra("email");

        tidbitButton = (Button)this.findViewById(R.id.button_tidbit);
        gameButton = (Button)this.findViewById(R.id.button_game);
        leaderboardButton = (Button)this.findViewById(R.id.button_leaderboard);
        sleepButton = (Button)this.findViewById(R.id.button_sleep);
        logoutButton = (Button)this.findViewById(R.id.button_logout);

        tidbitButton.setOnClickListener(this);
        gameButton.setOnClickListener(this);
        leaderboardButton.setOnClickListener(this);
        sleepButton.setOnClickListener(this);
        logoutButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v){
        if(v.getId() == R.id.button_tidbit){
            Intent i = new Intent(this, TidbitActivity.class);
            i.putExtra("email", email);
            startActivity(i);
        } else if(v.getId() == R.id.button_game){
            Intent i = new Intent(this, GameActivity.class);
            i.putExtra("email", email);
            startActivity(i);
        } else if(v.getId() == R.id.button_leaderboard){
            Intent i = new Intent(this, LeaderboardActivity.class);
            i.putExtra("email", email);
            startActivity(i);
        } else if(v.getId() == R.id.button_sleep){
            Intent i = new Intent(this, RegisterActivity.class);
            i.putExtra("email", email);
            startActivity(i);
        } else if(v.getId() == R.id.button_logout){
            Intent i = new Intent(this, RegisterActivity.class);
            //i.putExtra("email", email); //probably don't need
            startActivity(i);
        }
    }
}

