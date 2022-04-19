package com.lbartels.cocktailtime.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.ConstraintSet.*
import androidx.core.os.bundleOf
import androidx.fragment.app.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lbartels.cocktailtime.API.ApiService
import com.lbartels.cocktailtime.activities.CocktailDetailsActivity
import com.lbartels.cocktailtime.adapters.OptionListAdapter
import com.lbartels.cocktailtime.models.DrinkResponse
import com.lbartels.cocktailtime.models.DrinkModel
import com.lbartels.cocktailtime.adapters.SearchListAdapter
import com.lbartels.cocktailtime.constants.OptionConstants
import com.lbartels.cocktailtime.databinding.FragmentSearchBinding
import com.lbartels.cocktailtime.models.Options
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList


class SearchCocktailFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var optionRecyclerView: RecyclerView
    private lateinit var manager: RecyclerView.LayoutManager
    private lateinit var optionManager: RecyclerView.LayoutManager

    private lateinit var optionsRecyclerAdapter: OptionListAdapter

    private var drinkList: ArrayList<DrinkModel>? = null
    private var optionList: ArrayList<Options> = OptionConstants.defaultOption()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val view = binding.root
        manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        optionManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupHomeView()

        binding.etSearch.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (event.action == KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    changeLayout()
                    v?.hideKeyboard()
                    getDrink(binding.etSearch.text.toString())
                    return true
                }
                return false
            }
        })
    }

    private fun setupHomeView() {
        val randomIngrident = getRandomCocktail()
        binding.tvCocktailInspiration.text = "Cocktail inspiration: $randomIngrident"
        getDrink(randomIngrident)
        optionRecyclerView = binding.rvOptions.apply {
            optionsRecyclerAdapter = OptionListAdapter(optionList, context)
            layoutManager = optionManager
            adapter = optionsRecyclerAdapter
        }

        optionsRecyclerAdapter.setOnClickListen(object: OptionListAdapter.OnClickListner{
            override fun onClick(position: Int, model: Options) {
                val option = optionList[position].getSearch()
                getDrink(option)
            }

        })

    }

    fun getRandomCocktail(): String {
        val arrayDrink: Array<String> = arrayOf(
            "vodka",
            "Gin",
            "Tequila",
            "Lemon",
            "martini",
            "Mango",
            "pineapple",
            "Honey",
            "Brandy",
            "Amaretto",
            "Rum",
            "Coffee"
        )
        println(arrayDrink.size)
        return arrayDrink[(arrayDrink.indices).random()]
    }

    private fun getDrink(search: String) {
        val api = ApiService.create().getDrink(search)
        api.enqueue(object: Callback<DrinkResponse> {
            override fun onResponse(
                call: retrofit2.Call<DrinkResponse>,
                response: Response<DrinkResponse>
            ) {
                if (response.body()?.drinks == null) {
                    Toast.makeText(activity, "geen restulaat", Toast.LENGTH_LONG).show()
                } else {
                    if (response.isSuccessful) {
                        drinkList = response.body()!!.drinks
                        setUpSearchResultRV(drinkList!!)
                    }
                }
            }
            override fun onFailure(call: retrofit2.Call<DrinkResponse>, t: Throwable) {
                t.printStackTrace()
                return
            }
        })
    }

    private fun changeLayout() {
        binding.rvOptions.visibility = View.GONE
        binding.tvOption.visibility = View.GONE
        binding.tvCocktailInspiration.visibility = View.GONE
//      TODO: fix constraint parameters when layout change
//        binding.rvSearch.layoutParams = ViewGroup.LayoutParams.MATCH_PARENT, "0dp"
        val constraintSet = ConstraintSet()
        constraintSet.clone(binding.search)

        constraintSet.connect(
            binding.rvSearch.id,
            TOP,
            binding.textInputLayout.id,
            BOTTOM
        )

        constraintSet.connect(
            binding.rvSearch.id,
            START,
            PARENT_ID,
            START
        )
        constraintSet.connect(
            binding.rvSearch.id,
            END,
            PARENT_ID,
            END
        )
        constraintSet.applyCustomAttributes(binding.search)
    }

    private fun View.hideKeyboard() {
        val methodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        methodManager.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun setUpSearchResultRV(list: ArrayList<DrinkModel>) {
        binding.rvSearch.layoutManager = LinearLayoutManager(activity)
        val searchAdapter = SearchListAdapter(list, requireContext())
        binding.rvSearch.adapter = searchAdapter


        searchAdapter.setOnClickListner(object : SearchListAdapter.OnClickListner{
            override fun onClick(position: Int, model: DrinkModel) {
                val bundle = bundleOf("data" to drinkList!![position])
                binding.etSearch.text!!.clear()

                activity.let {
                    val intent = Intent(activity, CocktailDetailsActivity::class.java)
                    intent.putExtra("data", bundle)
                    it!!.startActivity(intent)
                }
            }
        })


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}