package br.com.emersonfiwre.drawteam.features.drawplayers.repository

import br.com.emersonfiwre.drawteam.commons.model.PlayerModel

interface DrawPlayersRepository {

    suspend fun getPlayersBySession(): List<PlayerModel>

    suspend fun resetPlayers(): List<PlayerModel>

    fun applySelectedPlayers(players: List<PlayerModel>)
}
