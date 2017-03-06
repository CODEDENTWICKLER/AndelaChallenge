package com.example.andelachallenge.model;

/**
 * Created by codedentwickler on 3/4/17.
 */

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class is a models and encapsulates the state
 * and properties of each developer
 * */


public class Developer implements Parcelable {

    private String username;
    private String imageUrl;
    private String githubProfileUrl;

    public Developer(String username, String imageUrl, String githubUrl) {
        this.username = username;
        this.imageUrl = imageUrl;
        this.githubProfileUrl = githubUrl;
    }


    protected Developer(Parcel in) {
        username = in.readString();
        imageUrl = in.readString();
        githubProfileUrl = in.readString();
    }

    public static final Creator<Developer> CREATOR = new Creator<Developer>() {
        @Override
        public Developer createFromParcel(Parcel in) {
            return new Developer(in);
        }

        @Override
        public Developer[] newArray(int size) {
            return new Developer[size];
        }
    };

    public String getUsername() {
        return username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getGithubProfileUrl() {
        return githubProfileUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(imageUrl);
        dest.writeString(githubProfileUrl);
    }
}
