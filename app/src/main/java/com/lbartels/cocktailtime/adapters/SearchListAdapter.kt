package com.lbartels.cocktailtime.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lbartels.cocktailtime.R
import com.lbartels.cocktailtime.database.DatabaseHandler

import com.lbartels.cocktailtime.models.DrinkModel

class SearchListAdapter(
    private val data: ArrayList<DrinkModel>,
    private var context: Context
) : RecyclerView.Adapter<SearchListAdapter.MyViewHolder>(){

    private var onClickListner: OnClickListner? = null

    inner class MyViewHolder(private val view : View): RecyclerView.ViewHolder(view){
        fun bind(drinkModel: DrinkModel) {
            val drink = view.findViewById<TextView>(R.id.tv_Title)
            val ingrident = view.findViewById<TextView>(R.id.tv_description)
            val image = view.findViewById<ImageView>(R.id.iv_image)

            drink.text = drinkModel.strDrink
            ingrident.text = drinkModel.strInstructions
            if (drinkModel.strDrinkThumb!!.startsWith("/data/")){
                image.setImageURI(Uri.parse(drinkModel.strDrinkThumb))
            } else {
                Glide.with(view).load(drinkModel.strDrinkThumb + "/preview").centerCrop().into(image)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_search, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener{
            if (onClickListner != null)
                onClickListner!!.onClick(position, data[position])
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun removeOwnAt(position: Int) {
        val dbHandler = DatabaseHandler(context)
        val isDeleted = dbHandler.deleteOwnCocktail(data[position])
        if (isDeleted > 0) {
            data.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun setOnClickListner(onClickListner: OnClickListner) {
        this.onClickListner = onClickListner
    }

    interface OnClickListner {
        fun onClick(position: Int, model: DrinkModel)
    }
}
