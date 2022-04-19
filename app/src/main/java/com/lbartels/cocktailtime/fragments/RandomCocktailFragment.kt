package com.lbartels.cocktailtime.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.lbartels.cocktailtime.API.ApiService
import com.lbartels.cocktailtime.R
import com.lbartels.cocktailtime.databinding.FragmentRandomCocktailBinding
import com.lbartels.cocktailtime.models.DrinkResponse
import com.lbartels.cocktailtime.models.DrinkModel
import retrofit2.Callback
import retrofit2.Response


class RandomCocktailFragment : Fragment() {
    private var _binding: FragmentRandomCocktailBinding? = null
    private val binding get() = _binding!!

    private lateinit var drinkModel: DrinkModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRandomCocktailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getCocktail()
    }

    private fun getCocktail() {
        val api = ApiService.create().getRandomCocktail()
        api.enqueue(object: Callback<DrinkResponse> {
            override fun onResponse(
                call: retrofit2.Call<DrinkResponse>,
                response: Response<DrinkResponse>
            ) {
                if (response.body()?.drinks == null) {
                    Toast.makeText(activity, "geen restulaat", Toast.LENGTH_LONG).show()
                } else {
                    if (response.isSuccessful) {
                        drinkModel = response.body()!!.drinks[0]
                        setUpLayout()
                    }
                }
            }
            override fun onFailure(call: retrofit2.Call<DrinkResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })

    }

    private fun setUpLayout() {

        binding.tvTitleDrink.text = drinkModel.strDrink
        binding.tvDayPreparation.text = drinkModel.strInstructions
        binding.tvDayPreparation.movementMethod = ScrollingMovementMethod()
        Glide.with(requireContext())
            .load(drinkModel.strDrinkThumb)
            .centerCrop()
            .into(binding.ivDrink)
        checkIngredient()
    }

    private fun checkIngredient() {
        val check = arrayOf(
            drinkModel.strIngredient1,
            drinkModel.strIngredient2,
            drinkModel.strIngredient3,
            drinkModel.strIngredient4,
            drinkModel.strIngredient5,
            drinkModel.strIngredient6,
            drinkModel.strIngredient7,
            drinkModel.strIngredient8,
            drinkModel.strIngredient9,
            drinkModel.strIngredient10,
            drinkModel.strIngredient11)
        for ((index, element) in check.withIndex()) {
            if (element == null) {
                check.drop(index)
            } else {
                val cb = CheckBox(context)
                cb.text = element
                cb.buttonTintList = ColorStateList.valueOf(resources.getColor(R.color.design_default_color_primary))
                cb.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                binding.lvCheckbox.addView(cb)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}