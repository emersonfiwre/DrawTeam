package br.com.emersonfiwre.drawteam.commons.view.listener

import br.com.emersonfiwre.drawteam.commons.model.TeamModel

interface DrawTeamListener {

    fun onDrawClick(teams: List<TeamModel>)
}
