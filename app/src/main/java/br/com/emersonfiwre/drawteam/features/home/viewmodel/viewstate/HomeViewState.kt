package br.com.emersonfiwre.drawteam.features.home.viewmodel.viewstate

import br.com.emersonfiwre.drawteam.features.home.model.TeamViewType

sealed class HomeViewState {

    sealed class HomeListOfTeamViewState {
        data class ShowTeams(val players: List<TeamViewType>): HomeListOfTeamViewState()
        data class ShowTwoTeams(
            val firstTeam: List<TeamViewType>,
            val secondTeam: List<TeamViewType>
        ): HomeListOfTeamViewState()
    }

    sealed class HomeError {
        object ShowPresentationError: HomeError()
    }
}
