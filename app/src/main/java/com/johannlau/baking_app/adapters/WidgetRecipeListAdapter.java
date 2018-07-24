package com.johannlau.baking_app.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.johannlau.baking_app.R;
import com.johannlau.baking_app.utilities.Recipe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WidgetRecipeListAdapter extends ArrayAdapter<Recipe> {

    @BindView(R.id.widget_adapter_text_view)
    TextView widgetRecipeNameTextView;

    public WidgetRecipeListAdapter(Context context, ArrayList<Recipe> recipes) {
        super(context,0,recipes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Recipe recipeName = getItem(position);
        if (convertView == null) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.baking_widget_list_view,parent);
            ButterKnife.bind(this,view);
        }
        widgetRecipeNameTextView.setText(recipeName.getRecipe_name());
        return convertView;
    }
}
