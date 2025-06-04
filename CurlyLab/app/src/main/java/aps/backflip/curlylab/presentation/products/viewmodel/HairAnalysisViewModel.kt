package aps.backflip.curlylab.presentation.products.viewmodel
import javax.inject.Inject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import aps.backflip.curlylab.data.remote.model.request.profile.HairTypeRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import aps.backflip.curlylab.domain.repository.profile.AnalysisRepository
import aps.backflip.curlylab.domain.repository.profile.HairTypesRepository
import aps.backflip.curlylab.domain.repository.profile.UsersRepository
import aps.backflip.curlylab.presentation.hairTyping.screen.texttyping.PorosityTypes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.util.UUID
import android.util.Log

@HiltViewModel
class HairAnalysisViewModel @Inject constructor(
    private val repository: AnalysisRepository,
    private val hairTypesRepository: HairTypesRepository,
    private val usersRepository: UsersRepository
) : ViewModel() {

    private val _result = MutableStateFlow<String?>(null)
    val result: StateFlow<String?> = _result

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _saved = MutableStateFlow<Boolean?>(null)
    val saved: StateFlow<Boolean?> = _saved.asStateFlow()

    fun analyze(imageBytes: ByteArray) {
        viewModelScope.launch {
            try {
                val porosity = repository.analyzePhoto(imageBytes)
                _result.value = porosity
            } catch (e: Exception) {
                _result.value = "Ошибка анализа: ${e.message}"
            }
        }
    }

    fun saveResult() {
        viewModelScope.launch {
            try {
                val userId = getCurrentUserId().toString()
                try {
                    var result = _result.value.toString()
                    Log.e("VIK", "$result")
                    result = when(result) {
                        "medium" -> {
                            PorosityTypes.SEMI_POROUS.result
                        }

                        "high" -> {
                            PorosityTypes.POROUS.result
                        }

                        else -> PorosityTypes.NON_POROUS.result
                    }
                    hairTypesRepository.updateHairType(
                        userId,
                        HairTypeRequest(
                            porosity = result
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
}
