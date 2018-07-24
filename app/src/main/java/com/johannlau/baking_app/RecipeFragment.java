package com.johannlau.baking_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.johannlau.baking_app.adapters.RecipeDetailAdapter;
import com.johannlau.baking_app.utilities.Recipe;
import com.johannlau.baking_app.utilities.Steps;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecipeFragment extends Fragment implements RecipeDetailAdapter.DetailCardViewClickListener {

    @BindView(R.id.recipe_ingredient_card_view)
    CardView recipeIngredientCard;
    @BindView(R.id.recipeDescription_recyclerview)
    RecyclerView recipeItemsRecyclerView;

    private RecipeDetailAdapter recipeDetailAdapter;
    private Recipe recipe;
    private boolean mTwoPane;

    public RecipeFragment() {  }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.recipe_description_fragment,container,false);
        ButterKnife.bind(this,rootView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recipeItemsRecyclerView.setLayoutManager(layoutManager);
        recipe = getArguments().getParcelable(RecipeStepsActivity.RECIPE_STEPS_EXTRA);
        ArrayList<Steps> recipe_steps = recipe.getRecipe_steps();
        if(getArguments().getBoolean(getString(R.string.tabletmode)) == true){
            mTwoPane = true;
            launchRecipeFragment();

        }
        else{
            mTwoPane = false;
        }

        recipeDetailAdapter = new RecipeDetailAdapter(recipe_steps,this);


        recipeItemsRecyclerView.setAdapter(recipeDetailAdapter);
        return rootView;
    }

    @OnClick(R.id.recipe_ingredient_card_view)
    public void onClick(){
        if(mTwoPane) {
            launchRecipeFragment();
        }
        else{
            Class recipeIngredientsActivity = RecipeIngredientsActivity.class;
            Intent intent = new Intent(getContext(),recipeIngredientsActivity);

            intent.putExtra(getString(R.string.recipeingredientsextra), recipe);
            startActivity(intent);
        }
    }

    @Override
    public void onCardViewClick(int clickedposition) {
        Steps step = recipe.getRecipe_steps().get(clickedposition);
        if(mTwoPane){

            launchDetailFragment(step);
        }
        else{
            Class stepDetailsActivity = StepDetailActivity.class;

            Intent intent = new Intent(getContext(), stepDetailsActivity);
            intent.putExtra(getString(R.string.stepdetail),step);
            startActivity(intent);
        }
    }

    private void launchRecipeFragment() {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        RecipeIngredientsFragment recipeIngredientsFragment = new RecipeIngredientsFragment();
        Bundle ingredientBundle = new Bundle();
        ingredientBundle.putParcelable(getString(R.string.recipeingredientsextra), recipe);
        recipeIngredientsFragment.setArguments(ingredientBundle);
        fragmentManager.beginTransaction().replace(R.id.detail_layout,recipeIngredientsFragment)
                .addToBackStack(null)
                .commit();
    }

    private void launchDetailFragment(Steps step) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        StepDetailFragment stepDetailFragment = new StepDetailFragment();

        Bundle stepBundle = new Bundle();
        stepBundle.putParcelable((getString(R.string.fragmentstepdetail)),step);
        stepDetailFragment.setArguments(stepBundle);
        fragmentManager.beginTransaction().replace(R.id.detail_layout,stepDetailFragment)
                .addToBackStack(null)
                .commit();

    }
}
