package com.johannlau.baking_app;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.johannlau.baking_app.adapters.RecipeListAdapter;
import com.johannlau.baking_app.utilities.Ingredients;
import com.johannlau.baking_app.utilities.NetworkUtils;
import com.johannlau.baking_app.utilities.RecipeUtils;
import com.johannlau.baking_app.utilities.Recipes;
import com.johannlau.baking_app.utilities.Steps;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesListActivity extends AppCompatActivity implements RecipeListAdapter.CardViewClickListener {

    private static final int INGREDIENTS_LOADER_ID = 22;
    private ArrayList<Recipes> ingredientsList;
    private RecyclerView.LayoutManager recipeLayoutManager;
    private static final String RECIPE_EXTRA = "RECIPE_DETAIL";

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
        Recipes recipe = ingredientsList.get(clickedItemIndex);

        Intent intent = new Intent(RecipesListActivity.this, recipeDetailsActivity);
        intent.putExtra(RECIPE_EXTRA,recipe);
        startActivity(intent);
    }

    private static class IngredientNetworkCall implements LoaderManager.LoaderCallbacks<ArrayList<Recipes>> {

        private WeakReference<RecipesListActivity> mainActivityWeakReference;

        public IngredientNetworkCall(RecipesListActivity mainActivity){

            this.mainActivityWeakReference = new WeakReference<>(mainActivity);
        }

        @NonNull
        @Override
        public Loader<ArrayList<Recipes>> onCreateLoader(int id, @Nullable final Bundle args) {

            final Context mainActivity = mainActivityWeakReference.get();
            return new AsyncTaskLoader<ArrayList<Recipes>>(mainActivity) {

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
                public ArrayList<Recipes> loadInBackground() {
                    try{
                        if(NetworkUtils.isOnline(mainActivity)){
                            URL url = NetworkUtils.buildURL();
                            String ingredientsJsonResponse = NetworkUtils.getURLResponse(url);
                            ArrayList<Recipes> jsonIngredientsList = RecipeUtils.getRecipes(ingredientsJsonResponse);
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
        public void onLoadFinished(@NonNull Loader<ArrayList<Recipes>> loader, ArrayList<Recipes> data) {

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
        public void onLoaderReset(@NonNull Loader<ArrayList<Recipes>> loader) {

        }
    }
}
