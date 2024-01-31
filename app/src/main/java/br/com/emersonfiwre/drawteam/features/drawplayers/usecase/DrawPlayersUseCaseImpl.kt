package br.com.emersonfiwre.drawteam.features.drawplayers.usecase

import android.util.Log
import br.com.emersonfiwre.drawteam.commons.constants.DrawTeamConstants.ZERO_INT
import br.com.emersonfiwre.drawteam.commons.model.PlayerModel
import br.com.emersonfiwre.drawteam.commons.model.TeamModel
import br.com.emersonfiwre.drawteam.features.drawplayers.repository.DrawPlayersRepository
import br.com.emersonfiwre.drawteam.features.drawplayers.usecase.state.DrawPlayerUseCaseState
import br.com.emersonfiwre.drawteam.features.player.model.PlayerStateEnum
import br.com.emersonfiwre.drawteam.features.player.usecase.PlayerUseCaseImpl

class DrawPlayersUseCaseImpl(
    private val numberOfTeams: Int,
    private val numberOfPlayers: Int,
    private val repository: DrawPlayersRepository
): DrawPlayersUseCase {

    @Suppress("ToGenericExceptionCaught")
    override suspend fun getPlayers(): DrawPlayerUseCaseState.PlayerListState {
        return try {
            val result = repository.getPlayersBySession()
            if (result.isEmpty()) {
                DrawPlayerUseCaseState.PlayerListState.DisplayEmptyPlayers
            } else {
                DrawPlayerUseCaseState.PlayerListState.DisplayPlayers(
                    result, setupCountItemsSelected(result)
                )
            }
        } catch (ex: Exception) {
            Log.d(PlayerUseCaseImpl::javaClass.name, ex.message.toString())
            DrawPlayerUseCaseState.PlayerListState.DisplayError
        }
    }

    private fun setupCountItemsSelected(result: List<PlayerModel>) =
        result.count { it.playerState == PlayerStateEnum.CHECKED }

    @Suppress("ToGenericExceptionCaught")
    override fun setupDrawPlayers(players: List<PlayerModel>): DrawPlayerUseCaseState.TeamShuffledState {
        return try {
            val selectedPlayers =
                players.filter { it.playerState == PlayerStateEnum.CHECKED }

            when {
                selectedPlayers.isEmpty() -> {
                    DrawPlayerUseCaseState.TeamShuffledState.DisplayNotPlayersSelected
                }

                isHavePlayersForTeam(selectedPlayers) -> {
                    repository.applySelectedPlayers(players)
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

    @Suppress("ToGenericExceptionCaught")
    override suspend fun setupResetSelection(): DrawPlayerUseCaseState.PlayerListState {
        return try {
            val model = repository.resetPlayers()
            return DrawPlayerUseCaseState.PlayerListState.DisplayPlayers(
                model,
                ZERO_INT
            )
        } catch (ex: Exception) {
            Log.d(PlayerUseCaseImpl::javaClass.name, ex.message.toString())
            DrawPlayerUseCaseState.PlayerListState.DisplayError
        }

    }

    private fun isHavePlayersForTeam(selectedPlayers: List<PlayerModel>) =
        (selectedPlayers.size >= (numberOfPlayers * numberOfTeams))

    private fun setupSeparateTeam(selectedPlayers: List<PlayerModel>): MutableList<TeamModel> {
        val teams = mutableListOf<TeamModel>()
        val shuffledPlayers = setupShufflePlayers(selectedPlayers)

        val substitutes = setupNumberPlayerOfTeam(shuffledPlayers)

        val playersOfTeams = shuffledPlayers.chunked(numberOfPlayers)

        playersOfTeams.forEachIndexed { index, players ->
            teams.add(
                TeamModel(teamName = "Time ${index.inc()}", teamPlayers = players)
            )
        }

        setupGoalKeepersForAll(teams)
        substitutes?.let { teams.addAll(it) }

        return teams
    }

    private fun setupNumberPlayerOfTeam(selectedPlayers: MutableList<PlayerModel>): MutableList<TeamModel>? {
        val teamSubstitutes = mutableListOf<TeamModel>()
        val substitutes = mutableListOf<PlayerModel>()
        var numberPlayerByTeam = selectedPlayers.size.toDouble().div(numberOfTeams.toDouble())

        while (numberPlayerByTeam > numberOfPlayers) {
            val randomIndex = (ZERO_INT until selectedPlayers.size).random()
            substitutes.add(selectedPlayers[randomIndex])
            selectedPlayers.removeAt(randomIndex)
            numberPlayerByTeam = selectedPlayers.size.toDouble().div(numberOfTeams.toDouble())
        }

        return if (substitutes.isNotEmpty()) {
            teamSubstitutes.add(
                TeamModel(
                    teamName = "Jogadores reservas",
                    teamPlayers = substitutes
                )
            )
            teamSubstitutes
        } else {
            null
        }
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

            val goalPicked = (ZERO_INT until goalkeepers.size.dec()).random()
            val teamChanged = setupChangePlayers(goalkeepers[goalPicked], teamsWithoutGoalKeepers)
            setupChangeTeamToWithGoalKeeper(teamChanged, teamsWithoutGoalKeepers, teamsGoalKeepers)
        }

        model.addAll(teamsGoalKeepers)
        model.addAll(teamsWithoutGoalKeepers)
        return model
    }

    private fun setupChangeTeamToWithGoalKeeper(
        teamChanged: TeamModel,
        teamsWithoutGoalKeepers: MutableList<TeamModel>,
        teamsGoalKeepers: MutableList<TeamModel>
    ) {
        teamsWithoutGoalKeepers.remove(teamChanged)
        teamsGoalKeepers.add(teamChanged)
    }

    private fun setupTeamsWithMoreGoalKeeper(teams: MutableList<TeamModel>): MutableList<TeamModel> {
        val model = mutableListOf<TeamModel>()
        model.addAll(teams.filter { team ->
            (team.teamPlayers?.filter { it.playerGoalKeeper == true }?.size
                ?: ZERO_INT) >= TWO_INT
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
    ): TeamModel {
        val teamIndex = (ZERO_INT until teamsWithoutGoalKeepers.size.dec()).random()
        val playerIndex =
            (ZERO_INT until (teamsWithoutGoalKeepers[teamIndex].teamPlayers?.size?.dec()
                ?: ZERO_INT)
                    ).random()
        val playerChosen = teamsWithoutGoalKeepers[teamIndex].teamPlayers?.get(playerIndex)

        teamsWithoutGoalKeepers[teamIndex].teamPlayers = setupChangeInTeamList(
            playerChosen,
            goalKeeper,
            teamsWithoutGoalKeepers[teamIndex].teamPlayers
        )

        return teamsWithoutGoalKeepers[teamIndex]
    }

    @Suppress("ToGenericExceptionCaught")
    override fun foundPlayerByName(
        parameter: String,
        items: List<PlayerModel>?
    ): DrawPlayerUseCaseState.PlayerListState {
        return try {
            val foundedPlayers = mutableListOf<PlayerModel>()
            setupParameterIsBlank(parameter, items)
            if (!items.isNullOrEmpty()) {
                foundedPlayers.addAll(items.filter {
                    containsPlayer(parameter, it)
                })
                setupFoundedPlayers(foundedPlayers, setupCountItemsSelected(items))

            } else {
                DrawPlayerUseCaseState.PlayerListState.DisplayNotFoundedPlayers
            }
        } catch (ex: Exception) {
            Log.d(PlayerUseCaseImpl::javaClass.name, ex.message.toString())
            DrawPlayerUseCaseState.PlayerListState.DisplayError
        }
    }

    private fun setupFoundedPlayers(foundedPlayers: MutableList<PlayerModel>, itemsSelected: Int) =
        if (foundedPlayers.isEmpty()) {
            DrawPlayerUseCaseState.PlayerListState.DisplayNotFoundedPlayers
        } else {
            DrawPlayerUseCaseState.PlayerListState.DisplayPlayers(
                foundedPlayers, itemsSelected
            )
        }

    private fun setupParameterIsBlank(parameter: String, items: List<PlayerModel>?) =
        if (parameter.isBlank()) {
            items?.let {
                DrawPlayerUseCaseState.PlayerListState.DisplayPlayers(
                    it,
                    setupCountItemsSelected(it)
                )
            }
        } else {
            DrawPlayerUseCaseState.PlayerListState.DisplayNotFoundedPlayers
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