package com.lbartels.cocktailtime.constants

import com.lbartels.cocktailtime.R
import com.lbartels.cocktailtime.models.Options

class OptionConstants {

    companion object{
        fun defaultOption (): ArrayList<Options> {
            val optionsList = ArrayList<Options>()

            val malibuOption = Options(R.drawable.malibu_cocktail, "Malibu", "malibu")
            optionsList.add(malibuOption)

            val mojitoOption = Options(R.drawable.mojito_cocktail, "Mojito", "bacardi")
            optionsList.add(mojitoOption)

            val espressoOption = Options(R.drawable.espresso_cocktail, "Espresso", "espresso")
            optionsList.add(espressoOption)

            val vodkaOption = Options(R.drawable.vodka_cocktail, "Vodka", "vodka")
            optionsList.add(vodkaOption)

            return optionsList
        }
    }

}