package br.com.emersonfiwre.drawteam

import br.com.emersonfiwre.drawteam.commons.model.TeamModel

object DrawTeamSession {

    var numberOfTeams: Int = 2
    var numberOfPlayers: Int = 5

    // for devices with dont keep activities on
    internal var teamsShuffledSession: List<TeamModel>? = null
}