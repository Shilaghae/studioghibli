package application.daggerttest.base;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * @author anna
 */

public class Movie extends RealmObject implements Parcelable {

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

    protected Movie(Parcel in) {
        mId = in.readString();
        mTitle = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mTitle);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

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
