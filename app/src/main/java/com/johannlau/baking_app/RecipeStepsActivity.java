package com.johannlau.baking_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.johannlau.baking_app.utilities.Recipes;

public class RecipeStepsActivity extends AppCompatActivity {

    public static final String RECIPE_STEPS_EXTRA = "RECIPE_STEPS_DATA";
    Recipes recipe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_description_list);

        Intent startedIntent = getIntent();
        if (startedIntent.hasExtra("RECIPE_DETAIL")) {
            recipe = startedIntent.getParcelableExtra("RECIPE_DETAIL");
        }
        RecipeFragment recipeFragment = new RecipeFragment();

        Bundle recipe_steps_bundle = new Bundle();
        recipe_steps_bundle.putParcelable(RECIPE_STEPS_EXTRA,recipe);

        recipeFragment.setArguments(recipe_steps_bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.recipe_step_container,recipeFragment)
                .commit();
    }
}
