package com.johannlau.baking_app.utilities;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecipeUtils {

    private static final String TAG = RecipeUtils.class.getSimpleName();

    public static ArrayList<Recipes> getRecipes(String json) throws JSONException {

        ArrayList<Recipes> recipes = new ArrayList<>();
        ArrayList<Ingredients> ingredients = new ArrayList<>();
        ArrayList<Steps> steps = new ArrayList<>();

        JSONArray recipesJSONArray = new JSONArray(json);
        for(int i = 0; i < recipesJSONArray.length(); i++){

            JSONObject jsonRecipeObject = recipesJSONArray.getJSONObject(i);
            int recipeID = jsonRecipeObject.getInt("id");
            String recipeName = jsonRecipeObject.getString("name");
            JSONArray recipeIngredients = jsonRecipeObject.getJSONArray("ingredients");
            JSONArray recipeSteps = jsonRecipeObject.getJSONArray("steps");
            try {
                ingredients = getIngredients(recipeIngredients);
                steps = getSteps(recipeSteps);
            }
            catch (JSONException e){
                printJSONException(e);
            }
            Recipes recipe = new Recipes(recipeID,recipeName,ingredients,steps);
            recipes.add(recipe);
        }
        return recipes;
    }

    private static ArrayList<Ingredients> getIngredients(JSONArray jsonArray) throws JSONException {

        ArrayList<Ingredients> ingredientsList = new ArrayList<>();

        for(int i =0; i < jsonArray.length(); i++) {
            JSONObject result = jsonArray.getJSONObject(i);

            double quantity = result.getDouble("quantity");
            String measurement = result.getString("measure");
            String ingredient = result.getString("ingredient");

            ingredientsList.add(new Ingredients(quantity,measurement,ingredient));
        }

        if(ingredientsList.size() == 0){
            Log.e(TAG,"Error: Zero ingredients found");
        }
        return ingredientsList;
    }

    private static ArrayList<Steps> getSteps(JSONArray jsonArray) throws JSONException {

        ArrayList<Steps> stepsList = new ArrayList<>();

        for(int i = 0; i < jsonArray.length();i++){
            JSONObject step = jsonArray.getJSONObject(i);

            int id = step.getInt("id");
            String shortDescription = step.getString("shortDescription");
            String description = step.getString("description");
            String videoURL = step.getString("videoURL");
            String thumbnailURL = step.getString("thumbnailURL");
            stepsList.add(new Steps(id,shortDescription,description,videoURL,thumbnailURL));
        }
        if(stepsList.size() == 0){
            Log.e(TAG,"Error: Zero steps found");
        }
        return stepsList;
    }

    private static void printJSONException(JSONException e) {
        e.printStackTrace();
        Log.e(TAG, "JSON Error");
    }
}
