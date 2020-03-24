package com.example.practicequizapp;

import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private int mScore;
    private int flag=0;
    private TextView mTextViewQuestion;
    private TextView mTextViewScore;
    private int mIndex;
    private Button mButtonTrue;
    private Button mButtonFalse;
    private ProgressBar mProgressBar;
    private String[] mQuestionBank;
    private String[] mAnswerBank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mQuestionBank = getResources().getStringArray(R.array.question_bank);
        mAnswerBank = getResources().getStringArray(R.array.answer_bank);
        final int noOfQuestion = mAnswerBank.length;
        final boolean[] answer = new boolean[noOfQuestion];
        int i = 0;
        for (String s : mAnswerBank) {
            if (s.equals("true")) answer[i] = true;
            else answer[i] = false;
            i++;
        }
        mProgressBar = findViewById(R.id.score_bar);
        mTextViewQuestion = findViewById(R.id.question_text);
        mTextViewScore = findViewById(R.id.score_text);
        mButtonTrue = findViewById(R.id.true_button);
        mButtonFalse = findViewById(R.id.false_button);

        if(savedInstanceState!=null){
            mIndex=savedInstanceState.getInt("IndexKey");
            mScore=savedInstanceState.getInt("ScoreKey");
            flag=savedInstanceState.getInt("Flag");
            if(flag==1) finish();
        }
        else{
            mScore=0;
            mIndex=0;
        }

        mTextViewQuestion.setText(mQuestionBank[mIndex]);
        mTextViewScore.setText("Score " + mScore + "/15");


        mButtonTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true,answer);
                updateQuestion(noOfQuestion);
            }
        });

        mButtonFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false,answer);
                updateQuestion(noOfQuestion);
            }
        });
    }

    private void checkAnswer(boolean userChoice,boolean[] answer){
        if(userChoice==answer[mIndex]) {
            mScore++;
            Toast.makeText(this,R.string.correct_toast,Toast.LENGTH_SHORT).show();
        }
        else Toast.makeText(this,R.string.incorrect_toast,Toast.LENGTH_SHORT).show();
    }

    private void updateQuestion(int noOfQuestion){

        mIndex=(mIndex+1)%noOfQuestion;

        if(mIndex==0){
            flag=1;
            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

            alert.setTitle("Game Over");
            alert.setCancelable(false);
            alert.setMessage("You scored " + mScore + " points!");
            alert.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            alert.setNegativeButton("restart", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mIndex=0;
                    mScore=0;
                    mTextViewQuestion.setText(mQuestionBank[mIndex]);
                    mTextViewScore.setText("Score " + mScore + "/15");
                    mProgressBar.setProgress(0);
                }
            });
            alert.show();
            mTextViewScore.setText("Score " + mScore + "/15");
            mProgressBar.setProgress(100);
        }
        else {
            mTextViewScore.setText("Score " + mScore + "/15");
            mTextViewQuestion.setText(mQuestionBank[mIndex]);
            mProgressBar.setProgress((int) Math.ceil(((mIndex) * 100.0) / 15.0));
        }

    }
    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

        outState.putInt("Flag",flag);
        outState.putInt("ScoreKey", mScore);
        outState.putInt("IndexKey", mIndex);
    }
}
