package com.example.recipefinderapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchButton.setOnClickListener {
            var intent = Intent(this, RecipeList::class.java)
            val ingredients = ingredientsEdit.text.toString().trim()
            val dish = searchTermEdit.text.toString().trim()

            intent.putExtra("ingredients", ingredients)
            intent.putExtra("dish", dish)

            if (ingredients == "" && dish == "") {
                intent = Intent(this, EmptyScreen::class.java)
                startActivity(intent)
            } else {
                startActivity(intent)
            }

        }
    }
}

