package br.com.emersonfiwre.drawteam.commons.extensions

import android.content.Context
import android.provider.Settings
import br.com.emersonfiwre.drawteam.commons.constants.DrawTeamConstants.ZERO_INT

private const val DONT_KEEP_ACTIVITIES_ENABLE = 1

internal fun Context?.disableDontKeepActivities(): Boolean {
    val result =
        Settings.Global.getInt(
            this?.contentResolver,
            Settings.Global.ALWAYS_FINISH_ACTIVITIES,
            ZERO_INT
        )

    return (result == DONT_KEEP_ACTIVITIES_ENABLE)
}
