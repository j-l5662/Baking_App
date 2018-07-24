package com.johannlau.baking_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.johannlau.baking_app.adapters.RecipeDetailAdapter;
import com.johannlau.baking_app.utilities.Steps;

public class StepDetailActivity extends AppCompatActivity {

    public static final String STEPS_DETAILS = "DETAIL_DATA";
    Steps recipeStep;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_detail_activity);

        Intent startedIntent = getIntent();
        String stepDetailExtra = getString(R.string.stepdetail);
        if(startedIntent.hasExtra(stepDetailExtra)) {
            recipeStep = startedIntent.getParcelableExtra(stepDetailExtra);
        }

        StepDetailFragment detailFragment = new StepDetailFragment();

        Bundle recipe_detail_bundle = new Bundle();
        recipe_detail_bundle.putParcelable(getString(R.string.fragmentstepdetail), recipeStep);

        detailFragment.setArguments(recipe_detail_bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.step_detail_container,detailFragment)
                    .commit();
        }
    }
}
