package br.com.emersonfiwre.drawteam.commons.model

data class TeamModel(
    var teamName: String? = null,
    var teamPlayers: List<PlayerModel>? = null
)
