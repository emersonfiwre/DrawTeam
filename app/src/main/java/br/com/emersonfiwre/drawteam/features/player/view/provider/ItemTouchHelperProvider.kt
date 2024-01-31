package br.com.emersonfiwre.drawteam.features.player.view.provider

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import br.com.emersonfiwre.drawteam.R
import br.com.emersonfiwre.drawteam.commons.constants.DrawTeamConstants.ZERO_INT
import br.com.emersonfiwre.drawteam.commons.view.viewholder.PlayerViewHolder
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

class ItemTouchHelperProvider(
    private val context: Context,
    private val onItemClick: (position: Int) -> Unit
): ItemTouchHelper.Callback() {

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        var movementFlags = ZERO_INT
        when (viewHolder) {
            is PlayerViewHolder -> {
                movementFlags = makeMovementFlags(ZERO_INT, ItemTouchHelper.START)
            }
        }
        return movementFlags
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        // Not yet implemented
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        when (direction) {
            ItemTouchHelper.START -> {
                onItemClick(viewHolder.bindingAdapterPosition)
            }
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        RecyclerViewSwipeDecorator.Builder(
            c,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
            .addBackgroundColor(ContextCompat.getColor(context, R.color.draw_team_red))
            .addActionIcon(R.drawable.draw_team_ic_delete)
            .create()
            .decorate()
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}