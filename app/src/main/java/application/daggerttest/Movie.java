package application.daggerttest;

import com.google.gson.annotations.SerializedName;

/**
 * @author anna
 */

public class Movie {

    @SerializedName("id")
    String mId;

    @SerializedName("title")
    String mTitle;

    public Movie(String id, String title) {
        mId = id;
        mTitle = title;
    }

    public String getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }
}
