package br.com.emersonfiwre.drawteam.features.drawplayers.usecase

import br.com.emersonfiwre.drawteam.features.drawplayers.usecase.state.DrawPlayerUseCaseState
import br.com.emersonfiwre.drawteam.commons.model.PlayerModel

interface DrawPlayersUseCase {

    fun getPlayers(): DrawPlayerUseCaseState.PlayerListState

    fun setupDrawPlayers(players: List<PlayerModel>): DrawPlayerUseCaseState.TeamShuffledState

    fun foundPlayerByName(
        parameter: String,
        items: List<PlayerModel>?
    ): DrawPlayerUseCaseState.PlayerListState
}