package br.com.emersonfiwre.drawteam.features.player.usecase.state

import br.com.emersonfiwre.drawteam.commons.model.PlayerModel

object PlayerUseCaseState {

    sealed class PlayerState {
        data class DisplayPlayers(
            val items: List<PlayerModel>
        ): PlayerState()

        object DisplayError: PlayerState()
    }

    sealed class AddPlayerState {
        object DisplaySuccess: AddPlayerState()

        object DisplayError: AddPlayerState()
    }

    sealed class DeletePlayerState {
        object DisplaySuccess: DeletePlayerState()

        object DisplayError: DeletePlayerState()
    }
}