package com.example.ps1_basic;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    public static final String KEY_EXTRA_ANSWER = "pl.edu.pb.wi.quiz.correctAnswer";
    private Button trueButton;
    private Button falseButton;
    private Button nextButton;
    private Button promptButton;
    private TextView questionTextView;
    private int currentIndex = 0;
    private static final String KEY_CURRENT_INDEX="currentIndex";
    private static final int REQUEST_CODE_PROMPT = 0;
    private boolean answerWasShown = false;


    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        Log.d("PS", "OnSaveInstanceState");
        outState.putInt(KEY_CURRENT_INDEX, currentIndex);
    }

    private Question[] questions = new Question[]{
            new Question(R.string.q_day, false),
            new Question(R.string.q_food, false),
            new Question(R.string.q_person, true),
            new Question(R.string.q_pizza, false),
            new Question(R.string.q_year, false)
    };


    private void checkAnswerCorrectness(boolean userAnswer){
        boolean correctAnswer = questions[currentIndex].isTrueAnswer();
        int resultMessegeId=0;
        if (answerWasShown){
            resultMessegeId=R.string.answer_was_shown;
        }
        else {
            if (userAnswer == correctAnswer) {
                resultMessegeId = R.string.correct_answer;
            } else {
                resultMessegeId = R.string.incorrect_answer;
            }
        }
        Toast.makeText(this,resultMessegeId,Toast.LENGTH_SHORT).show();
    }
    private void setNextQuestion(){
        answerWasShown=false;
        questionTextView.setText(questions[currentIndex].getQuestionId());
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("PS", "Create");
        setContentView(R.layout.activity_main);
        if(savedInstanceState!=null) {
            currentIndex = savedInstanceState.getInt(KEY_CURRENT_INDEX);
        }
        questionTextView=findViewById(R.id.queston);
        trueButton = findViewById(R.id.button_true);
        falseButton = findViewById(R.id.button_false);
        nextButton = findViewById(R.id.button_next);
        promptButton = findViewById(R.id.button_prompt);
        setNextQuestion();
        promptButton.setOnClickListener((v) -> {
            Intent intent = new Intent(MainActivity.this, PromptActivity.class);
            boolean correctAnswer = questions[currentIndex].isTrueAnswer();
            intent.putExtra(KEY_EXTRA_ANSWER, correctAnswer);
            startActivityForResult(intent,REQUEST_CODE_PROMPT);
        });
        trueButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                checkAnswerCorrectness(true);
            }
        });
        falseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                checkAnswerCorrectness(false);
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                currentIndex=(currentIndex+1)%questions.length;
                setNextQuestion();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode != RESULT_OK){    return;}
        if(requestCode==REQUEST_CODE_PROMPT){
            if(data==null){     return; }
            answerWasShown=data.getBooleanExtra(PromptActivity.KEY_EXTRA_ANSWER_SHOWN,false);
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.d("PS", "Start");

    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d("PS", "Resume");

    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.d("PS", "Pause");

    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.d("PS", "Stop");

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d("PS", "Destroy");

    }

    
}