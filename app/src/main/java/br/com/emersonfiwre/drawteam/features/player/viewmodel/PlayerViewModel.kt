package br.com.emersonfiwre.drawteam.features.player.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.emersonfiwre.drawteam.commons.viewmodel.DrawTeamViewModel
import br.com.emersonfiwre.drawteam.commons.model.PlayerModel
import br.com.emersonfiwre.drawteam.features.player.usecase.PlayerUseCase
import br.com.emersonfiwre.drawteam.features.player.usecase.state.PlayerUseCaseState
import br.com.emersonfiwre.drawteam.features.player.viewmodel.mapper.PlayerViewModelMapper
import br.com.emersonfiwre.drawteam.features.player.viewmodel.viewstate.PlayerViewState
import kotlinx.coroutines.launch
import java.lang.Exception

class PlayerViewModel(
    private val useCase: PlayerUseCase
): DrawTeamViewModel() {

    private val playerState = MutableLiveData<PlayerViewState.PlayerListViewState>()
    val playerViewState: LiveData<PlayerViewState.PlayerListViewState>
        get() = playerState

    private val playerErrorState = MutableLiveData<PlayerViewState.PlayerError>()
    val playerErrorViewState: LiveData<PlayerViewState.PlayerError>
        get() = playerErrorState

    @Suppress("ToGenericExceptionCaught")
    fun setupPlayers() {
        coroutineScope.launch {
            try {
                val result = useCase.getPlayers()
                handlePlayer(result)
            } catch (ex: Exception) {
                Log.d(PlayerViewModel::javaClass.name, ex.message.toString())
                playerErrorState.value = PlayerViewState.PlayerError.ShowListError
            }
        }
    }

    @Suppress("ToGenericExceptionCaught")
    fun setupAddPlayers(name: String, goalkeeper: Boolean) {
        coroutineScope.launch {
            try {
                val result = useCase.addPlayer(name, goalkeeper)
                handleAddPlayer(result)
            } catch (ex: Exception) {
                Log.d(PlayerViewModel::javaClass.name, ex.message.toString())
                playerErrorState.value = PlayerViewState.PlayerError.ShowAddError
            }
        }
    }

    @Suppress("ToGenericExceptionCaught")
    fun setupDeletePlayer(playerModel: PlayerModel?) {
        coroutineScope.launch {
            try {
                val result = useCase.deletePlayer(playerModel)
                handleDeletePlayer(result)
            } catch (ex: Exception) {
                Log.d(PlayerViewModel::javaClass.name, ex.message.toString())
                playerErrorState.value = PlayerViewState.PlayerError.ShowDeleteError
            }
        }
    }

    private fun handlePlayer(result: PlayerUseCaseState.PlayerState) {
        when (result) {
            is PlayerUseCaseState.PlayerState.DisplayPlayers -> {
                playerState.value =
                    PlayerViewModelMapper.convertModelToPlayerViewTypeState(result.items)
            }

            PlayerUseCaseState.PlayerState.DisplayError -> {
                playerErrorState.value = PlayerViewState.PlayerError.ShowListError
            }
        }
    }

    private fun handleAddPlayer(result: PlayerUseCaseState.AddPlayerState) {
        when (result) {
            PlayerUseCaseState.AddPlayerState.DisplayError -> {
                playerErrorState.value = PlayerViewState.PlayerError.ShowAddError
            }

            PlayerUseCaseState.AddPlayerState.DisplaySuccess -> {
                playerState.value =
                    PlayerViewState.PlayerListViewState.ShowSuccessAdd
            }
        }
    }

    private fun handleDeletePlayer(result: PlayerUseCaseState.DeletePlayerState) {
        when (result) {
            PlayerUseCaseState.DeletePlayerState.DisplayError -> {
                playerErrorState.value = PlayerViewState.PlayerError.ShowDeleteError
            }

            PlayerUseCaseState.DeletePlayerState.DisplaySuccess -> {
                playerState.value =
                    PlayerViewState.PlayerListViewState.ShowSuccessAdd
            }
        }
    }
}