package br.com.emersonfiwre.drawteam.features.home.model

import br.com.emersonfiwre.drawteam.commons.model.PlayerModel

sealed class TeamViewType(val viewType: TeamTypeEnum) {

    data class TeamName(val name: String?): TeamViewType(TeamTypeEnum.TEAM_NAME)
    data class Player(val player: PlayerModel): TeamViewType(TeamTypeEnum.TEAM_PLAYER)
}
