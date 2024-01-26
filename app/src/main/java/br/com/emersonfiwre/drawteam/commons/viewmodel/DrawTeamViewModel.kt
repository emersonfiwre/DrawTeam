package br.com.emersonfiwre.drawteam.commons.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlin.coroutines.CoroutineContext

abstract class DrawTeamViewModel: ViewModel(), CoroutineScope {

    private val viewModelJob = SupervisorJob()
    final override val coroutineContext: CoroutineContext = Dispatchers.Main + viewModelJob

    protected val coroutineScope = CoroutineScope(coroutineContext)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancelChildren()
    }
}