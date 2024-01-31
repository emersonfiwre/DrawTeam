package br.com.emersonfiwre.drawteam.features.home.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import br.com.emersonfiwre.drawteam.databinding.DrawTeamAdapterTeamNameBinding

class TeamNameViewHolder(
    private val binding: DrawTeamAdapterTeamNameBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(name: String?) {
        binding.idAdapterPlayerTitle.text = name
    }
}
