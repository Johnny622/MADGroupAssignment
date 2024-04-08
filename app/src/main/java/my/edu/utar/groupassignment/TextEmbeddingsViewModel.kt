package my.edu.utar.groupassignment;

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.withContext

//sealed class State {
//    object Loading : State()
//    object Success : State()
//    object Error : State()
//    object Empty : State()
//}

data class TextEmbeddingsUiState(
    val sentences: List<String> = emptyList(),
    val similaritySentences: List<SentenceSimilarity> = emptyList(),
    val state: State = State.Empty,
    val errorMessage: String = ""
)

enum class State {
    Loading,
    Success,
    Error,
    Empty
}

class TextEmbeddingsViewModel : ViewModel() {

    private lateinit var mediaPipeEmbeddings: MediaPipeEmbeddings
    val uiStateTextEmbeddings = MutableLiveData<TextEmbeddingsUiState>()

    fun setUpMLModel(context: Context) {
        mediaPipeEmbeddings = MediaPipeEmbeddings()
        uiStateTextEmbeddings.value = TextEmbeddingsUiState(state = State.Loading)
        viewModelScope.launch {
            try {
                mediaPipeEmbeddings.setUpMLModel(context)
                val sentences = listOf(
                    "The next weekend will be your birthday",
                    "Birds sing in the morning",
                    "My cat sleeps a lot",
                    "Pizza is my favorite food",
                    "The sun is hot",
                    "I like to play with my dog",
                    "Flowers are colorful",
                    "Books take us on adventures",
                    "The moon comes out at night",
                    "Ice cream melts quickly in the sun",
                    "Soccer is a fun game",
                    "My mom makes yummy cookies",
                    "Cars go fast on the road",
                    "I can count to ten",
                    "Rainbows have many colors",
                    "I love watching cartoons"
                )
                uiStateTextEmbeddings.value = TextEmbeddingsUiState(sentences = sentences, state = State.Empty)
            } catch (e: Exception) {
                uiStateTextEmbeddings.value = TextEmbeddingsUiState(state = State.Error, errorMessage = "Error setting up ML model")
            }
        }
    }

    fun calculateSimilarity(mainSentence: String, sentences: List<String>) {
        uiStateTextEmbeddings.value = TextEmbeddingsUiState(state = State.Loading)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val similarities = mediaPipeEmbeddings.getSimilarities(mainSentence, sentences)
                withContext(Dispatchers.Main) {
                    uiStateTextEmbeddings.value = TextEmbeddingsUiState(similaritySentences = similarities, state = State.Success)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    uiStateTextEmbeddings.value = TextEmbeddingsUiState(state = State.Error, errorMessage = "Error getting similarities")
                }
            }
        }
    }

}