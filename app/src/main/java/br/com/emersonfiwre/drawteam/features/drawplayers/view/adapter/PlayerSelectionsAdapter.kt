package br.com.emersonfiwre.drawteam.features.drawplayers.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.emersonfiwre.drawteam.databinding.DrawTeamAdapterPlayerBinding
import br.com.emersonfiwre.drawteam.commons.model.PlayerModel
import br.com.emersonfiwre.drawteam.commons.view.viewholder.PlayerViewHolder

class PlayerSelectionsAdapter(
    private val playerList: MutableList<PlayerModel>,
    private val checkPlayer: (playerModel: PlayerModel) -> Unit
): RecyclerView.Adapter<PlayerViewHolder>() {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder =
        PlayerViewHolder(
            DrawTeamAdapterPlayerBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun getItemCount(): Int = playerList.size

    override fun getItemId(position: Int): Long = position.toLong()

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        holder.bind(playerList[position], checkPlayer, true)
    }

    fun notifyItems(players: List<PlayerModel>) {
        playerList.clear()
        playerList.addAll(players)
        notifyDataSetChanged()
    }
}
