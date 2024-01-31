package br.com.emersonfiwre.drawteam.features.home.usecase

import android.util.Log
import br.com.emersonfiwre.drawteam.commons.model.TeamModel
import br.com.emersonfiwre.drawteam.features.home.usecase.state.HomeUseCaseState
import br.com.emersonfiwre.drawteam.features.player.usecase.PlayerUseCaseImpl

class HomeUseCaseImpl: HomeUseCase {

    @Suppress("ToGenericExceptionCaught")
    override fun setupPresentationTeams(teamList: List<TeamModel>?): HomeUseCaseState.TeamPresentationState {
        return try {
            when {
                teamList.isNullOrEmpty() -> {
                    HomeUseCaseState.TeamPresentationState.DisplayError
                }

                teamList.size == TWO_INT && teamList.isNotEmpty() -> {
                    HomeUseCaseState.TeamPresentationState.DisplayTwoTeams(
                        teamList.first(),
                        teamList.last()
                    )
                }

                else -> {
                    HomeUseCaseState.TeamPresentationState.DisplayTeams(teamList)
                }
            }
        } catch (ex: Exception) {
            Log.d(PlayerUseCaseImpl::javaClass.name, ex.message.toString())
            HomeUseCaseState.TeamPresentationState.DisplayError
        }
    }

    companion object {
        private const val TWO_INT = 2
    }
}