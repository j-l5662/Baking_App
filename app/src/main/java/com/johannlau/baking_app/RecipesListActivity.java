package com.johannlau.baking_app;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.johannlau.baking_app.adapters.RecipeListAdapter;
import com.johannlau.baking_app.utilities.GsonUtils;
import com.johannlau.baking_app.utilities.Ingredients;
import com.johannlau.baking_app.utilities.NetworkUtils;
import com.johannlau.baking_app.utilities.Recipe;
import com.johannlau.baking_app.utilities.RecipeUtils;
import com.johannlau.baking_app.widget.BakingWidgetProvider;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesListActivity extends AppCompatActivity implements RecipeListAdapter.CardViewClickListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = RecipesListActivity.class.getSimpleName();

    public static final String RECIPE_EXTRA = "RECIPE_DETAIL";
    private static final int INGREDIENTS_LOADER_ID = 22;
    private ArrayList<Recipe> ingredientsList;
    private RecyclerView.LayoutManager recipeLayoutManager;

    private SharedPreferences mSharedPreferences;

    @BindView(R.id.mainrecipeslist_recyclerview)
    RecyclerView mRecipeRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_list_main);
        ButterKnife.bind(this);
        Bundle bundle = new Bundle();
        ingredientsNetworkQuery(bundle);
        mRecipeRecyclerView.setHasFixedSize(true);
        recipeLayoutManager = new LinearLayoutManager(this);
        mRecipeRecyclerView.setLayoutManager(recipeLayoutManager);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    private void ingredientsNetworkQuery(Bundle bundle){

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<ArrayList<Ingredients>> ingredientsLoader = loaderManager.getLoader(INGREDIENTS_LOADER_ID);

        IngredientNetworkCall ingredientNetworkCall = new IngredientNetworkCall(this);

        if(ingredientsLoader == null){
            loaderManager.initLoader(INGREDIENTS_LOADER_ID,bundle,ingredientNetworkCall);
        }
        else{
            loaderManager.restartLoader(INGREDIENTS_LOADER_ID,bundle,ingredientNetworkCall);
        }
    }

    @Override
    public void onCardViewClick(int clickedItemIndex) {

        Class recipeDetailsActivity = RecipeStepsActivity.class;
        Recipe recipe = ingredientsList.get(clickedItemIndex);

        Intent intent = new Intent(RecipesListActivity.this, recipeDetailsActivity);
        intent.putExtra(getString(R.string.stepintentextra),recipe);

        String recipeObject = GsonUtils.recipeToStringObj(recipe);
        String preferenceKey = getString(R.string.recipepreferencekey);
        SharedPreferences.Editor preferences = mSharedPreferences.edit();

        preferences.putString(preferenceKey,recipeObject);
        preferences.apply();
        startActivity(intent);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        //Update Widget

        Intent intent = new Intent(this, BakingWidgetProvider.class);

        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(this);

        int[] ids = widgetManager.getAppWidgetIds(new ComponentName(this,BakingWidgetProvider.class));

        RemoteViews rv = new RemoteViews(this.getPackageName(),R.layout.baking_widget_provider);
        String json = sharedPreferences.getString(getString(R.string.recipepreferencekey),null);
        Recipe recipe = GsonUtils.stringtoRecipe(json);

        rv.setTextViewText(R.id.widget_recipe_name_text_view,recipe.getRecipe_name());
        widgetManager.updateAppWidget(new ComponentName(this,BakingWidgetProvider.class),rv);
        widgetManager.notifyAppWidgetViewDataChanged(ids,R.id.widget_recipe_name_text_view);
        widgetManager.notifyAppWidgetViewDataChanged(ids,R.id.widget_list_view);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,ids);
        this.sendBroadcast(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    private static class IngredientNetworkCall implements LoaderManager.LoaderCallbacks<ArrayList<Recipe>> {

        private WeakReference<RecipesListActivity> mainActivityWeakReference;

        public IngredientNetworkCall(RecipesListActivity mainActivity){

            this.mainActivityWeakReference = new WeakReference<>(mainActivity);
        }

        @NonNull
        @Override
        public Loader<ArrayList<Recipe>> onCreateLoader(int id, @Nullable final Bundle args) {

            final Context mainActivity = mainActivityWeakReference.get();
            return new AsyncTaskLoader<ArrayList<Recipe>>(mainActivity) {

                @Override
                protected void onStartLoading() {
                    super.onStartLoading();
                    if (args == null) {
                        return;
                    }
                    forceLoad();
                }

                @Nullable
                @Override
                public ArrayList<Recipe> loadInBackground() {
                    try{
                        if(NetworkUtils.isOnline(mainActivity)){
                            URL url = NetworkUtils.buildURL();
                            String ingredientsJsonResponse = NetworkUtils.getURLResponse(url);
                            ArrayList<Recipe> jsonIngredientsList = RecipeUtils.getRecipes(ingredientsJsonResponse);
                            return jsonIngredientsList;
                        }
                        else{
                            return null;
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            };
        }

        @Override
        public void onLoadFinished(@NonNull Loader<ArrayList<Recipe>> loader, ArrayList<Recipe> data) {

            RecipesListActivity mainActivity = mainActivityWeakReference.get();
            if( data != null ) {
                mainActivity.ingredientsList = data;
                RecipeListAdapter listAdapter = new RecipeListAdapter(mainActivity.ingredientsList,mainActivity);
                mainActivity.mRecipeRecyclerView.setAdapter(listAdapter);

            }
            else{
                Toast.makeText(mainActivity.getApplicationContext(),"Error Connecting to Internet", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<ArrayList<Recipe>> loader) {

        }
    }
}
