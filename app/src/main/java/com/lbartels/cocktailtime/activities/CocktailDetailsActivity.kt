package com.lbartels.cocktailtime.activities

import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.lbartels.cocktailtime.R
import com.lbartels.cocktailtime.databinding.ActivityCocktailDetailsBinding
import com.lbartels.cocktailtime.models.DrinkModel

class CocktailDetailsActivity : AppCompatActivity() {

    private var binding: ActivityCocktailDetailsBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCocktailDetailsBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        setSupportActionBar(binding!!.toolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
//
//        binding?.ivFavoriteDrink?.setOnClickListener {
//            val isImage = binding?.ivFavoriteDrink?.resources.getDrawable().getConstantState()
//
//            binding?.ivFavoriteDrink.ag)
//        }

        setContent()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setContent() {
        val bundle : Bundle? = intent.extras
        val data = bundle!!.getBundle("data")!!.get("data") as DrinkModel

        binding!!.tvTitleDrink.text = data.strDrink
        binding!!.tvPreparation.text = data.strInstructions
        Glide.with(this).load(data.strDrinkThumb).centerCrop().into(binding!!.ivDrink)
        checkIngredient(data)
    }
    private fun checkIngredient(data : DrinkModel) {
        val check = arrayOf(
            data.strIngredient1,
            data.strIngredient2,
            data.strIngredient3,
            data.strIngredient4,
            data.strIngredient5,
            data.strIngredient6,
            data.strIngredient7,
            data.strIngredient8,
            data.strIngredient9,
            data.strIngredient10,
            data.strIngredient11)
        for ((index, element) in check.withIndex()) {
            if (element == null) {
                check.drop(index)
            } else {
                val cb = CheckBox(applicationContext)
                cb.text = element
                cb.buttonTintList = ColorStateList.valueOf(resources.getColor(R.color.design_default_color_primary))
                cb.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                binding?.lvCheckbox?.addView(cb)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
