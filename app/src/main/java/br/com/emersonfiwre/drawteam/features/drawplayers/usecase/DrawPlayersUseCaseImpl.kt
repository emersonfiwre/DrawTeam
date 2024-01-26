package br.com.emersonfiwre.drawteam.features.drawplayers.usecase

import android.util.Log
import br.com.emersonfiwre.drawteam.commons.constants.DrawTeamConstants
import br.com.emersonfiwre.drawteam.commons.model.PlayerModel
import br.com.emersonfiwre.drawteam.commons.model.TeamModel
import br.com.emersonfiwre.drawteam.features.drawplayers.usecase.state.DrawPlayerUseCaseState
import br.com.emersonfiwre.drawteam.features.player.model.PlayerStateEnum
import br.com.emersonfiwre.drawteam.features.player.repository.PlayerDAO
import br.com.emersonfiwre.drawteam.features.player.usecase.PlayerUseCaseImpl

class DrawPlayersUseCaseImpl(
    private val numberOfTeams: Int,
    private val playerDao: PlayerDAO
): DrawPlayersUseCase {

    @Suppress("ToGenericExceptionCaught")
    override fun getPlayers(): DrawPlayerUseCaseState.PlayerListState {
        return try {
            val result = playerDao.getAllPlayers()
            if (result.isEmpty()) {
                DrawPlayerUseCaseState.PlayerListState.DisplayEmptyPlayers
            } else {
                DrawPlayerUseCaseState.PlayerListState.DisplayPlayers(
                    result
                )
            }
        } catch (ex: Exception) {
            Log.d(PlayerUseCaseImpl::javaClass.name, ex.message.toString())
            DrawPlayerUseCaseState.PlayerListState.DisplayError
        }
    }

    @Suppress("ToGenericExceptionCaught")
    override fun setupDrawPlayers(players: List<PlayerModel>): DrawPlayerUseCaseState.TeamShuffledState {
        return try {
            val selectedPlayers =
                players.filter { it.playerState == PlayerStateEnum.CHECKED }

            when {
                selectedPlayers.isEmpty() -> {
                    DrawPlayerUseCaseState.TeamShuffledState.DisplayNotPlayersSelected
                }

                isPlayersForTeam(selectedPlayers) -> {
                    val teams = setupSeparateTeam(selectedPlayers)
                    DrawPlayerUseCaseState.TeamShuffledState.DisplayTeams(teams)
                }

                else -> {
                    DrawPlayerUseCaseState.TeamShuffledState.DisplayNotPlayersEnough
                }
            }
        } catch (ex: Exception) {
            Log.d(PlayerUseCaseImpl::javaClass.name, ex.message.toString())
            DrawPlayerUseCaseState.TeamShuffledState.DisplayError
        }
    }

    private fun isPlayersForTeam(selectedPlayers: List<PlayerModel>): Boolean =
        (selectedPlayers.size % numberOfTeams == DrawTeamConstants.ZERO_INT)

    private fun setupSeparateTeam(selectedPlayers: List<PlayerModel>): MutableList<TeamModel> {
        val teams = mutableListOf<TeamModel>()
        val shuffledPlayers = setupShufflePlayers(selectedPlayers)

        val numberPlayerByTeam = selectedPlayers.size / numberOfTeams
        val playersOfTeams = shuffledPlayers.chunked(numberPlayerByTeam)

        playersOfTeams.forEachIndexed { index, players ->
            teams.add(
                TeamModel(teamName = "Time ${index.inc()}", teamPlayers = players)
            )
        }

        setupGoalKeepersForAll(teams)

        return teams
    }

    private fun setupShufflePlayers(selectPlayers: List<PlayerModel>): MutableList<PlayerModel> {
        val model = mutableListOf<PlayerModel>()
        model.addAll(selectPlayers)
        model.shuffle()
        return model
    }

    private fun setupGoalKeepersForAll(teams: MutableList<TeamModel>): MutableList<TeamModel> {
        val model = mutableListOf<TeamModel>()
        val teamsGoalKeepers = setupTeamsWithMoreGoalKeeper(teams)
        val teamsWithoutGoalKeepers = setupTeamsWithoutGoalKeeper(teams)

        if (teamsGoalKeepers.isEmpty() || teamsWithoutGoalKeepers.isEmpty()) {
            return teams
        }

        teamsGoalKeepers.forEach { teamWithGoalkeeper ->
            val goalkeepers =
                setupGoalKeepers(teamWithGoalkeeper.teamPlayers)
            if (goalkeepers.isEmpty()) {
                return teams
            }

            val goalPicked = (DrawTeamConstants.ZERO_INT until goalkeepers.size).random()
            setupChangePlayers(goalkeepers[goalPicked], teamsWithoutGoalKeepers)

        }

        model.addAll(teamsGoalKeepers)
        model.addAll(teamsWithoutGoalKeepers)
        return model
    }

    private fun setupTeamsWithMoreGoalKeeper(teams: MutableList<TeamModel>): MutableList<TeamModel> {
        val model = mutableListOf<TeamModel>()
        model.addAll(teams.filter { team ->
            (team.teamPlayers?.filter { it.playerGoalKeeper == true }?.size
                ?: DrawTeamConstants.ZERO_INT) >= TWO_INT
        })
        return model
    }

    private fun setupTeamsWithoutGoalKeeper(teams: MutableList<TeamModel>): MutableList<TeamModel> {
        val model = mutableListOf<TeamModel>()
        model.addAll(teams.filter { team ->
            team.teamPlayers?.any { it.playerGoalKeeper == false } == true
        })
        return model
    }

    private fun setupGoalKeepers(teamPlayers: List<PlayerModel>?): MutableList<PlayerModel> {
        val model = mutableListOf<PlayerModel>()
        teamPlayers?.filter { it.playerGoalKeeper == true }?.let {
            model.addAll(it)
        }
        return model
    }

    private fun setupChangeInTeamList(
        playerTraded: PlayerModel?,
        goalKeeper: PlayerModel,
        teamPlayers: List<PlayerModel>?
    ): List<PlayerModel>? {
        val changePlayer = mutableListOf<PlayerModel>()
        if (!teamPlayers.isNullOrEmpty()) {
            changePlayer.addAll(teamPlayers)

            changePlayer.remove(playerTraded)
            changePlayer.add(goalKeeper)
            return changePlayer
        }
        return teamPlayers
    }

    private fun setupChangePlayers(
        goalKeeper: PlayerModel,
        teamsWithoutGoalKeepers: MutableList<TeamModel>
    ): PlayerModel? {
        val teamIndex = (DrawTeamConstants.ZERO_INT until teamsWithoutGoalKeepers.size).random()
        val playerIndex =
            (DrawTeamConstants.ZERO_INT until (teamsWithoutGoalKeepers[teamIndex].teamPlayers?.size
                ?: DrawTeamConstants.ZERO_INT)
                    ).random()
        val playerTraded = teamsWithoutGoalKeepers[teamIndex].teamPlayers?.get(playerIndex)

        teamsWithoutGoalKeepers[teamIndex].teamPlayers = setupChangeInTeamList(
            playerTraded,
            goalKeeper,
            teamsWithoutGoalKeepers[teamIndex].teamPlayers
        )

        return playerTraded
    }

    @Suppress("ToGenericExceptionCaught")
    override fun foundPlayerByName(
        parameter: String,
        items: List<PlayerModel>?
    ): DrawPlayerUseCaseState.PlayerListState {
        return try {
            val foundedPlayers = mutableListOf<PlayerModel>()
            if (parameter.isBlank()) {
                items?.let { DrawPlayerUseCaseState.PlayerListState.DisplayPlayers(it) }
            }
            if (!items.isNullOrEmpty()) {
                foundedPlayers.addAll(items.filter {
                    containsPlayer(parameter, it)
                })
                DrawPlayerUseCaseState.PlayerListState.DisplayPlayers(foundedPlayers)
            } else {
                DrawPlayerUseCaseState.PlayerListState.DisplayEmptyPlayers
            }
        } catch (ex: Exception) {
            Log.d(PlayerUseCaseImpl::javaClass.name, ex.message.toString())
            DrawPlayerUseCaseState.PlayerListState.DisplayError
        }
    }

    private fun containsPlayer(
        parameter: String,
        player: PlayerModel
    ): Boolean =
        (player.playerName?.trim()?.lowercase()
            ?.contains(parameter.trim().lowercase()) == true)

    companion object {
        private const val TWO_INT = 2
    }
}