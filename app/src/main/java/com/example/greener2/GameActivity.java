package com.example.greener2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class GameActivity extends AppCompatActivity implements View.OnClickListener{

    final int PT_AWARD = 10;
    private TextView scoreText;
    private TextView questionText;
    private Button buttonA0;
    private Button buttonA1;
    private Button buttonA2;
    private Button buttonA3;
    private TextView correctText;
    private Button buttonNext;
    private TextView wantUserIDText;
    private TextView correctAnswerIDText;

    private DatabaseReference dbres, userres, usercountres, tempres, tempscoreres, a0res, a1res, a2res, a3res, qres, qcres, tempUserRes;
    private FirebaseDatabase fbdb;
    private DataSnapshot[] allQuestions, allUsers;
    private int nextIndex, nextUser;
    private int questionCount, userCount;
    private int currentScore;
    private int answerChoice;
    private String findEmail;
    private String wantUserID;
    private String correctAnswerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        scoreText = (TextView)this.findViewById(R.id.score_text);
        questionText = (TextView)this.findViewById(R.id.question_text);
        buttonA0 = (Button)this.findViewById(R.id.button_a0);
        buttonA1 = (Button)this.findViewById(R.id.button_a1);
        buttonA2 = (Button)this.findViewById(R.id.button_a2);
        buttonA3 = (Button)this.findViewById(R.id.button_a3);
        correctText = (TextView)this.findViewById(R.id.correct_text);
        buttonNext = (Button)this.findViewById(R.id.button_next);
        wantUserIDText = (TextView)this.findViewById(R.id.want_user_id);
        correctAnswerIDText = (TextView)this.findViewById(R.id.correct_answer_id);

        buttonA0.setOnClickListener(this);
        buttonA1.setOnClickListener(this);
        buttonA2.setOnClickListener(this);
        buttonA3.setOnClickListener(this);
        buttonNext.setOnClickListener(this);

        nextIndex = 0;
        buttonNext.setText("submit");
        answerChoice = -1;


        //also need to modify user score
        Intent i = getIntent();
        findEmail = i.getStringExtra("email");
        fbdb = FirebaseDatabase.getInstance();


        usercountres = fbdb.getReference("usercount");
        usercountres.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userCount = Integer.parseInt(dataSnapshot.getValue().toString());

                for(int j = 0; j < userCount; j++){
                    String resStr = "user" + j + "email";

                    tempres = fbdb.getReference(resStr);
                    final int finalJ = j;
                    tempres.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String tempEmail = dataSnapshot.getValue().toString();

                            if(tempEmail.equals(findEmail)){
                                //scoreText.setText(tempEmail);
                                tempscoreres = fbdb.getReference("user" + finalJ + "score");
                                wantUserIDText.setText(finalJ + "");

                                tempscoreres.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        scoreText.setText(dataSnapshot.getValue().toString());
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //now check all the users to see who has the right email
        //allUsers = new DataSnapshot[userCount];


//        userres = fbdb.getReference("users");
//        ValueEventListener userListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                userCount = (int)dataSnapshot.getChildrenCount();
//                allUsers = new DataSnapshot[userCount];
//                int i = 0;
//                for (DataSnapshot child : dataSnapshot.getChildren()) {
//                    allUsers[i++] = child;
//
//                    DataSnapshot dt1 = child.child("user_id");
//                    for(DataSnapshot ld2: dt1.getChildren()){
//                        DataSnapshot dt2 = ld2.child("email");
//                        String tempEmail = dt2.getValue(String.class); scoreText.setText(tempEmail);
//                        if(tempEmail.equals(findEmail))
//                            currentScore = dt2.getValue(Integer.class);
//                    }
//                }
//
//                //scoreText.setText(currentScore + "");
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w("Failed to read value.", error.toException());
//                questionText.setText("Something failed");
//            }
//        };
//        userres.addValueEventListener(userListener);
        //userres.removeEventListener(userListener);

        onClick(buttonNext);
    }

    @Override
    public void onClick(View v){

        if(v.getId() == R.id.button_next){

            //find total number of questions
            dbres = fbdb.getReference("questioncount");
            dbres.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    questionCount = Integer.parseInt(dataSnapshot.getValue().toString());
                    wantUserID = wantUserIDText.getText().toString();

                    qres = fbdb.getReference("q" + nextIndex + "q");
                    qres.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            questionText.setText(dataSnapshot.getValue().toString());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    a0res = fbdb.getReference("q" + nextIndex + "a0");
                    a0res.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            buttonA0.setText("1. " + dataSnapshot.getValue().toString());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    a1res = fbdb.getReference("q" + nextIndex + "a1");
                    a1res.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            buttonA1.setText("2. " + dataSnapshot.getValue().toString());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    a2res = fbdb.getReference("q" + nextIndex + "a2");
                    a2res.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            buttonA2.setText("3. " + dataSnapshot.getValue().toString());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    a3res = fbdb.getReference("q" + nextIndex + "a3");
                    a3res.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            buttonA3.setText("4. " + dataSnapshot.getValue().toString());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    //find right answer
                    qcres = fbdb.getReference("q" + nextIndex + "c");
                    qcres.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String correctID = dataSnapshot.getValue().toString();
                            String correctAnswer = Integer.parseInt(correctID) + 1 + "";
                            correctText.setText("Correct Answer: " + correctAnswer);
                            correctAnswerIDText.setText(correctID);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    if(answerChoice == -1){
                        buttonNext.setEnabled(false);
                        buttonNext.setText("Submit");
                        buttonA0.setBackgroundColor(getResources().getColor(R.color.common_google_signin_btn_text_dark_default));
                        buttonA1.setBackgroundColor(getResources().getColor(R.color.common_google_signin_btn_text_dark_default));
                        buttonA2.setBackgroundColor(getResources().getColor(R.color.common_google_signin_btn_text_dark_default));
                        buttonA3.setBackgroundColor(getResources().getColor(R.color.common_google_signin_btn_text_dark_default));
                        correctText.setTextColor(getResources().getColor(R.color.common_google_signin_btn_text_dark_default));

                    } else {
                        if(correctAnswerIDText.getText().toString().equals(answerChoice + "")){
                            //answers correctly, now award points
                            tempUserRes = fbdb.getReference("user" + wantUserID + "score");
                            ValueEventListener scoreListen = new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    int oldScore = Integer.parseInt(dataSnapshot.getValue().toString());
                                    oldScore += PT_AWARD;
                                    tempUserRes.setValue(oldScore + "");
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            };
                            tempUserRes.addListenerForSingleValueEvent(scoreListen);
                        }
                        buttonNext.setText("Next");
                        nextIndex = (int)(Math.random() * questionCount);
                        correctText.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                        answerChoice = -1;
                    }

                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("Failed to read value.", error.toException());
                    questionText.setText("Something failed");
                }
            });

        } else if(v.getId() == R.id.button_a0){
            answerChoice = 0;
            buttonNext.setEnabled(true);
            buttonA0.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            buttonA1.setBackgroundColor(getResources().getColor(R.color.common_google_signin_btn_text_dark_default));
            buttonA2.setBackgroundColor(getResources().getColor(R.color.common_google_signin_btn_text_dark_default));
            buttonA3.setBackgroundColor(getResources().getColor(R.color.common_google_signin_btn_text_dark_default));
        } else if(v.getId() == R.id.button_a1){
            answerChoice = 1;
            buttonNext.setEnabled(true);
            buttonA1.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            buttonA0.setBackgroundColor(getResources().getColor(R.color.common_google_signin_btn_text_dark_default));
            buttonA2.setBackgroundColor(getResources().getColor(R.color.common_google_signin_btn_text_dark_default));
            buttonA3.setBackgroundColor(getResources().getColor(R.color.common_google_signin_btn_text_dark_default));
        } else if(v.getId() == R.id.button_a2){
            answerChoice = 2;
            buttonNext.setEnabled(true);
            buttonA2.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            buttonA1.setBackgroundColor(getResources().getColor(R.color.common_google_signin_btn_text_dark_default));
            buttonA0.setBackgroundColor(getResources().getColor(R.color.common_google_signin_btn_text_dark_default));
            buttonA3.setBackgroundColor(getResources().getColor(R.color.common_google_signin_btn_text_dark_default));
        } else if(v.getId() == R.id.button_a3){
            answerChoice = 3;
            buttonNext.setEnabled(true);
            buttonA3.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            buttonA1.setBackgroundColor(getResources().getColor(R.color.common_google_signin_btn_text_dark_default));
            buttonA2.setBackgroundColor(getResources().getColor(R.color.common_google_signin_btn_text_dark_default));
            buttonA0.setBackgroundColor(getResources().getColor(R.color.common_google_signin_btn_text_dark_default));
        }
    }
}

