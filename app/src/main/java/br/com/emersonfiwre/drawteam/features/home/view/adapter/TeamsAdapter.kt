package br.com.emersonfiwre.drawteam.features.home.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.emersonfiwre.drawteam.databinding.DrawTeamAdapterPlayerBinding
import br.com.emersonfiwre.drawteam.databinding.DrawTeamAdapterTeamNameBinding
import br.com.emersonfiwre.drawteam.features.home.model.TeamTypeEnum
import br.com.emersonfiwre.drawteam.features.home.model.TeamViewType
import br.com.emersonfiwre.drawteam.features.home.view.viewholder.TeamNameViewHolder
import br.com.emersonfiwre.drawteam.commons.view.viewholder.PlayerViewHolder

class TeamsAdapter(
    private val teamsViewTypeList: MutableList<TeamViewType>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (TeamTypeEnum.values()[viewType]) {
            TeamTypeEnum.TEAM_NAME -> {
                TeamNameViewHolder(
                    DrawTeamAdapterTeamNameBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }

            TeamTypeEnum.TEAM_PLAYER -> {
                PlayerViewHolder(
                    DrawTeamAdapterPlayerBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val playerViewType = teamsViewTypeList[position]) {
            is TeamViewType.Player -> {
                (holder as PlayerViewHolder).bind(playerViewType.player)
            }

            is TeamViewType.TeamName -> {
                (holder as TeamNameViewHolder).bind(playerViewType.name)
            }
        }
    }

    override fun getItemCount(): Int = teamsViewTypeList.size

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemViewType(position: Int): Int {
        return teamsViewTypeList[position].viewType.ordinal
    }

    fun notifyItems(teams: List<TeamViewType>) {
        teamsViewTypeList.clear()
        teamsViewTypeList.addAll(teams)
        notifyDataSetChanged()
    }
}