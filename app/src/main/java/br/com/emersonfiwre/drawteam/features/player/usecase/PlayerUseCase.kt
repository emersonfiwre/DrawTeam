package br.com.emersonfiwre.drawteam.features.player.usecase

import br.com.emersonfiwre.drawteam.commons.model.PlayerModel
import br.com.emersonfiwre.drawteam.features.player.usecase.state.PlayerUseCaseState

interface PlayerUseCase {

    fun getPlayers(): PlayerUseCaseState.PlayerState

    fun addPlayer(name: String, goalKeeper: Boolean): PlayerUseCaseState.AddPlayerState

    fun deletePlayer(player: PlayerModel?): PlayerUseCaseState.DeletePlayerState
}