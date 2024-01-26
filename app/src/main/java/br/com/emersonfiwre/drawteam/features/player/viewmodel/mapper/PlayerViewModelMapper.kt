package br.com.emersonfiwre.drawteam.features.player.viewmodel.mapper

import br.com.emersonfiwre.drawteam.commons.model.PlayerModel
import br.com.emersonfiwre.drawteam.features.player.model.PlayerTypeEnum
import br.com.emersonfiwre.drawteam.features.player.model.PlayerViewType
import br.com.emersonfiwre.drawteam.features.player.viewmodel.viewstate.PlayerViewState

object PlayerViewModelMapper {

    fun convertModelToPlayerViewTypeState(
        players: List<PlayerModel>
    ): PlayerViewState.PlayerListViewState.ShowPlayers {
        val model = mutableListOf<PlayerViewType>()
        players.forEach {
            when (it.playerType) {
                PlayerTypeEnum.ADD_PLAYER -> {
                    model.add(
                        PlayerViewType.AddPlayer
                    )
                }

                PlayerTypeEnum.PLAYER -> {
                    model.add(PlayerViewType.Player(it))
                }

                null -> {
                    model.add(PlayerViewType.Player(it))
                }
            }
        }
        return PlayerViewState.PlayerListViewState.ShowPlayers(model)
    }
}