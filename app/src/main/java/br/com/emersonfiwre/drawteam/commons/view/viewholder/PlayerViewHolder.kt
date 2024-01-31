package br.com.emersonfiwre.drawteam.commons.view.viewholder

import android.view.View
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import br.com.emersonfiwre.drawteam.databinding.DrawTeamAdapterPlayerBinding
import br.com.emersonfiwre.drawteam.commons.model.PlayerModel
import br.com.emersonfiwre.drawteam.features.player.model.PlayerStateEnum

class PlayerViewHolder(
    private val binding: DrawTeamAdapterPlayerBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(
        playerModel: PlayerModel,
        checkPlayer: (playerModel: PlayerModel) -> Unit = {},
        checkBoxVisible: Boolean = false
    ) {
        if (checkBoxVisible) {
            binding.idAdapterPlayerCheckBox.text = playerModel.playerName
            binding.idAdapterPlayerCheckBox.visibility = View.VISIBLE
            binding.idAdapterPlayerCheckBox.setIsChecked(playerModel)
            setupCheckBoxListener(playerModel, checkPlayer)
        } else {
            binding.idAdapterPlayerCheckBox.visibility = View.GONE
            binding.idAdapterPlayerName.text = playerModel.playerName
        }
    }

    private fun CheckBox.setIsChecked(playerModel: PlayerModel) = this.run {
        isChecked = playerModel.playerState == PlayerStateEnum.CHECKED
    }

    private fun setupCheckBoxListener(
        playerModel: PlayerModel,
        checkPlayer: (playerModel: PlayerModel) -> Unit
    ) {
        binding.idAdapterPlayerCheckBox.setOnClickListener {
            if (binding.idAdapterPlayerCheckBox.isChecked) {
                playerModel.playerState = PlayerStateEnum.CHECKED
            } else {
                playerModel.playerState = PlayerStateEnum.UNCHECKED
            }
            checkPlayer(playerModel)
        }
    }
}