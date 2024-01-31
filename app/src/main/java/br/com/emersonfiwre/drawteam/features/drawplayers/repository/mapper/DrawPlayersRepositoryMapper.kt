package br.com.emersonfiwre.drawteam.features.drawplayers.repository.mapper

import br.com.emersonfiwre.drawteam.commons.model.PlayerModel

object DrawPlayersRepositoryMapper {

    fun convertToSelectedPlayers(
        result: List<PlayerModel>,
        playerSelected: List<PlayerModel>
    ): List<PlayerModel> {
        val model = mutableListOf<PlayerModel>()
        model.addAll(playerSelected)
        result.forEach {
            if (!contains(it, model)) {
                model.add(it)
            }
        }
        return model
    }

    private fun contains(player: PlayerModel, model: MutableList<PlayerModel>) =
        model.any { it.uid == player.uid }
}
