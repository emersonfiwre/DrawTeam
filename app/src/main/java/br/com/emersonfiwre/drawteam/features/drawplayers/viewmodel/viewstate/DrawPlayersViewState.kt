package br.com.emersonfiwre.drawteam.features.drawplayers.viewmodel.viewstate

import br.com.emersonfiwre.drawteam.commons.model.TeamModel
import br.com.emersonfiwre.drawteam.commons.model.PlayerModel

sealed class DrawPlayersViewState {

    sealed class PlayerListViewState {
        data class ShowPlayers(val players: List<PlayerModel>): PlayerListViewState()
        object ShowEmptyState: PlayerListViewState()
    }

    sealed class PlayerListFoundedViewState {
        data class ShowPlayers(val players: List<PlayerModel>): PlayerListFoundedViewState()
        object ShowEmptyState: PlayerListFoundedViewState()
    }

    sealed class ShuffledPlayersViewState {
        data class ShowTeams(val teams: List<TeamModel>): ShuffledPlayersViewState()
        object ShowWithoutEnoughPlayers: ShuffledPlayersViewState()
        object ShowNoPlayersSelected: ShuffledPlayersViewState()
    }

    sealed class DrawPlayersError {
        object ShowDrawError: DrawPlayersError()
        object ShowPlayerListError: DrawPlayersError()
        object ShowSearchError: DrawPlayersError()
    }
}
