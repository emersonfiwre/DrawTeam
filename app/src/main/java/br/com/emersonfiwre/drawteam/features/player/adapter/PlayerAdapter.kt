package br.com.emersonfiwre.drawteam.features.player.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.emersonfiwre.drawteam.databinding.DrawTeamAdapterAddPlayerBinding
import br.com.emersonfiwre.drawteam.databinding.DrawTeamAdapterPlayerBinding
import br.com.emersonfiwre.drawteam.commons.model.PlayerModel
import br.com.emersonfiwre.drawteam.features.player.model.PlayerTypeEnum
import br.com.emersonfiwre.drawteam.features.player.model.PlayerViewType
import br.com.emersonfiwre.drawteam.features.player.view.viewholder.AddPlayerViewHolder
import br.com.emersonfiwre.drawteam.features.player.view.viewholder.PlayerViewHolder

class PlayerAdapter(
    private val playerViewTypeList: MutableList<PlayerViewType>,
    private val onItemClick: () -> Unit
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (PlayerTypeEnum.values()[viewType]) {

            PlayerTypeEnum.ADD_PLAYER -> AddPlayerViewHolder(
                DrawTeamAdapterAddPlayerBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )

            PlayerTypeEnum.PLAYER -> PlayerViewHolder(
                DrawTeamAdapterPlayerBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }

    override fun getItemCount(): Int = playerViewTypeList.size

    override fun getItemId(position: Int): Long = position.toLong()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val playerViewType = playerViewTypeList[position]) {
            is PlayerViewType.AddPlayer -> {
                (holder as AddPlayerViewHolder).bind(onItemClick)
            }

            is PlayerViewType.Player -> {
                (holder as PlayerViewHolder).bind(playerViewType.player)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return playerViewTypeList[position].viewType.ordinal
    }

    fun getItemByPosition(position: Int): PlayerModel? {
        return (playerViewTypeList[position] as? PlayerViewType.Player)?.player
    }

    fun notifyItems(playerList: List<PlayerViewType>) {
        playerViewTypeList.clear()
        playerViewTypeList.addAll(playerList)
        notifyDataSetChanged()
    }
}