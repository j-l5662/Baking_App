package com.johannlau.baking_app;



import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.johannlau.baking_app.utilities.Recipes;

public class RecipeIngredientsActivity extends AppCompatActivity {

    Recipes recipe;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_ingredients_list);

//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(false);
        Intent startedIntent = getIntent();
        if(startedIntent.hasExtra("ingredients_detail")) {
            recipe = startedIntent.getParcelableExtra("ingredients_detail");
        }

        RecipeIngredientsFragment recipeIngredientsFragment = new RecipeIngredientsFragment();

        Bundle recipe_data = new Bundle();
        recipe_data.putParcelable("ingredients",recipe);

        recipeIngredientsFragment.setArguments(recipe_data);

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.recipe_ingredients_container,recipeIngredientsFragment)
                .commit();
    }
}
