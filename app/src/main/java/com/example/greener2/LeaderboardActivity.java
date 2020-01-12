package com.example.greener2;

import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.TreeMap;

public class LeaderboardActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText groupCodeText;
    private Button searchButton;
    private Button createButton;
    private TextView loggingView;
    private TextView groupNameView;
    private LinearLayout membersLayout;

    private DatabaseReference dbres, tempres, tempresm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        groupCodeText = (EditText)this.findViewById(R.id.group_code_text);
        searchButton = (Button)this.findViewById(R.id.button_search);
        createButton = (Button)this.findViewById(R.id.button_create);
        loggingView = (TextView)this.findViewById(R.id.logging_view);
        groupNameView = (TextView)this.findViewById(R.id.group_name_text);
        membersLayout = (LinearLayout)this.findViewById(R.id.members_layout);

        searchButton.setOnClickListener(this);
        createButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v){
        if(v.getId() == R.id.button_search){
            final Map<Integer, Integer> memberMap = new TreeMap<>();
            //does not error check yet, just pulls out the right group
            dbres = FirebaseDatabase.getInstance().getReference("g" + groupCodeText.getText().toString());
            dbres.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for(DataSnapshot child: dataSnapshot.getChildren()){
                        final int key = Integer.parseInt(child.getValue().toString()); //groupNameView.setText(key + "");

                        tempres = FirebaseDatabase.getInstance().getReference("user" + key + "score");
                        tempres.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                int points = Integer.parseInt(dataSnapshot.getValue().toString());
                                loggingView.setText(loggingView.getText().toString() + points + " " + key + " ");
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("Failed to read value.", error.toException());
                }
            });

            String[] numstrs = loggingView.getText().toString().split("\\s+");
            for(int at = 0; at < numstrs.length / 2; at++){
                memberMap.put(new Integer(Integer.parseInt(numstrs[at])), new Integer(Integer.parseInt(numstrs[at + 1])));
            }

            groupNameView.setText(numstrs.length + "");
            final int[] i = {0};
            for(final Integer key: memberMap.keySet()){

                tempresm = FirebaseDatabase.getInstance().getReference("user" + key.intValue() + "name");
                tempresm.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String name = dataSnapshot.getValue().toString();
                        TextView memberline = new TextView(membersLayout.getContext());
                        memberline.setText(++i[0] + ". " + name + " " + memberMap.get(key));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

        } else if(v.getId() == R.id.button_create){
//            Intent i = new Intent(this, RegisterActivity.class);
//            startActivity(i);
        }
    }
}

