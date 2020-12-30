package net.it96.enfoque.fragments.goals

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import net.it96.enfoque.R
import net.it96.enfoque.database.Project
import net.it96.enfoque.databinding.FragmentProjectDetailBinding

class GoalDelete (var adapter : GoalsAdapter, var selectedProject : Project, var context : Context, val binding: FragmentProjectDetailBinding) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
    private var swipeBackground: ColorDrawable = ColorDrawable(Color.parseColor("#FF0000"))
    private lateinit var deleteIcon: Drawable

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder,
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val goal = adapter.getObject(viewHolder.adapterPosition)
        adapter.deleteGoal(goal, viewHolder, binding)
    }

    /*
     *  Function to draw rectangle and delete icon when swiping an item
     */
    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean,
    ) {
        val itemView = viewHolder.itemView
        deleteIcon = ContextCompat.getDrawable(context, R.drawable.ic_delete)!!

        val iconMargin = (itemView.height - deleteIcon.intrinsicHeight) / 2

        if (dX > 0) {
            swipeBackground.setBounds(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
            deleteIcon.setBounds(itemView.left + iconMargin,
                itemView.top + iconMargin,
                itemView.left + iconMargin + deleteIcon.intrinsicWidth,
                itemView.bottom - iconMargin)
        } else {
            swipeBackground.setBounds(itemView.right + dX.toInt(),
                itemView.top,
                itemView.right,
                itemView.bottom)
            deleteIcon.setBounds(itemView.right - iconMargin - deleteIcon.intrinsicWidth,
                itemView.top + iconMargin,
                itemView.right - iconMargin,
                itemView.bottom - iconMargin)
        }

        swipeBackground.draw(c)

        c.save()

        if (dX > 0) {
            c.clipRect(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
        } else {
            c.clipRect(itemView.right - iconMargin - deleteIcon.intrinsicWidth,
                itemView.top + iconMargin,
                itemView.right - iconMargin,
                itemView.bottom - iconMargin)
        }
        deleteIcon.draw(c)

        c.restore()

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}