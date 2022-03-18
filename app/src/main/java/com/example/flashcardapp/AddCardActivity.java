package com.example.flashcardapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.view.View;
import android.widget.EditText;

public class AddCardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        ImageView cancel_button = findViewById(R.id.cancel_button);
        ImageView save_button = findViewById(R.id.save_button);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent();
                String input_question = ((EditText) findViewById(R.id.flashcard_question_edittext)).getText().toString();
                String input_answer = ((EditText) findViewById(R.id.flashcard_answer_edittext)).getText().toString();
                data.putExtra("QUESTION KEY", input_question);
                data.putExtra("ANSWER KEY", input_answer);
                setResult(RESULT_OK, data);
                finish();
            }
        });

    }

}

