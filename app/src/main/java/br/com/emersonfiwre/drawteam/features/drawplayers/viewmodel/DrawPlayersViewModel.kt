package br.com.emersonfiwre.drawteam.features.drawplayers.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.emersonfiwre.drawteam.commons.viewmodel.DrawTeamViewModel
import br.com.emersonfiwre.drawteam.features.drawplayers.usecase.DrawPlayersUseCase
import br.com.emersonfiwre.drawteam.features.drawplayers.usecase.state.DrawPlayerUseCaseState
import br.com.emersonfiwre.drawteam.features.drawplayers.viewmodel.viewstate.DrawPlayersViewState
import br.com.emersonfiwre.drawteam.commons.model.PlayerModel
import br.com.emersonfiwre.drawteam.features.player.viewmodel.PlayerViewModel
import kotlinx.coroutines.launch

class DrawPlayersViewModel(
    private val useCase: DrawPlayersUseCase
): DrawTeamViewModel() {

    private val playerListState = MutableLiveData<DrawPlayersViewState.PlayerListViewState>()
    val playerListViewState: LiveData<DrawPlayersViewState.PlayerListViewState>
        get() = playerListState

    private val playerListFoundedState =
        MutableLiveData<DrawPlayersViewState.PlayerListFoundedViewState>()
    val playerListFoundedViewState: LiveData<DrawPlayersViewState.PlayerListFoundedViewState>
        get() = playerListFoundedState

    private val shuffledPlayersState =
        MutableLiveData<DrawPlayersViewState.ShuffledPlayersViewState>()
    val shuffledPlayersViewState: LiveData<DrawPlayersViewState.ShuffledPlayersViewState>
        get() = shuffledPlayersState

    private val drawErrorState = MutableLiveData<DrawPlayersViewState.DrawPlayersError>()
    val drawErrorViewState: LiveData<DrawPlayersViewState.DrawPlayersError>
        get() = drawErrorState

    @Suppress("ToGenericExceptionCaught")
    fun getPlayers() {
        coroutineScope.launch {
            try {
                val result = useCase.getPlayers()
                handleListPlayer(result)
            } catch (ex: Exception) {
                Log.d(PlayerViewModel::javaClass.name, ex.message.toString())
                drawErrorState.value = DrawPlayersViewState.DrawPlayersError.ShowPlayerListError
            }
        }
    }

    @Suppress("ToGenericExceptionCaught")
    fun setupDrawPlayers(playerList: List<PlayerModel>) {
        coroutineScope.launch {
            try {
                val result = useCase.setupDrawPlayers(playerList)
                handleTeams(result)
            } catch (ex: Exception) {
                Log.d(PlayerViewModel::javaClass.name, ex.message.toString())
                drawErrorState.value = DrawPlayersViewState.DrawPlayersError.ShowDrawError
            }
        }
    }

    @Suppress("ToGenericExceptionCaught")
    fun setupResetSelection() {
        coroutineScope.launch {
            try {
                val result = useCase.setupResetSelection()
                handleListPlayer(result)
            } catch (ex: Exception) {
                Log.d(PlayerViewModel::javaClass.name, ex.message.toString())
                drawErrorState.value = DrawPlayersViewState.DrawPlayersError.ShowDrawError
            }
        }
    }

    @Suppress("ToGenericExceptionCaught")
    fun filterByKeywords(parameter: String, items: List<PlayerModel>?) {
        coroutineScope.launch {
            try {
                val result = useCase.foundPlayerByName(parameter, items)
                handleListPlayersFounded(result)
            } catch (ex: Exception) {
                Log.d(PlayerViewModel::javaClass.name, ex.message.toString())
                drawErrorState.value = DrawPlayersViewState.DrawPlayersError.ShowSearchError
            }
        }
    }

    private fun handleTeams(result: DrawPlayerUseCaseState.TeamShuffledState) {
        when (result) {
            DrawPlayerUseCaseState.TeamShuffledState.DisplayError -> {
                drawErrorState.value = DrawPlayersViewState.DrawPlayersError.ShowDrawError
            }

            is DrawPlayerUseCaseState.TeamShuffledState.DisplayTeams -> {
                shuffledPlayersState.value =
                    DrawPlayersViewState.ShuffledPlayersViewState.ShowTeams(result.teams)
            }

            DrawPlayerUseCaseState.TeamShuffledState.DisplayNotPlayersEnough -> {
                shuffledPlayersState.value =
                    DrawPlayersViewState.ShuffledPlayersViewState.ShowWithoutEnoughPlayers
            }

            DrawPlayerUseCaseState.TeamShuffledState.DisplayNotPlayersSelected -> {
                shuffledPlayersState.value =
                    DrawPlayersViewState.ShuffledPlayersViewState.ShowNoPlayersSelected
            }
        }
    }

    private fun handleListPlayer(result: DrawPlayerUseCaseState.PlayerListState) {
        when (result) {
            DrawPlayerUseCaseState.PlayerListState.DisplayError -> {
                drawErrorState.value = DrawPlayersViewState.DrawPlayersError.ShowPlayerListError
            }

            is DrawPlayerUseCaseState.PlayerListState.DisplayPlayers -> {
                playerListState.value =
                    DrawPlayersViewState.PlayerListViewState.ShowPlayers(
                        result.items,
                        result.itemsSelected
                    )
            }

            DrawPlayerUseCaseState.PlayerListState.DisplayEmptyPlayers -> {
                playerListState.value =
                    DrawPlayersViewState.PlayerListViewState.ShowEmptyState
            }

            DrawPlayerUseCaseState.PlayerListState.DisplayNotFoundedPlayers -> {
                // Not yet implemented
            }
        }
    }

    private fun handleListPlayersFounded(result: DrawPlayerUseCaseState.PlayerListState) {
        when (result) {
            DrawPlayerUseCaseState.PlayerListState.DisplayError -> {
                drawErrorState.value = DrawPlayersViewState.DrawPlayersError.ShowSearchError
            }

            is DrawPlayerUseCaseState.PlayerListState.DisplayPlayers -> {
                playerListFoundedState.value =
                    DrawPlayersViewState.PlayerListFoundedViewState.ShowPlayers(result.items)
            }

            DrawPlayerUseCaseState.PlayerListState.DisplayEmptyPlayers -> {
                playerListFoundedState.value =
                    DrawPlayersViewState.PlayerListFoundedViewState.ShowEmptyState
            }

            DrawPlayerUseCaseState.PlayerListState.DisplayNotFoundedPlayers -> {
                playerListFoundedState.value =
                    DrawPlayersViewState.PlayerListFoundedViewState.ShowNotPlayersFoundedState
            }
        }
    }
}