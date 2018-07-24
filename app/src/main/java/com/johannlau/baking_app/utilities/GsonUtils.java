package com.johannlau.baking_app.utilities;

import android.util.JsonReader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.StringReader;
import java.lang.reflect.Type;

//Utils class to parse Recipe objects into Strings for SharedPreference Storage
public class GsonUtils {


    //Convert the Recipe Object into a String for Shared Preference
    public static String recipeToStringObj(Recipe recipe) {
        if(recipe == null) {
            return null;
        }
        Gson gsonObject = new GsonBuilder().create();


        String recipeString =  gsonObject.toJson(recipe);

        return recipeString;
    }

    //Convert the String object back to the Recipe Class to read from Shared Preference

    public static Recipe stringtoRecipe(String gsonStringObj) {

        if(gsonStringObj == null || gsonStringObj.isEmpty()) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<Recipe>() {}.getType();
        Recipe recipe = gson.fromJson(gsonStringObj,type);

        return recipe;
    }

}
