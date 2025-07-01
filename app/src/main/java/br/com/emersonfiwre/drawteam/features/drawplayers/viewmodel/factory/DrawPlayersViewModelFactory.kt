package br.com.emersonfiwre.drawteam.features.drawplayers.viewmodel.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.emersonfiwre.drawteam.DrawTeamSession
import br.com.emersonfiwre.drawteam.features.drawplayers.repository.DrawPlayersRepositoryImpl
import br.com.emersonfiwre.drawteam.features.drawplayers.usecase.DrawPlayersUseCaseImpl
import br.com.emersonfiwre.drawteam.features.drawplayers.viewmodel.DrawPlayersViewModel
import br.com.emersonfiwre.drawteam.features.player.repository.AppDatabase

class DrawPlayersViewModelFactory(private val application: Application): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        val db = AppDatabase.getDatabase(application.applicationContext)
        val dao = db.playerDao()
        val numberOfTeams by lazy { DrawTeamSession.numberOfTeams }
        val numberOfPlayers by lazy { DrawTeamSession.numberOfPlayers }
        val repository = DrawPlayersRepositoryImpl(dao)
        val useCase = DrawPlayersUseCaseImpl(numberOfTeams, numberOfPlayers,repository)
        return DrawPlayersViewModel(useCase) as T
    }
}
