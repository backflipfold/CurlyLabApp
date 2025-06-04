package aps.backflip.curlylab.presentation.hairTyping.screen.texttyping.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import aps.backflip.curlylab.data.remote.model.request.profile.HairTypeRequest
import aps.backflip.curlylab.domain.repository.profile.HairTypesRepository
import aps.backflip.curlylab.domain.repository.profile.UsersRepository
import aps.backflip.curlylab.presentation.hairTyping.screen.texttyping.Answer
import aps.backflip.curlylab.presentation.hairTyping.screen.texttyping.PorosityTypes
import aps.backflip.curlylab.presentation.hairTyping.screen.texttyping.Question
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
class PorosityTextTypingViewModel @Inject constructor(
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
        val porous = resultString.count { it.toString() == PorosityTypes.POROUS.code }
        val semiPorous = resultString.count { it.toString() == PorosityTypes.SEMI_POROUS.code }
        val nonPorous = resultString.count { it.toString() == PorosityTypes.NON_POROUS.code }

        val max = maxOf(porous, semiPorous, nonPorous)

        _result.value = when (max) {
            porous -> PorosityTypes.POROUS.result
            semiPorous -> PorosityTypes.SEMI_POROUS.result
            else -> PorosityTypes.NON_POROUS.result
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
                            porosity = _result.value.toString()
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
                topic = "Проведите пальцами по волоску от кончика к коже головы.",
                answers = listOf(
                    Answer(
                        PorosityTypes.POROUS.code + PorosityTypes.SEMI_POROUS.code,
                        "Чувствую сопротивление",
                        true
                    ),
                    Answer(
                        PorosityTypes.SEMI_POROUS.code + PorosityTypes.NON_POROUS.code,
                        "Не чувствую сопротивление",
                        false
                    ),
                )
            ),
            Question(
                topic = "Какие Ваши волосы наощупь и на вид?",
                answers = listOf(
                    Answer(
                        PorosityTypes.NON_POROUS.code,
                        "Скользкие и гладкие, на вид –– блестящие, буквально отражающие свет",
                        true
                    ),
                    Answer(PorosityTypes.SEMI_POROUS.code, "Гладкие и блестящие", false),
                    Answer(
                        PorosityTypes.POROUS.code,
                        "На ощупь шершавые, визуально матовые, не блестят",
                        false
                    )
                )
            ),
            Question(
                topic = "Хорошо ли Ваши волосы держат завиток?",
                answers = listOf(
                    Answer(PorosityTypes.POROUS.code + PorosityTypes.SEMI_POROUS.code, "Да", true),
                    Answer(PorosityTypes.NON_POROUS.code, "Нет", false),
                )
            ),
            Question(
                topic = "Пушаться ли Ваши волосы?",
                answers = listOf(
                    Answer(PorosityTypes.POROUS.code + PorosityTypes.SEMI_POROUS.code, "Да", true),
                    Answer(PorosityTypes.NON_POROUS.code, "Нет", false),
                )
            ),
            Question(
                topic = "Насколько хорошо Ваши волосы держат кудри и прикорневой объем?",
                answers = listOf(
                    Answer(
                        PorosityTypes.NON_POROUS.code,
                        "Плохо, без дополнительных фиксирующих средств совсем не держат",
                        true
                    ),
                    Answer(
                        PorosityTypes.SEMI_POROUS.code,
                        "Укладка может держаться несколько дней",
                        false
                    ),
                    Answer(
                        PorosityTypes.POROUS.code,
                        "Отлично держат форму, хороший прикорневой объем",
                        false
                    )
                )
            ),
            Question(
                topic = "Как ведут себя Ваши волосы после мытья?",
                answers = listOf(
                    Answer(
                        PorosityTypes.NON_POROUS.code,
                        "Во влажном состоянии могут виться, но после высыхания распрямляются",
                        true
                    ),
                    Answer(PorosityTypes.SEMI_POROUS.code, "Пушаться и лохматяться", false),
                    Answer(
                        PorosityTypes.POROUS.code,
                        "Если лечь спать с влажными волосами, на утро будет колтун и валенок",
                        false
                    )
                )
            )
        )
    }
}