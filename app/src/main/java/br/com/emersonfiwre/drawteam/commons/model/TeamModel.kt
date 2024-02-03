package br.com.emersonfiwre.drawteam.commons.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TeamModel(
    var teamName: String? = null,
    var teamPlayers: List<PlayerModel>? = null
): Parcelable
