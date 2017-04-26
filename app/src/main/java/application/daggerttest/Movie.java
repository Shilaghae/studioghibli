package application.daggerttest;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * @author anna
 */

public class Movie extends RealmObject {

    @SerializedName("id")
    String mId;

    @SerializedName("title")
    String mTitle;

    public Movie() {
        // To remove "Error:A default public constructor with no argument must be declared if a custom constructor is
        // declared." error.
    }

    public Movie(String id, String title) {
        mId = id;
        mTitle = title;
    }

    public void setId(final String id) {
        mId = id;
    }

    public void setTitle(final String title) {
        mTitle = title;
    }

    public String getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }
}
