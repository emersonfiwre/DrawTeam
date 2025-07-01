package br.com.emersonfiwre.drawteam.features.home.viewmodel.mapper

import br.com.emersonfiwre.drawteam.commons.model.TeamModel
import br.com.emersonfiwre.drawteam.features.home.model.TeamViewType
import br.com.emersonfiwre.drawteam.features.home.viewmodel.viewstate.HomeViewState

object HomeViewModelMapper {

    fun convertModelToTeamViewTypeState(
        teams: List<TeamModel>
    ): HomeViewState.HomeListOfTeamViewState.ShowTeams {
        val model = mutableListOf<TeamViewType>()
        teams.forEach { team ->
            model.add(
                TeamViewType.TeamName(team.teamName)
            )
            team.teamPlayers?.forEach {
                model.add(
                    TeamViewType.Player(it)
                )
            }
        }
        return HomeViewState.HomeListOfTeamViewState.ShowTeams(model)
    }

    fun convertModelToTwoTeamViewTypeState(
        team: TeamModel, secondTeam: TeamModel
    ): HomeViewState.HomeListOfTeamViewState.ShowTwoTeams {
        val firstTeamList = mutableListOf<TeamViewType>()
        val secondTeamList = mutableListOf<TeamViewType>()
        setupOneTeam(firstTeamList, team)
        setupOneTeam(secondTeamList, secondTeam)
        return HomeViewState.HomeListOfTeamViewState.ShowTwoTeams(firstTeamList, secondTeamList)
    }

    private fun setupOneTeam(model: MutableList<TeamViewType>, team: TeamModel) {
        model.add(
            TeamViewType.TeamName(team.teamName)
        )
        team.teamPlayers?.forEach {
            model.add(
                TeamViewType.Player(it)
            )
        }
    }
}
