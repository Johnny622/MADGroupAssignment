package my.edu.utar.groupassignment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import java.util.List;
import java.util.Objects;

public class past_year_discussion extends AppCompatActivity {

    private TextEmbeddingsViewModel textEmbeddingsViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_year_discussion);

        // Initialize ViewModel
        textEmbeddingsViewModel = new ViewModelProvider(this).get(TextEmbeddingsViewModel.class);

        // Set up ML model
        textEmbeddingsViewModel.setUpMLModel(this);

        EditText userInputEditText = findViewById(R.id.userInput);
        Button checkSimilaritiesButton = findViewById(R.id.checkSimilarities);
        TextView resultTextView = findViewById(R.id.result);

        checkSimilaritiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userInput = userInputEditText.getText().toString().trim();
                if (!userInput.isEmpty()) {
                    // Get sentences list from ViewModel
                    List<String> sentences = Objects.requireNonNull(textEmbeddingsViewModel.getUiStateTextEmbeddings().getValue()).getSentences();
                    // Calculate similarity
                    textEmbeddingsViewModel.calculateSimilarity(userInput, sentences);
                } else {
                    Toast.makeText(past_year_discussion.this, "Please enter some text.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Observe LiveData for UI updates
        textEmbeddingsViewModel.getUiStateTextEmbeddings().observe(this, textEmbeddingsUiState -> {
            switch (textEmbeddingsUiState.getState()) {
                case Loading:
                    resultTextView.setText("Calculating similarities...");
                    break;
                case Success:
                    List<SentenceSimilarity> similaritySentences = textEmbeddingsUiState.getSimilaritySentences();
                    if (!similaritySentences.isEmpty()) {
                        SentenceSimilarity firstSimilarity = similaritySentences.get(0);
                        resultTextView.setText("Similarity: " + (int) (firstSimilarity.getResultSimilarity() * 100) + "%");
                    } else {
                        resultTextView.setText("No similar sentences found.");
                    }
                    break;
                case Error:
                    resultTextView.setText("Error: " + textEmbeddingsUiState.getErrorMessage());
                    break;
                case Empty:
                default:
                    resultTextView.setText("");
            }
        });
    }
}