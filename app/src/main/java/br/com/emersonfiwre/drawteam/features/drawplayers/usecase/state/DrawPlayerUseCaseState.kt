package br.com.emersonfiwre.drawteam.features.drawplayers.usecase.state

import br.com.emersonfiwre.drawteam.commons.model.TeamModel
import br.com.emersonfiwre.drawteam.commons.model.PlayerModel

object DrawPlayerUseCaseState {
    sealed class PlayerListState {
        data class DisplayPlayers(
            val items: List<PlayerModel>,
            val itemsSelected: Int
        ): PlayerListState()

        object DisplayEmptyPlayers: PlayerListState()

        object DisplayNotFoundedPlayers: PlayerListState()

        object DisplayError: PlayerListState()
    }

    sealed class TeamShuffledState {
        data class DisplayTeams(
            val teams: List<TeamModel>,
        ): TeamShuffledState()

        object DisplayNotPlayersEnough: TeamShuffledState()

        object DisplayNotPlayersSelected: TeamShuffledState()

        object DisplayError: TeamShuffledState()
    }
}