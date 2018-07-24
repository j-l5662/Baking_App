package com.johannlau.baking_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.johannlau.baking_app.utilities.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepsActivity extends AppCompatActivity {

    public static final String RECIPE_STEPS_EXTRA = "RECIPE_STEPS_DATA";

    private boolean mTwoPane;
    private Recipe recipe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_description_list);
        ButterKnife.bind(this);

        Intent startedIntent = getIntent();
        String stepIntentExtra = getString(R.string.stepintentextra);

        if (startedIntent.hasExtra(stepIntentExtra)) {
            recipe = startedIntent.getParcelableExtra(stepIntentExtra);
        }

        if(ButterKnife.findById(this,R.id.wide_recipe_layout) != null) {
            mTwoPane = true;

            FragmentManager fragmentManager = getSupportFragmentManager();
            RecipeFragment recipeFragment = new RecipeFragment();
            Bundle recipe_two_pane = new Bundle();
            recipe_two_pane.putBoolean(getString(R.string.tabletmode),mTwoPane);
            recipe_two_pane.putParcelable(RECIPE_STEPS_EXTRA,recipe);
            recipeFragment.setArguments(recipe_two_pane);
            fragmentManager.beginTransaction()
                    .add(R.id.recipe_step_container,recipeFragment)
                    .commit();
        }
        else {
            mTwoPane = false;
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
}
