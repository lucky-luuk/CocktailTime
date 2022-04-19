package com.lbartels.cocktailtime.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lbartels.cocktailtime.activities.AddMemory
import com.lbartels.cocktailtime.adapters.MemoryListAdapter
import com.lbartels.cocktailtime.database.DatabaseHandler
import com.lbartels.cocktailtime.databinding.FragmentMemoryBinding
import com.lbartels.cocktailtime.models.MemoryModel
import com.lbartels.cocktailtime.util.SwipeToDeleteCallback


class MemoryFragment : Fragment() {
    private var _binding: FragmentMemoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMemoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabAddMemory.setOnClickListener{
            val intent = Intent(context, AddMemory::class.java)
            startActivityForResult(intent, ADD_MEMORY_RC)
        }
        getCocktailMemoryFromDB()
    }

    private fun getCocktailMemoryFromDB() {
        val dbHandler = DatabaseHandler(requireActivity())
        val getMemoryList : ArrayList<MemoryModel> = dbHandler.getCocktailMemoryList()

        if (getMemoryList.size > 0) {
            binding.rvCocktailMemory.visibility = View.VISIBLE
            binding.tvNoMemory.visibility = View.GONE
            setupMemoryRV(getMemoryList)
        }else {
            binding.rvCocktailMemory.visibility = View.GONE
            binding.tvNoMemory.visibility = View.VISIBLE
        }
    }

    private fun setupMemoryRV(memoryList: ArrayList<MemoryModel>) {
        binding.rvCocktailMemory.layoutManager = LinearLayoutManager(activity)
        val memoryAdapter = MemoryListAdapter(requireContext(), memoryList)
        binding.rvCocktailMemory.adapter = memoryAdapter

        memoryAdapter.setOnClickListner(object : MemoryListAdapter.OnClickListner{
            override fun onClick(position: Int, model: MemoryModel) {

            }
        })

        val deleteSwipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = binding.rvCocktailMemory.adapter as MemoryListAdapter
                adapter.removeAt(viewHolder.adapterPosition)
                getCocktailMemoryFromDB()
            }
        }

        val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
        deleteItemTouchHelper.attachToRecyclerView(binding.rvCocktailMemory)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_MEMORY_RC) {
            if (resultCode == Activity.RESULT_OK) {
                getCocktailMemoryFromDB()
            } else{
                Log.e("Activity", "Cancelled")
            }
        }
    }

    companion object {
        var ADD_MEMORY_RC = 1
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}