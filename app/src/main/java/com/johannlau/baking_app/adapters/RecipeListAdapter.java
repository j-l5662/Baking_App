package com.johannlau.baking_app.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.johannlau.baking_app.R;
import com.johannlau.baking_app.utilities.Recipes;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.ViewHolder>{

    private ArrayList<Recipes> recipesList;
    final private CardViewClickListener mOnClickListener;

    public interface CardViewClickListener {
        void onCardViewClick(int clickedItemIndex);
    }

    public RecipeListAdapter(ArrayList<Recipes> recipesList, CardViewClickListener cardViewClickListener) {

        this.recipesList = recipesList;
        this.mOnClickListener = cardViewClickListener;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_name_cardview,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.recipeName.setText(recipesList.get(position).getRecipe_name());
    }

    @Override
    public int getItemCount() {

        return recipesList.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.recipe_name)
        TextView recipeName;
        @BindView(R.id.mainrecipeslist_cardview)
        CardView recipeCardView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickposition = getAdapterPosition();
            mOnClickListener.onCardViewClick(clickposition);
        }
    }
}
