package br.com.emersonfiwre.drawteam.features.home.usecase.state

import br.com.emersonfiwre.drawteam.commons.model.TeamModel

object HomeUseCaseState {

    sealed class TeamPresentationState {
        data class DisplayTeams(
            val teams: List<TeamModel>,
        ): TeamPresentationState()

        data class DisplayTwoTeams(
            val team: TeamModel, val secondTeam: TeamModel
        ): TeamPresentationState()

        object DisplayError: TeamPresentationState()
    }
}