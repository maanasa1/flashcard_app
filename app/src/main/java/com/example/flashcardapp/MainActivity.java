package com.example.flashcardapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView flashcard_question;
    TextView flashcard_answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flashcard_question = findViewById(R.id.flashcard_question_textview);
        flashcard_answer = findViewById(R.id.flashcard_answer_textview);

        flashcard_answer.setVisibility(View.INVISIBLE);

        flashcard_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flashcard_answer.setVisibility(View.VISIBLE);
                flashcard_question.setVisibility(View.INVISIBLE);
            }
        });

        flashcard_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flashcard_answer.setVisibility(View.INVISIBLE);
                flashcard_question.setVisibility(View.VISIBLE);
            }
        });

        ImageView add_flashcard = findViewById(R.id.add_flashcard_button);
        add_flashcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddCardActivity.class);
                //startActivity(intent);
                startActivityForResult(intent, 100);
            }
        });
    }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if(requestCode == 100){
                if(data != null) { //null check
                    String question_string = data.getExtras().getString("QUESTION KEY");
                    String answer_string = data.getExtras().getString("ANSWER KEY");
                    flashcard_question.setText(question_string);
                    flashcard_answer.setText(answer_string);
                }
        }
    }
}