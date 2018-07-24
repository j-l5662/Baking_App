package com.johannlau.baking_app;



import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.johannlau.baking_app.utilities.Recipe;

public class RecipeIngredientsActivity extends AppCompatActivity {

    Recipe recipe;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_ingredients_list);

        Intent startedIntent = getIntent();
        String intentExtra = getString(R.string.recipeingredientsextra);
        if(startedIntent.hasExtra(intentExtra)) {
            recipe = startedIntent.getParcelableExtra(intentExtra);
        }

        RecipeIngredientsFragment recipeIngredientsFragment = new RecipeIngredientsFragment();

        Bundle recipe_data = new Bundle();
        recipe_data.putParcelable(getString(R.string.recipeingredientsextra),recipe);

        recipeIngredientsFragment.setArguments(recipe_data);

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.recipe_ingredients_container,recipeIngredientsFragment)
                .commit();
    }
}
