package br.com.emersonfiwre.drawteam.features.home.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.com.emersonfiwre.drawteam.commons.viewmodel.DrawTeamViewModel
import br.com.emersonfiwre.drawteam.commons.model.TeamModel
import br.com.emersonfiwre.drawteam.features.home.usecase.HomeUseCase
import br.com.emersonfiwre.drawteam.features.home.usecase.state.HomeUseCaseState
import br.com.emersonfiwre.drawteam.features.home.viewmodel.mapper.HomeViewModelMapper
import br.com.emersonfiwre.drawteam.features.home.viewmodel.viewstate.HomeViewState
import br.com.emersonfiwre.drawteam.features.player.viewmodel.PlayerViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeViewModel(
    private val useCase: HomeUseCase
): DrawTeamViewModel() {

    private val homeListOfTeamState = MutableLiveData<HomeViewState.HomeListOfTeamViewState>()
    val homeListOfTeamViewState: LiveData<HomeViewState.HomeListOfTeamViewState>
        get() = homeListOfTeamState

    private val homeErrorState = MutableLiveData<HomeViewState.HomeError>()
    val playerErrorViewState: LiveData<HomeViewState.HomeError>
        get() = homeErrorState

    private val _timeMillis = MutableLiveData(0L)
    val timeMillis: LiveData<Long> = _timeMillis

    private val _isRunning = MutableLiveData(false)
    val isRunning: LiveData<Boolean> = _isRunning

    val formattedTime: MediatorLiveData<String> = object: MediatorLiveData<String>() {
        private var lastKnownTimeMillis = -1L

        init {
            addSource(_timeMillis) { newTimeMillis ->
                if (newTimeMillis != lastKnownTimeMillis) {
                    lastKnownTimeMillis = newTimeMillis
                    value = formatMillis(newTimeMillis)
                }
            }
        }

        private fun formatMillis(millis: Long): String {
            val totalSeconds = millis / 1000
            val minutes = (totalSeconds / 60).toString().padStart(2, '0')
            val seconds = (totalSeconds % 60).toString().padStart(2, '0')
            val centiSeconds =
                (millis % 1000 / 10).toString().padStart(2, '0') // if needed centiSeconds
            return "$minutes:$seconds"
        }
    }

    private var timerJob: Job? = null

    @Suppress("TooGenericExceptionCaught")
    fun setupListTeams(teamList: List<TeamModel>?) {
        coroutineScope.launch {
            try {
                val result = useCase.setupPresentationTeams(teamList)
                handleListTeams(result)
            } catch (ex: Exception) {
                Log.d(PlayerViewModel::javaClass.name, ex.message.toString())
                homeErrorState.value = HomeViewState.HomeError.ShowPresentationError
            }
        }
    }

    fun startTimer() {
        if (_isRunning.value == true) return
        _isRunning.value = true
        timerJob = viewModelScope.launch {
            val startTime = System.currentTimeMillis() - (_timeMillis.value ?: 0L)
            while (_isRunning.value == true) {
                _timeMillis.postValue(System.currentTimeMillis() - startTime)
                delay(10)
            }
        }
    }

    fun stopTimer() {
        _isRunning.value = false
        timerJob?.cancel()
    }

    private fun resetTimer() {
        stopTimer()
        _timeMillis.value = 0L
    }

    fun toggleOrReset() {
        if (_isRunning.value == true) {
            resetTimer()
        } else {
            startTimer()
        }
    }

    private fun handleListTeams(result: HomeUseCaseState.TeamPresentationState) {
        when (result) {
            HomeUseCaseState.TeamPresentationState.DisplayError -> {
                homeErrorState.value = HomeViewState.HomeError.ShowPresentationError
            }

            is HomeUseCaseState.TeamPresentationState.DisplayTeams -> {
                homeListOfTeamState.value =
                    HomeViewModelMapper.convertModelToTeamViewTypeState(result.teams)
            }

            is HomeUseCaseState.TeamPresentationState.DisplayTwoTeams -> {
                homeListOfTeamState.value =
                    HomeViewModelMapper.convertModelToTwoTeamViewTypeState(
                        result.team, result.secondTeam
                    )
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
