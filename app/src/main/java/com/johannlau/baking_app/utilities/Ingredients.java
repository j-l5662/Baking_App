package com.johannlau.baking_app.utilities;

/*
        Class to identify the ingredients that are required for the recipe
 */

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredients implements Parcelable {

    private String measurement;
    private double quantity;
    private String ingredient;

    public Ingredients(double quantity,String measurement,String ingredient){

        this.quantity = quantity;
        this.measurement = measurement;
        this.ingredient = ingredient;
    }

    public Ingredients(Parcel in) {

        this.quantity = in.readDouble();
        this.measurement = in.readString();
        this.ingredient = in.readString();
    }

    public String getMeasurement() {
        return measurement;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getIngredient() {
        return ingredient;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeDouble(quantity);
        dest.writeString(measurement);
        dest.writeString(ingredient);
    }

    public static final Parcelable.Creator<Ingredients> CREATOR = new Parcelable.Creator<Ingredients>() {
        @Override
        public Ingredients createFromParcel(Parcel source) {
            return new Ingredients(source);
        }

        @Override
        public Ingredients[] newArray(int size) {
            return new Ingredients[size];
        }
    };
}
