package com.johannlau.baking_app.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.johannlau.baking_app.R;
import com.johannlau.baking_app.utilities.GsonUtils;
import com.johannlau.baking_app.utilities.Ingredients;
import com.johannlau.baking_app.utilities.Recipe;

import java.util.ArrayList;

public class BakingWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private ArrayList<Ingredients> mIngredients;

    public BakingWidgetRemoteViewsFactory(Context applicationContext, Intent intent) {

        mContext = applicationContext;
        getRecipeFromSharedPreference();
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        getRecipeFromSharedPreference();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mIngredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.baking_widget_list_view);
        Ingredients ingredients = mIngredients.get(position);
        rv.setTextViewText(R.id.widget_adapter_text_view,ingredients.getIngredient());
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private void getRecipeFromSharedPreference() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);

        String preferenceKey = mContext.getString(R.string.recipepreferencekey);
        String recipeJson = sharedPreferences.getString(preferenceKey,null);
        if(recipeJson.isEmpty() || recipeJson == null) {
            return;
        }
        Recipe recipe = GsonUtils.stringtoRecipe(recipeJson);
        mIngredients = recipe.getRecipe_ingredients();
    }

}
