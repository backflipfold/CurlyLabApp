package aps.backflip.curlylab.presentation.composition.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import aps.backflip.curlylab.data.remote.api.CompositionApi
import aps.backflip.curlylab.domain.model.composition.AnalysisResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CompositionViewModel : ViewModel() {

    private val _inputText = mutableStateOf("")
    val inputText: State<String> = _inputText

    private val _result = MutableStateFlow("")
    val result: StateFlow<String> = _result

    fun onInputTextChange(newText: String) {
        _inputText.value = newText
    }

    fun analyze(context: Context, imageUri: Uri?) {
        _result.value = "Анализируем..."
        viewModelScope.launch {
            try {
                val res: AnalysisResult =
                    CompositionApi.analyze(context, _inputText.value, imageUri)
                _result.value = if (res.issues.isNullOrEmpty()) {
                    "✔ Всё в порядке!"
                } else {
                    val list = res.issues.joinToString("\n\n") { issue ->
                        "❌ ${issue.ingredient}\nКатегория: ${issue.category}\nПричина: ${issue.reason}"
                    }
                    "Обнаружены проблемы:\n\n$list"
                }
            } catch (e: Exception) {
                _result.value = "Ошибка: ${e.message}"
            }
        }
    }
}
