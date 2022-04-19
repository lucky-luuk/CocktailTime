package com.lbartels.cocktailtime.adapters

import android.view.View

interface OnItemClickListner {
    fun onItemClick(position: Int, view: View)
    fun onOptionClicked(position: Int, view: View)
}