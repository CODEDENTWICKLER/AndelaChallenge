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
        this.username = username.toLowerCase();
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Developer developer = (Developer) o;

        if (username != null ? !username.equals(developer.username) : developer.username != null)
            return false;
        if (imageUrl != null ? !imageUrl.equals(developer.imageUrl) : developer.imageUrl != null)
            return false;
        return githubProfileUrl != null ? githubProfileUrl.equals(developer.githubProfileUrl) : developer.githubProfileUrl == null;

    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
        result = 31 * result + (githubProfileUrl != null ? githubProfileUrl.hashCode() : 0);
        return result;
    }
}
