package br.com.emersonfiwre.drawteam.features.player.viewmodel.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.emersonfiwre.drawteam.features.player.repository.AppDatabase
import br.com.emersonfiwre.drawteam.features.player.usecase.PlayerUseCaseImpl
import br.com.emersonfiwre.drawteam.features.player.viewmodel.PlayerViewModel

class PlayerViewModelFactory(private val application: Application): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        val db = AppDatabase.getDatabase(application.applicationContext)
        val dao = db.playerDao()
        val useCase = PlayerUseCaseImpl(dao)
        return PlayerViewModel(useCase) as T
    }
}
