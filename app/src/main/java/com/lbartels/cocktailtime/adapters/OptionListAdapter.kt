package com.lbartels.cocktailtime.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lbartels.cocktailtime.R
import com.lbartels.cocktailtime.models.Options

class OptionListAdapter (
    private val data : ArrayList<Options>,
    private val context: Context
        ) : RecyclerView.Adapter<OptionListAdapter.OptionViewHolder>() {

    private var onClickListner: OnClickListner? = null


    inner class OptionViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
                fun bind(option : Options) {
                    val name = view.findViewById<TextView>(R.id.tv_option)
                    val image = view.findViewById<ImageView>(R.id.iv_drink)
                    name.text = option.getName()
                    image.setImageResource(option.getImage())
                }
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_option, parent, false)
        return OptionViewHolder(v)
    }

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        holder.bind(data[position])

        holder.itemView.setOnClickListener{
            if (onClickListner != null)
                onClickListner!!.onClick(position, data[position])
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setOnClickListen(onClickListner: OnClickListner) {
        this.onClickListner = onClickListner
    }

    interface OnClickListner {
        fun onClick(position: Int, model: Options)
    }

}