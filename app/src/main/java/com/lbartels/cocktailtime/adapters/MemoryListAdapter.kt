package com.lbartels.cocktailtime.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lbartels.cocktailtime.R
import com.lbartels.cocktailtime.database.DatabaseHandler
import com.lbartels.cocktailtime.models.MemoryModel

class MemoryListAdapter(
    private val context: Context,
    private var list: ArrayList<MemoryModel>,
    ): RecyclerView.Adapter<MemoryListAdapter.MemoryViewHolder>() {

    private var onClickListner: OnClickListner? = null

    inner class MemoryViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(memory: MemoryModel) {
            val image = view.findViewById<ImageView>(R.id.iv_memory)
            val title = view.findViewById<TextView>(R.id.tv_memory_cr_title)

            image.setImageURI(Uri.parse(memory.image))
            title.text = memory.title
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoryViewHolder {
        return MemoryViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.card_memory,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MemoryViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setOnClickListner(onClickListner: OnClickListner) {
        this.onClickListner = onClickListner
    }

    fun removeAt(position: Int) {
        val dbHandler = DatabaseHandler(context)
        val isDeleted = dbHandler.deleteMemoryPlace(list[position])
        if (isDeleted > 0) {
            list.removeAt(position)
            notifyItemRemoved(position)
        }
    }



    interface OnClickListner {
        fun onClick(position: Int, model: MemoryModel)
    }



}