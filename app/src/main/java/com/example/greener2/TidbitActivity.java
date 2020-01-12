package com.example.greener2;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class TidbitActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tidbitView;
    private Button shuffleButton;
    private DatabaseReference dbres;
    private String[] allTidbits;
    private int nextIndex;
    private int tidbitCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tidbit);
        tidbitView = (TextView)this.findViewById(R.id.tidbitView);
        shuffleButton = (Button)this.findViewById(R.id.button_shuffle);
        shuffleButton.setOnClickListener(this);
        nextIndex = 0;

        dbres = FirebaseDatabase.getInstance().getReference("tidbits");
        dbres.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tidbitCount = (int)dataSnapshot.getChildrenCount();
                allTidbits = new String[tidbitCount];
                int i = 0;
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    allTidbits[i++] = child.getValue().toString();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Failed to read value.", error.toException());
                tidbitView.setText("Something failed");
            }
        });
    }

    @Override
    public void onClick(View v){
        if(v.getId() == shuffleButton.getId()){
            tidbitView.setText(allTidbits[nextIndex]);
            nextIndex = (int)(Math.random() * tidbitCount);
        } else if(v.getId() == R.id.Register){
//            Intent i = new Intent(this, RegisterActivity.class);
//            startActivity(i);
        }
    }
}

