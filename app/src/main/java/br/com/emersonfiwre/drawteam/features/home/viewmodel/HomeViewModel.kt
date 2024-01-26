package br.com.emersonfiwre.drawteam.features.home.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.emersonfiwre.drawteam.commons.viewmodel.DrawTeamViewModel
import br.com.emersonfiwre.drawteam.commons.model.TeamModel
import br.com.emersonfiwre.drawteam.features.home.usecase.HomeUseCase
import br.com.emersonfiwre.drawteam.features.home.usecase.state.HomeUseCaseState
import br.com.emersonfiwre.drawteam.features.home.viewmodel.mapper.HomeViewModelMapper
import br.com.emersonfiwre.drawteam.features.home.viewmodel.viewstate.HomeViewState
import br.com.emersonfiwre.drawteam.features.player.viewmodel.PlayerViewModel
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

    @Suppress("ToGenericExceptionCaught")
    fun setupListTeams(teamList: List<TeamModel>) {
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
}