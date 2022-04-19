package com.lbartels.cocktailtime


import com.lbartels.cocktailtime.fragments.SearchCocktailFragment
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import org.mockito.Mock
import org.mockito.Mockito


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class ExampleUnitTest {

    @Mock
    lateinit var searchCocktailFragment: SearchCocktailFragment

    @Before
    fun setUp() {
        searchCocktailFragment = SearchCocktailFragment()
    }

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun returnString() {
        var result = searchCocktailFragment.getRandomCocktail()
        assertTrue(!result.isEmpty())

    }
}