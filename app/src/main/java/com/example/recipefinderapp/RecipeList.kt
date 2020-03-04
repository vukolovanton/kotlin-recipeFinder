package com.example.recipefinderapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request

import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest

import com.android.volley.toolbox.Volley
import com.example.recipefinderapp.data.DISH_LINK
import com.example.recipefinderapp.data.LEFT_LINK
import com.example.recipefinderapp.data.QUERY
import com.example.recipefinderapp.data.RecipeListAdapter
import kotlinx.android.synthetic.main.activity_recipe_list.*
import org.json.JSONException
import org.json.JSONObject


class RecipeList : AppCompatActivity() {

    var volleyRequest: RequestQueue? = null
    var recipeList: ArrayList<Recipe>? = null
    var recipeAdapter: RecipeListAdapter? = null
    var layoutManager: RecyclerView.LayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_list)

        var urlString = "http://www.recipepuppy.com/api/?i=onions,garlic&q=omelet&p=3"

        var url: String = ""

        val extras = intent.extras
        val ingredients = extras!!.get("ingredients").toString()
        val dish = extras.get("dish").toString()



        if (ingredients == "" && dish != "") {
            val tempIngredientUrl = DISH_LINK+dish
            url = tempIngredientUrl
        } else if (ingredients != "" && dish == "") {
            val tempIngredientUrl = LEFT_LINK+ingredients
            url = tempIngredientUrl
        } else if (ingredients == "" && dish == "") {
            Toast.makeText(this, "Type something", Toast.LENGTH_LONG).show()
        }

        getRecipe(url)

        recipeList = ArrayList<Recipe>()
        volleyRequest = Volley.newRequestQueue(this)

    }

    fun getRecipe(url: String) {

        val recipeRequest = JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
            response ->
            try {

                val resultArray = response.getJSONArray("results")
                for (i in 0..resultArray.length() - 1) {
                    var recipeObj = resultArray.getJSONObject(i)

                    var title = recipeObj.getString("title")
                    var link = recipeObj.getString("href")
                    var thumbnail = recipeObj.getString("thumbnail")
                    var ingredients = recipeObj.getString("ingredients")

                    var recipe = Recipe()
                    recipe.title = title
                    recipe.link = link
                    recipe.thumbnail = thumbnail
                    recipe.ingredients = "Ingredients: $ingredients"

                    recipeList!!.add(recipe)

                    //Adapter
                    recipeAdapter = RecipeListAdapter(recipeList!!, this)
                    layoutManager = LinearLayoutManager(this)

                    //recyclerview
                    recyclerViewId.layoutManager = layoutManager
                    recyclerViewId.adapter = recipeAdapter

                }

                recipeAdapter!!.notifyDataSetChanged()

            } catch (e: Exception) {

                e.printStackTrace()
            }

        }, Response.ErrorListener {
            error: VolleyError? ->
            try {
                Log.d("MYERROR", error.toString())
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        })
        Volley.newRequestQueue(this).add(recipeRequest)
    }
}
