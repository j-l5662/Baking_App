package com.johannlau.baking_app.utilities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Recipes implements Parcelable{

    private ArrayList<Ingredients> recipe_ingredients = new ArrayList<>();
    private ArrayList<Steps> recipe_steps = new ArrayList<>();
    private int recipe_id;
    private String recipe_name;

    public Recipes(int recipe_id,String recipe_name, ArrayList<Ingredients> ingredients, ArrayList<Steps> steps){

        this.recipe_id = recipe_id;
        this.recipe_ingredients = ingredients;
        this.recipe_steps = steps;
        this.recipe_name = recipe_name;
    }

    public Recipes(Parcel in) {
        this.recipe_id = in.readInt();
        in.readTypedList(this.recipe_ingredients, Ingredients.CREATOR);
        in.readTypedList(this.recipe_steps, Steps.CREATOR);
        this.recipe_name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(recipe_id);
        dest.writeTypedList(this.recipe_ingredients);
        dest.writeTypedList(this.recipe_steps);
        dest.writeString(recipe_name);
    }
    public ArrayList<Ingredients> getRecipe_ingredients() {
        return recipe_ingredients;
    }

    public ArrayList<Steps> getRecipe_steps() {
        return recipe_steps;
    }

    public int getRecipe_id() {
        return recipe_id;
    }

    public String getRecipe_name() {
        return recipe_name;
    }

    public static final Parcelable.Creator<Recipes> CREATOR = new Parcelable.Creator<Recipes>() {
        @Override
        public Recipes createFromParcel(Parcel source) {
            return new Recipes(source);
        }

        @Override
        public Recipes[] newArray(int size) {
            return new Recipes[size];
        }
    };
}
