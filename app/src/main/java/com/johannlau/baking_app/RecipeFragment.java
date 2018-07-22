package com.johannlau.baking_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.johannlau.baking_app.adapters.RecipeDetailAdapter;
import com.johannlau.baking_app.utilities.Recipes;
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
    private static final String INGREDIENTS_EXTRA = "ingredients_detail";
    private Recipes recipes;

    public RecipeFragment() {  }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.recipe_description_fragment,container,false);
        ButterKnife.bind(this,rootView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recipeItemsRecyclerView.setLayoutManager(layoutManager);
        recipes = getArguments().getParcelable(RecipeStepsActivity.RECIPE_STEPS_EXTRA);
        ArrayList<Steps> recipe_steps = recipes.getRecipe_steps();
        recipeDetailAdapter = new RecipeDetailAdapter(recipe_steps,this);


        recipeItemsRecyclerView.setAdapter(recipeDetailAdapter);
        return rootView;
    }

    @OnClick(R.id.recipe_ingredient_card_view)
    public void onClick(){
//        Toast.makeText(getContext(),"Ingredients",Toast.LENGTH_SHORT).show();
        Class recipeIngredientsActivity = RecipeIngredientsActivity.class;
        Intent intent = new Intent(getContext(),recipeIngredientsActivity);

        intent.putExtra(INGREDIENTS_EXTRA, recipes);
        startActivity(intent);

    }

    @Override
    public void onCardViewClick(int clickedposition) {
        Steps step= recipes.getRecipe_steps().get(clickedposition);
        Class stepDetailsActivity = StepDetailActivity.class;

        Intent intent = new Intent(getContext(), stepDetailsActivity);
        intent.putExtra("STEP_DETAIL",step);
        startActivity(intent);

    }
}
