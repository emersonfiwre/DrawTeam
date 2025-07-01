package br.com.emersonfiwre.drawteam.features.home.usecase

import br.com.emersonfiwre.drawteam.commons.model.TeamModel
import br.com.emersonfiwre.drawteam.features.home.usecase.state.HomeUseCaseState

interface HomeUseCase {

    fun setupPresentationTeams(teamList: List<TeamModel>?): HomeUseCaseState.TeamPresentationState
}
