package aps.backflip.curlylab.presentation.hairTyping.screen.texttyping.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import aps.backflip.curlylab.data.remote.model.request.profile.HairTypeRequest
import aps.backflip.curlylab.domain.repository.profile.HairTypesRepository
import aps.backflip.curlylab.domain.repository.profile.UsersRepository
import aps.backflip.curlylab.presentation.hairTyping.screen.texttyping.Answer
import aps.backflip.curlylab.presentation.hairTyping.screen.texttyping.Question
import aps.backflip.curlylab.presentation.hairTyping.screen.texttyping.ThicknessTypes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ThicknessTextTypingViewModel @Inject constructor(
    private val hairTypesRepository: HairTypesRepository,
    private val usersRepository: UsersRepository
) : ViewModel() {

    private val _questions = MutableStateFlow<List<Question>>(porosityQuestions)
    val questions: StateFlow<List<Question>> = _questions.asStateFlow()

    private val _currentQuestionId = MutableStateFlow<Int>(0)
    val currentQuestionId: StateFlow<Int> = _currentQuestionId.asStateFlow()

    private val _result = MutableStateFlow<String?>(null)
    val result: StateFlow<String?> = _result.asStateFlow()

    private val _saved = MutableStateFlow<Boolean?>(null)
    val saved: StateFlow<Boolean?> = _saved.asStateFlow()

    fun answering(answerId: Int, questionId: Int) {
        _questions.value = _questions.value.mapIndexed { idQuestion, question ->
            if (idQuestion == questionId) {
                Question(
                    question.topic,
                    question.answers.mapIndexed { id, it ->
                        Answer(
                            text = it.text,
                            isSelected = id == answerId,
                            result = it.result
                        )
                    }
                )
            } else {
                question
            }
        }
    }

    fun nextQuestion() {
        _currentQuestionId.value += 1
    }

    fun previousQuestion() {
        _currentQuestionId.value -= 1
    }

    fun getResult() {
        var resultString = ""
        _questions.value.forEach {
            resultString += it.answers.first { it.isSelected }.result
        }
        val thin = resultString.count { it.toString() == ThicknessTypes.THIN.code }
        val medium = resultString.count { it.toString() == ThicknessTypes.MEDIUM.code }
        val bold = resultString.count { it.toString() == ThicknessTypes.BOLD.code }

        val max = maxOf(thin, medium, bold)

        _result.value = when (max) {
            bold -> ThicknessTypes.BOLD.result
            thin -> ThicknessTypes.THIN.result
            else -> ThicknessTypes.MEDIUM.result
        }
    }

    fun saveResult() {
        viewModelScope.launch {
            try {
                val userId = getCurrentUserId().toString()
                try {
                    hairTypesRepository.updateHairType(
                        userId,
                        HairTypeRequest(
                            thickness = _result.value.toString()
                        )
                    )
                    _saved.value = true
                } catch (e: Exception) {
                    _saved.value = false
                }
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    private suspend fun getCurrentUserId(): UUID {
        return withContext(Dispatchers.IO) {
            usersRepository.getCurrentUserId()
        }
    }

    companion object {
        val porosityQuestions = listOf(
            Question(
                topic = "Возьмите карандаш/ручку, намотайте волос виток к витку. Ширину всех витков поделите на число витков",
                answers = listOf(
                    Answer(
                        ThicknessTypes.BOLD.code,
                        "Более 0,07 мм",
                        true
                    ),
                    Answer(
                        ThicknessTypes.MEDIUM.code,
                        "0,05-0,07 мм",
                        false
                    ),
                    Answer(
                        ThicknessTypes.THIN.code,
                        "Менее 0,05 мм",
                        false
                    ),
                    Answer(
                        "",
                        "Я не умею/не хочу считать",
                        false
                    ),
                )
            ),
            Question(
                topic = "Поднесите волос к окну и рассмотри на свет",
                answers = listOf(
                    Answer(
                        ThicknessTypes.BOLD.code,
                        "Волосок широкий, крупный, его отчётливо видно",
                        true
                    ),
                    Answer(
                        ThicknessTypes.THIN.code,
                        "Волос настолько тонкий, что его еле заметно на свету",
                        false
                    ),
                    Answer(
                        ThicknessTypes.MEDIUM.code,
                        "Что-то среднее",
                        false
                    )
                )
            )
        )
    }
}