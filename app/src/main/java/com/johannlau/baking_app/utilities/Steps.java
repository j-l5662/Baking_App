package com.johannlau.baking_app.utilities;

import android.os.Parcel;
import android.os.Parcelable;

/*
    Class to consolidate the steps that the recipe requires
 */
public class Steps implements Parcelable{

    private int id;
    private String shortDescription;
    private String longDescription;
    private String videoUrl;
    private String thumbnailURL;

    public Steps(int id,String shortDescription,String longDescription,String videoUrl,String thumbnailURL){

        this.id = id;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.videoUrl = videoUrl;
        this.thumbnailURL = thumbnailURL;
    }

    public Steps(Parcel in) {

        this.id = in.readInt();
        this.shortDescription = in.readString();
        this.longDescription = in.readString();
        this.videoUrl = in.readString();
        this.thumbnailURL = in.readString();
    }
    public int getId() {
        return id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    @Override
    public int describeContents() {
        //TODO
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(id);
        dest.writeString(shortDescription);
        dest.writeString(longDescription);
        dest.writeString(videoUrl);
        dest.writeString(thumbnailURL);
    }

    public static final Parcelable.Creator<Steps> CREATOR = new Parcelable.Creator<Steps>() {
        @Override
        public Steps createFromParcel(Parcel source) {
            return new Steps(source);
        }

        @Override
        public Steps[] newArray(int size) {
            return new Steps[size];
        }
    };
}
