package br.com.emersonfiwre.drawteam.features.player.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import br.com.emersonfiwre.drawteam.commons.model.PlayerModel

@Dao
interface PlayerDAO {

    @Query("SELECT * FROM player")
    fun getAllPlayers(): List<PlayerModel>

    @Insert
    fun insertPlayer(vararg players: PlayerModel)

    @Delete
    fun deletePlayer(player: PlayerModel)
}