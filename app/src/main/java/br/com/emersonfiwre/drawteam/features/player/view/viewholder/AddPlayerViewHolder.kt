package br.com.emersonfiwre.drawteam.features.player.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import br.com.emersonfiwre.drawteam.databinding.DrawTeamAdapterAddPlayerBinding

class AddPlayerViewHolder(
    private val binding: DrawTeamAdapterAddPlayerBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(onItemClick: () -> Unit) {
        binding.root.setOnClickListener {
            onItemClick()
        }
    }
}
