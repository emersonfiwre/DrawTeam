package br.com.emersonfiwre.drawteam.features.player.viewmodel.viewstate

import br.com.emersonfiwre.drawteam.features.player.model.PlayerViewType

sealed class PlayerViewState {

    sealed class PlayerListViewState {

        data class ShowPlayers(val players: List<PlayerViewType>): PlayerListViewState()
        object ShowSuccessAdd: PlayerListViewState()
    }

    sealed class PlayerError {
        object ShowListError: PlayerError()
        object ShowAddError: PlayerError()
        object ShowDeleteError: PlayerError()
    }
}
