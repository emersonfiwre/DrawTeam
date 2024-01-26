package br.com.emersonfiwre.drawteam.features.player.model

import br.com.emersonfiwre.drawteam.commons.model.PlayerModel

sealed class PlayerViewType(val viewType: PlayerTypeEnum) {

    object AddPlayer: PlayerViewType(PlayerTypeEnum.ADD_PLAYER)
    data class Player(val player: PlayerModel): PlayerViewType(PlayerTypeEnum.PLAYER)
}
