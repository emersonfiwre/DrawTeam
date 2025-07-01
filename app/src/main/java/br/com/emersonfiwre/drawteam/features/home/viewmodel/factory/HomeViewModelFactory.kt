package br.com.emersonfiwre.drawteam.features.home.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.emersonfiwre.drawteam.features.home.usecase.HomeUseCaseImpl
import br.com.emersonfiwre.drawteam.features.home.viewmodel.HomeViewModel

class HomeViewModelFactory: ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        val useCase = HomeUseCaseImpl()
        return HomeViewModel(useCase) as T
    }
}
