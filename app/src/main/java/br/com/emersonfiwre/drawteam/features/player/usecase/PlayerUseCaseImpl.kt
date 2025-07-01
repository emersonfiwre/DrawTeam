package br.com.emersonfiwre.drawteam.features.player.usecase

import android.util.Log
import br.com.emersonfiwre.drawteam.commons.model.PlayerModel
import br.com.emersonfiwre.drawteam.features.player.model.PlayerTypeEnum
import br.com.emersonfiwre.drawteam.features.player.repository.PlayerDAO
import br.com.emersonfiwre.drawteam.features.player.usecase.state.PlayerUseCaseState
import java.lang.Exception

class PlayerUseCaseImpl(
    private val playerDao: PlayerDAO
): PlayerUseCase {

    @Suppress("TooGenericExceptionCaught")
    override fun getPlayers(): PlayerUseCaseState.PlayerState {
        return try {
            listOf(PlayerModel(playerType = PlayerTypeEnum.ADD_PLAYER))
            val result = playerDao.getAllPlayers()
            val rules = setupAddPlayerFirst(result)

            PlayerUseCaseState.PlayerState.DisplayPlayers(
                rules
            )
        } catch (ex: Exception) {
            Log.d(PlayerUseCaseImpl::javaClass.name, ex.message.toString())
            PlayerUseCaseState.PlayerState.DisplayError
        }
    }

    private fun setupAddPlayerFirst(result: List<PlayerModel>): MutableList<PlayerModel> {
        val model = mutableListOf<PlayerModel>()
        model.add(PlayerModel(playerType = PlayerTypeEnum.ADD_PLAYER))
        result.forEach {
            model.add(
                PlayerModel(
                    uid = it.uid,
                    playerName = it.playerName,
                    playerGoalKeeper = it.playerGoalKeeper,
                    playerType = PlayerTypeEnum.PLAYER
                )
            )
        }
        return model
    }

    @Suppress("TooGenericExceptionCaught")
    override fun addPlayer(
        name: String, goalKeeper: Boolean
    ): PlayerUseCaseState.AddPlayerState {
        return try {
            val model = PlayerModel(playerName = name, playerGoalKeeper = goalKeeper)
            playerDao.insertPlayer(model)
            PlayerUseCaseState.AddPlayerState.DisplaySuccess
        } catch (ex: Exception) {
            Log.d(PlayerUseCaseImpl::javaClass.name, ex.message.toString())
            PlayerUseCaseState.AddPlayerState.DisplayError
        }
    }

    @Suppress("TooGenericExceptionCaught")
    override fun deletePlayer(player: PlayerModel?): PlayerUseCaseState.DeletePlayerState {
        return try {
            if (player != null) {
                playerDao.deletePlayer(player)
                PlayerUseCaseState.DeletePlayerState.DisplaySuccess
            } else {
                PlayerUseCaseState.DeletePlayerState.DisplayError
            }
        } catch (ex: Exception) {
            Log.d(PlayerUseCaseImpl::javaClass.name, ex.message.toString())
            PlayerUseCaseState.DeletePlayerState.DisplayError
        }
    }
}
