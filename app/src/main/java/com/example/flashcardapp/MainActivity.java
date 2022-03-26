package com.example.flashcardapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView flashcardQuestion;
    TextView flashcardAnswer;
    FlashcardDatabase flashcardDatabase;
    List<Flashcard> allFlashcards;
    int prevIndex = -10;
    int currIndex;

    public int getRandomNumber(int minNumber, int maxNumber) {
        if(maxNumber == 0) {
            return minNumber;
        }
        Random rand = new Random();
        currIndex = rand.nextInt((maxNumber - minNumber) + 1) + minNumber;
        while(currIndex == prevIndex){
            currIndex = rand.nextInt((maxNumber - minNumber) + 1) + minNumber;
        }
        prevIndex = currIndex;
        return currIndex;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flashcardQuestion = findViewById(R.id.flashcardQuestionTextview);
        flashcardAnswer = findViewById(R.id.flashcardAnswerTextview);
        flashcardAnswer.setVisibility(View.INVISIBLE);
        findViewById(R.id.edit_flashcard_button).setVisibility(View.INVISIBLE);

        flashcardQuestion.setOnClickListener(view -> {
            flashcardAnswer.setVisibility(View.VISIBLE);
            flashcardQuestion.setVisibility(View.INVISIBLE);
        });

        flashcardAnswer.setOnClickListener(view -> {
            flashcardAnswer.setVisibility(View.INVISIBLE);
            flashcardQuestion.setVisibility(View.VISIBLE);
        });

        ImageView addFlashcard = findViewById(R.id.add_flashcard_button);
        addFlashcard.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddCardActivity.class);
            startActivityForResult(intent, 100);
        });

        flashcardDatabase = new FlashcardDatabase(getApplicationContext());
        allFlashcards = flashcardDatabase.getAllCards();

        if (allFlashcards != null && allFlashcards.size() > 0) {
            Flashcard currentCard = allFlashcards.get(0);
            flashcardQuestion.setText(currentCard.getQuestion());
            flashcardAnswer.setText(currentCard.getAnswer());
        }

        findViewById(R.id.next_flashcard_button).setOnClickListener(view -> {
            if (allFlashcards == null || allFlashcards.size() == 0) {
                return;
            }

            allFlashcards = flashcardDatabase.getAllCards();
            Flashcard currentCard = allFlashcards.get(getRandomNumber(0, allFlashcards.size() - 1));
            flashcardQuestion.setText(currentCard.getQuestion());
            flashcardAnswer.setText(currentCard.getAnswer());

            if(flashcardAnswer.getVisibility() == View.VISIBLE){
                flashcardAnswer.setVisibility(View.INVISIBLE);
                flashcardQuestion.setVisibility(View.VISIBLE);
            }
        });

        findViewById(R.id.delete_flashcard_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flashcardDatabase.deleteCard(((TextView) findViewById(R.id.flashcardQuestionTextview)).getText().toString());
                allFlashcards = flashcardDatabase.getAllCards();

                Flashcard currentCard = allFlashcards.get(getRandomNumber(0, allFlashcards.size() - 1));
                flashcardQuestion.setText(currentCard.getQuestion());
                flashcardAnswer.setText(currentCard.getAnswer());
            }
        });

    }
        @Override
        protected void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 100) {
                if (data != null) { //null check
                    String questionString = data.getExtras().getString("QUESTION KEY");
                    String answerString = data.getExtras().getString("ANSWER KEY");
                    flashcardQuestion.setText(questionString);
                    flashcardAnswer.setText(answerString);

                    Flashcard flashcard = (new Flashcard(questionString, answerString));
                    flashcardDatabase.insertCard(flashcard);
                    allFlashcards = flashcardDatabase.getAllCards();
                }
            }
        }
}
