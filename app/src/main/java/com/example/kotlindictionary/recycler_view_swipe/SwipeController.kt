package com.example.kotlindictionary.recycler_view_swipe


import android.R
import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator


abstract class SwipeController(val context:Context) : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    private val deleteColor=ContextCompat.getColor(context,R.color.holo_red_dark)
    val ccc=R.color.holo_red_dark
    private val deleteIcon=R.drawable.ic_menu_delete
    private val updateIcon=R.drawable.ic_menu_upload

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: ViewHolder,
        target: ViewHolder
    ): Boolean {
        return false
    }


    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: ViewHolder,
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
        ).addSwipeLeftLabel("")
            .addSwipeLeftBackgroundColor(ContextCompat.getColor(context,ccc))
            .addSwipeLeftActionIcon(deleteIcon)
            .addSwipeRightLabel("")
            .addSwipeRightBackgroundColor(R.color.black)
            .addSwipeRightActionIcon(updateIcon)
            .create()
            .decorate()
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }


}