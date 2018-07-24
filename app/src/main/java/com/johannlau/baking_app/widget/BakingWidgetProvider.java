package com.johannlau.baking_app.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.johannlau.baking_app.R;
import com.johannlau.baking_app.RecipeIngredientsActivity;
import com.johannlau.baking_app.RecipeStepsActivity;
import com.johannlau.baking_app.RecipesListActivity;
import com.johannlau.baking_app.utilities.GsonUtils;
import com.johannlau.baking_app.utilities.Recipe;

/**
 * Implementation of App Widget functionality.
 */
public class BakingWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {


        Intent intent = null;

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_widget_provider);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String recipeJSON = sharedPreferences.getString(context.getString(R.string.recipepreferencekey),null);
        Recipe recipe = GsonUtils.stringtoRecipe(recipeJSON);
        String recipeName = recipe.getRecipe_name();

        if(recipeJSON == null || recipeJSON.isEmpty()) {
            Log.e("Error","Shared Preference Error");
        }
        else{
            intent = new Intent(context,RecipeStepsActivity.class);
            intent.putExtra(RecipesListActivity.RECIPE_EXTRA,recipe);
            Log.v("TEst",recipeName);
        }
        Log.v("TEst",recipeName);

        views.setTextViewText(R.id.widget_recipe_name_text_view,recipe.getRecipe_name());

        Intent listIntent = new Intent(context,BakingAppWidgetRemoteViewsService.class);
        views.setRemoteAdapter(R.id.widget_list_view,listIntent);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_recipe_name_text_view,pendingIntent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

