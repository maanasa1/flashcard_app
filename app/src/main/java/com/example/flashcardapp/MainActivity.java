package com.example.flashcardapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.animation.Animator;


import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView flashcardQuestion;
    TextView flashcardAnswer;
    FlashcardDatabase flashcardDatabase;
    List<Flashcard> allFlashcards;
    int prevIndex = -10;
    int currIndex;
    final int ADD_CARD_REQUEST_CODE = 100;
    final int EDIT_CARD_REQUEST_CODE = 150;

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

        flashcardQuestion.setOnClickListener(view -> {
            // get the center for the clipping circle
            int cx = flashcardAnswer.getWidth() / 2;
            int cy = flashcardAnswer.getHeight() / 2;

            // get the final radius for the clipping circle
            float finalRadius = (float) Math.hypot(cx, cy);

            // create the animator for this view (the start radius is zero)
            Animator anim = ViewAnimationUtils.createCircularReveal(flashcardAnswer, cx, cy, 0f, finalRadius);
            flashcardAnswer.setVisibility(View.VISIBLE);
            flashcardQuestion.setVisibility(View.INVISIBLE);

            anim.setDuration(1000);
            anim.start();
        });

        flashcardAnswer.setOnClickListener(view -> {
            flashcardAnswer.setVisibility(View.INVISIBLE);
            flashcardQuestion.setVisibility(View.VISIBLE);
        });

        ImageView addFlashcard = findViewById(R.id.add_flashcard_button);
        addFlashcard.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddCardActivity.class);
            startActivityForResult(intent, ADD_CARD_REQUEST_CODE);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        });
        
        editFlashcard.setOnClickListener(view -> {
            Intent cardToEdit = new Intent(MainActivity.this, AddCardActivity.class);

            String questionToEdit = flashcardQuestion.getText().toString();
            String answerToEdit = flashcardAnswer.getText().toString();

            cardToEdit.putExtra("questionToEdit", questionToEdit);
            cardToEdit.putExtra("answerToEdit", answerToEdit);

            startActivityForResult(cardToEdit, EDIT_CARD_REQUEST_CODE);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
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

            final Animation leftOutAnim = AnimationUtils.loadAnimation(view.getContext(), R.anim.left_out);
            final Animation rightInAnim = AnimationUtils.loadAnimation(view.getContext(), R.anim.right_in);

            leftOutAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    // this method is called when the animation first starts
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    // this method is called when the animation is finished playing
                    flashcardQuestion.startAnimation(rightInAnim);

                    allFlashcards = flashcardDatabase.getAllCards();
                    Flashcard currentCard = allFlashcards.get(getRandomNumber(0, allFlashcards.size() - 1));
                    flashcardQuestion.setText(currentCard.getQuestion());
                    flashcardAnswer.setText(currentCard.getAnswer());

                    if(flashcardAnswer.getVisibility() == View.VISIBLE){
                        flashcardAnswer.setVisibility(View.INVISIBLE);
                        flashcardQuestion.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    // we don't need to worry about this method
                }
            });
            flashcardQuestion.startAnimation(leftOutAnim);
        });

        findViewById(R.id.delete_flashcard_button).setOnClickListener((View v) -> {
            flashcardDatabase.deleteCard(((TextView) findViewById(R.id.flashcardQuestionTextview)).getText().toString());
            allFlashcards = flashcardDatabase.getAllCards();
            Flashcard currentCard = allFlashcards.get(getRandomNumber(0, allFlashcards.size() - 1));
            flashcardQuestion.setText(currentCard.getQuestion());
            flashcardAnswer.setText(currentCard.getAnswer());
        });

    }
        @Override
        protected void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == ADD_CARD_REQUEST_CODE && resultCode == RESULT_OK) {
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
            
            if(requestCode == EDIT_CARD_REQUEST_CODE && resultCode == RESULT_OK){
                if (data != null) { //null check
                    String questionString = data.getExtras().getString("QUESTION KEY");
                    String answerString = data.getExtras().getString("ANSWER KEY");
                    flashcardQuestion.setText(questionString);
                    flashcardAnswer.setText(answerString);

                    Flashcard flashcard = (new Flashcard(questionString, answerString));
                    flashcardDatabase.updateCard(flashcard);
                    allFlashcards = flashcardDatabase.getAllCards();
                }
            }
        }
}
