package com.example.ps1_basic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PromptActivity extends AppCompatActivity {
    private boolean correctAnswer;
    private Button showButton;
    private TextView promptTextView;

    public static final String KEY_EXTRA_ANSWER_SHOWN = "pb.edu.pl.wi.quiz.answerShown";

    private void setAnswerShownResult(boolean answerWasShown){
        Intent resaultIntent = new Intent();
        resaultIntent.putExtra(KEY_EXTRA_ANSWER_SHOWN, answerWasShown);
        setResult(RESULT_OK,resaultIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prompt);

        promptTextView=findViewById(R.id.promptText);
        showButton = findViewById(R.id.button_show);

        correctAnswer=getIntent().getBooleanExtra(MainActivity.KEY_EXTRA_ANSWER, true);

        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int answer = correctAnswer?R.string.button_true:R.string.button_false;
                promptTextView.setText(answer);
                setAnswerShownResult(true);
            }
        });
    }
}