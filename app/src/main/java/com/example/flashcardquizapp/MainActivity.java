
package com.example.flashcardquizapp;

import android.os.Bundle;
import android.app.AlertDialog;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView questionText, answerText, cardNumber;
    Button showAnswerBtn, nextBtn, previousBtn;
    Button addBtn, editBtn, deleteBtn;

    ArrayList<String> questions = new ArrayList<>();
    ArrayList<String> answers = new ArrayList<>();

    int currentIndex = 0;
    boolean answerVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Connect XML layout
        setContentView(R.layout.activity_main);

        questionText = findViewById(R.id.questionText);
        answerText = findViewById(R.id.answerText);
        cardNumber = findViewById(R.id.cardNumber);

        showAnswerBtn = findViewById(R.id.showAnswerBtn);
        nextBtn = findViewById(R.id.nextBtn);
        previousBtn = findViewById(R.id.previousBtn);
        addBtn = findViewById(R.id.addBtn);
        editBtn = findViewById(R.id.editBtn);
        deleteBtn = findViewById(R.id.deleteBtn);

        questions.add("What is Java?");
        answers.add("Java is an object-oriented programming language.");

        questions.add("What is Android?");
        answers.add("Android is a mobile operating system.");

        questions.add("What is XML?");
        answers.add("XML is used to structure data.");

        questions.add("What is Artificial Intelligence?");
        answers.add("AI means Artificial Intelligence.");

        displayCard();

        showAnswerBtn.setOnClickListener(v -> {
            answerVisible = !answerVisible;
            displayCard();
        });

        nextBtn.setOnClickListener(v -> {
            if (currentIndex < questions.size() - 1) {
                currentIndex++;
                answerVisible = false;
                displayCard();
            } else {
                Toast.makeText(this, "Last card",
                        Toast.LENGTH_SHORT).show();
            }
        });

        previousBtn.setOnClickListener(v -> {
            if (currentIndex > 0) {
                currentIndex--;
                answerVisible = false;
                displayCard();
            } else {
                Toast.makeText(this, "First card",
                        Toast.LENGTH_SHORT).show();
            }
        });

        addBtn.setOnClickListener(v -> showDialog(false));

        editBtn.setOnClickListener(v -> {
            if (!questions.isEmpty()) showDialog(true);
        });

        deleteBtn.setOnClickListener(v -> deleteCard());
    }

    private void displayCard() {

        if (questions.isEmpty()) {
            questionText.setText("No flashcards available");
            answerText.setText("");
            cardNumber.setText("Card 0");

            showAnswerBtn.setEnabled(false);
            nextBtn.setEnabled(false);
            previousBtn.setEnabled(false);
            editBtn.setEnabled(false);
            deleteBtn.setEnabled(false);
            return;
        }

        showAnswerBtn.setEnabled(true);
        nextBtn.setEnabled(true);
        previousBtn.setEnabled(true);
        editBtn.setEnabled(true);
        deleteBtn.setEnabled(true);

        questionText.setText(questions.get(currentIndex));

        answerText.setText(answerVisible
                ? answers.get(currentIndex) : "");

        showAnswerBtn.setText(answerVisible
                ? "Hide Answer" : "Show Answer");

        cardNumber.setText("Card " +
                (currentIndex + 1) + " / " + questions.size());
    }

    private void showDialog(boolean edit) {

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(35, 10, 35, 10);

        EditText q = new EditText(this);
        q.setHint("Enter question");

        EditText a = new EditText(this);
        a.setHint("Enter answer");

        if (edit) {
            q.setText(questions.get(currentIndex));
            a.setText(answers.get(currentIndex));
        }

        layout.addView(q);
        layout.addView(a);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(edit ? "Edit Flashcard" : "Add Flashcard")
                .setView(layout)
                .setPositiveButton("Save", null)
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(d -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setOnClickListener(v -> {

                        String question = q.getText().toString().trim();
                        String answer = a.getText().toString().trim();

                        if (question.isEmpty() || answer.isEmpty()) {
                            Toast.makeText(this,
                                    "Enter both fields",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (edit) {
                            questions.set(currentIndex, question);
                            answers.set(currentIndex, answer);
                        } else {
                            questions.add(question);
                            answers.add(answer);
                            currentIndex = questions.size() - 1;
                        }

                        answerVisible = false;
                        displayCard();
                        dialog.dismiss();
                    });
        });

        dialog.show();
    }

    private void deleteCard() {

        if (questions.isEmpty()) return;

        new AlertDialog.Builder(this)
                .setTitle("Delete Flashcard")
                .setMessage("Do you want to delete this card?")
                .setPositiveButton("Delete", (d, w) -> {

                    questions.remove(currentIndex);
                    answers.remove(currentIndex);

                    currentIndex = Math.min(
                            currentIndex, questions.size() - 1);

                    answerVisible = false;
                    displayCard();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
