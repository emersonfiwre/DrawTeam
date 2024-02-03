package br.com.emersonfiwre.drawteam.commons.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import br.com.emersonfiwre.drawteam.features.player.model.PlayerStateEnum
import br.com.emersonfiwre.drawteam.features.player.model.PlayerTypeEnum
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "player")
data class PlayerModel(
    @PrimaryKey(autoGenerate = true)
    var uid: Long = 0,
    @ColumnInfo(name = "name")
    var playerName: String? = null,
    @ColumnInfo(name = "is_goalKeeper")
    var playerGoalKeeper: Boolean? = null,
    @Ignore
    var playerType: PlayerTypeEnum? = null,
    @Ignore
    var playerState: PlayerStateEnum? = null
): Parcelable
