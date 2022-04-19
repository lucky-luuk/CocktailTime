package com.lbartels.cocktailtime.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lbartels.cocktailtime.activities.AddOwnCocktail
import com.lbartels.cocktailtime.activities.CocktailDetailsActivity
import com.lbartels.cocktailtime.adapters.SearchListAdapter
import com.lbartels.cocktailtime.database.DatabaseHandler
import com.lbartels.cocktailtime.databinding.FragmentSaveOwnCocktailBinding
import com.lbartels.cocktailtime.models.DrinkModel
import com.lbartels.cocktailtime.util.SwipeToDeleteCallback

class FragmentSaveOwnCocktail : Fragment() {

    private var _binding: FragmentSaveOwnCocktailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSaveOwnCocktailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fabAddCocktail.setOnClickListener{
            val intent = Intent(context, AddOwnCocktail::class.java)
            startActivityForResult(intent, MemoryFragment.ADD_MEMORY_RC)
        }

        getOwnCocktailFromDB()
    }

    private fun getOwnCocktailFromDB() {
        val dbHandler = DatabaseHandler(requireActivity())
        val getOwnCocktails : ArrayList<DrinkModel> = dbHandler.getOwnCocktailList()

        if (getOwnCocktails.size > 0) {
            binding.rvOwnCocktail.visibility = View.VISIBLE
            binding.tvNoCocktail.visibility = View.GONE
            setupMemoryRV(getOwnCocktails)
        }else {
            binding.rvOwnCocktail.visibility = View.GONE
            binding.tvNoCocktail.visibility = View.VISIBLE
        }
    }

    private fun setupMemoryRV(ownCocktialList: ArrayList<DrinkModel>) {
        binding.rvOwnCocktail.layoutManager = LinearLayoutManager(activity)
        val ownCocktailAdapter = SearchListAdapter(ownCocktialList, requireContext())
        binding.rvOwnCocktail.adapter = ownCocktailAdapter

        ownCocktailAdapter.setOnClickListner(object : SearchListAdapter.OnClickListner{

            override fun onClick(position: Int, model: DrinkModel) {
                val bundle = bundleOf("data" to model)

                activity.let {
                    val intent = Intent(activity, CocktailDetailsActivity::class.java)
                    intent.putExtra("data", bundle)
                    it!!.startActivity(intent)
                }
            }
        })

        val deleteSwipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = binding.rvOwnCocktail.adapter as SearchListAdapter
                adapter.removeOwnAt(viewHolder.adapterPosition)
                getOwnCocktailFromDB()
            }
        }

        val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
        deleteItemTouchHelper.attachToRecyclerView(binding.rvOwnCocktail)

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_MEMORY_RC) {
            if (resultCode == Activity.RESULT_OK) {
                getOwnCocktailFromDB()
            } else{
                Log.e("Activity", "Cancelled")
            }
        }
    }
    companion object {
        var ADD_MEMORY_RC = 1

    }

}