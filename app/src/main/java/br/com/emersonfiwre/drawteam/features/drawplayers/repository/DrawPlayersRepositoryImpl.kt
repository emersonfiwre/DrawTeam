package br.com.emersonfiwre.drawteam.features.drawplayers.repository

import br.com.emersonfiwre.drawteam.commons.model.PlayerModel
import br.com.emersonfiwre.drawteam.features.drawplayers.repository.mapper.DrawPlayersRepositoryMapper
import br.com.emersonfiwre.drawteam.features.player.repository.PlayerDAO

class DrawPlayersRepositoryImpl(
    private val playerDao: PlayerDAO
): DrawPlayersRepository {

    override suspend fun getPlayersBySession(): List<PlayerModel> {
        val result = playerDao.getAllPlayers()
        originalPlayers = result
        return DrawPlayersRepositoryMapper.convertToSelectedPlayers(result, playerSelected)
    }

    override suspend fun resetPlayers(): List<PlayerModel> {
        playerSelected = listOf()
        return originalPlayers.ifEmpty {
            playerDao.getAllPlayers()
        }
    }

    override fun applySelectedPlayers(players: List<PlayerModel>) {
        playerSelected = players
    }

    companion object {

        private var playerSelected: List<PlayerModel> = listOf()
        private var originalPlayers: List<PlayerModel> = listOf()
    }
}
