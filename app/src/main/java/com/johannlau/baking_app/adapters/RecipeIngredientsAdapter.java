package com.johannlau.baking_app.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.johannlau.baking_app.R;
import com.johannlau.baking_app.utilities.Ingredients;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeIngredientsAdapter extends RecyclerView.Adapter<RecipeIngredientsAdapter.ViewHolder> {

    private ArrayList<Ingredients> recipeIngredients;

    public RecipeIngredientsAdapter(ArrayList<Ingredients> recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_ingredients_cardview,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.recipeIngredientTextView.setText(recipeIngredients.get(position).getIngredient());
    }

    @Override
    public int getItemCount() {
        return recipeIngredients.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.recipes_ingredients_cardview)
        CardView recipeIngredientsCardView;
        @BindView(R.id.recipe_ingredient)
        TextView recipeIngredientTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
