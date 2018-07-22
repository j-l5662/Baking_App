package com.johannlau.baking_app.adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.johannlau.baking_app.R;
import com.johannlau.baking_app.StepDetailActivity;
import com.johannlau.baking_app.utilities.Steps;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecipeDetailAdapter extends RecyclerView.Adapter<RecipeDetailAdapter.ViewHolder> {

    private ArrayList<Steps> recipesDescriptions;
    final private DetailCardViewClickListener mOnClickListner;

    public interface DetailCardViewClickListener {
        void onCardViewClick(int clickedposition);
    }
    public RecipeDetailAdapter(ArrayList<Steps> recipesDescriptions, DetailCardViewClickListener detailCardViewClickListener) {

        this.recipesDescriptions = recipesDescriptions;
        this.mOnClickListner = detailCardViewClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_steps_cardview,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.recipeDescriptionTextView.setText(recipesDescriptions.get(position).getShortDescription());
        holder.recipeStepsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Steps step= recipesDescriptions.get(position);
                Class stepDetailsActivity = StepDetailActivity.class;

                Intent intent = new Intent(v.getContext(), stepDetailsActivity);
                intent.putExtra("STEP_DETAIL",step);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipesDescriptions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.recipes_steps_cardview)
        CardView recipeStepsCardView;
        @BindView(R.id.recipe_step)
        TextView recipeDescriptionTextView;

        public ViewHolder(View itemView) {

            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @Override
        public void onClick(View view) {
            int clickPosition = getAdapterPosition();
            mOnClickListner.onCardViewClick(clickPosition);
        }
    }
}
