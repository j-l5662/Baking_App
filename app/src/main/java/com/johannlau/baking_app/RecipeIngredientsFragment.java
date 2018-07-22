package com.johannlau.baking_app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.johannlau.baking_app.adapters.RecipeIngredientsAdapter;
import com.johannlau.baking_app.utilities.Ingredients;
import com.johannlau.baking_app.utilities.Recipes;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeIngredientsFragment extends Fragment {

    @BindView(R.id.recipeIngredients_recyclerview)
    RecyclerView recipeIngredientsRecyclerView;

    private RecipeIngredientsAdapter recipeIngredientsAdapter;
    private static final String INGREDIENTS_EXTRA = "ingredients";

    public RecipeIngredientsFragment() {  }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.recipe_ingredients_fragment,container,false);

        ButterKnife.bind(this,rootView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recipeIngredientsRecyclerView.setLayoutManager(layoutManager);
        Recipes recipes = getArguments().getParcelable(INGREDIENTS_EXTRA);
        ArrayList<Ingredients> ingredients = recipes.getRecipe_ingredients();
        recipeIngredientsAdapter = new RecipeIngredientsAdapter(ingredients);

        recipeIngredientsRecyclerView.setAdapter(recipeIngredientsAdapter);

        return rootView;
    }
}
