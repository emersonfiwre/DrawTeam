package br.com.emersonfiwre.drawteam.features.drawplayers.usecase

import br.com.emersonfiwre.drawteam.features.drawplayers.usecase.state.DrawPlayerUseCaseState
import br.com.emersonfiwre.drawteam.commons.model.PlayerModel

interface DrawPlayersUseCase {

    suspend fun getPlayers(): DrawPlayerUseCaseState.PlayerListState

    fun setupDrawPlayers(players: List<PlayerModel>): DrawPlayerUseCaseState.TeamShuffledState

    suspend fun setupResetSelection(): DrawPlayerUseCaseState.PlayerListState

    fun foundPlayerByName(
        parameter: String,
        items: List<PlayerModel>?
    ): DrawPlayerUseCaseState.PlayerListState
}
