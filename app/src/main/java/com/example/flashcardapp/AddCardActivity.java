package com.example.flashcardapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.view.View;
import android.widget.EditText;

public class AddCardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        ImageView cancelButton = findViewById(R.id.cancelButton);
        ImageView saveButton = findViewById(R.id.saveButton);

        cancelButton.setOnClickListener(view -> {
            finish();
        });

        saveButton.setOnClickListener(view -> {
            Intent data = new Intent();
            String input_question = ((EditText) findViewById(R.id.flashcardQuestionEditText)).getText().toString();
            String input_answer = ((EditText) findViewById(R.id.flashcardAnswerEditText)).getText().toString();
            data.putExtra("QUESTION KEY", input_question);
            data.putExtra("ANSWER KEY", input_answer);
            setResult(RESULT_OK, data);
            finish();
        });

    }
}

