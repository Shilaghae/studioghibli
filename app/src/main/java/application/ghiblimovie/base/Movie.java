package application.ghiblimovie.base;

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

    @SerializedName("description")
    String mDescription;

    @SerializedName("director")
    String mDirector;

    @SerializedName("producer")
    String mProducer;

    @SerializedName("release_date")
    String mReleaseDate;

    @SerializedName("rt_score")
    String mScore;

    public Movie() {
        // To remove "Error:A default public constructor with no argument must be declared if a custom constructor is
        // declared." error.
    }

    protected Movie(Parcel in) {
        mId = in.readString();
        mTitle = in.readString();
        mDescription = in.readString();
        mDirector = in.readString();
        mProducer = in.readString();
        mReleaseDate = in.readString();
        mScore = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mTitle);
        dest.writeString(mDescription);
        dest.writeString(mDirector);
        dest.writeString(mProducer);
        dest.writeString(mReleaseDate);
        dest.writeString(mScore);
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

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(final String description) {
        mDescription = description;
    }

    public String getDirector() {
        return mDirector;
    }

    public void setDirector(final String director) {
        mDirector = director;
    }

    public String getProducer() {
        return mProducer;
    }

    public void setProducer(final String producer) {
        mProducer = producer;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(final String releaseDate) {
        mReleaseDate = releaseDate;
    }

    public String getScore() {
        return mScore;
    }

    public void setScore(final String score) {
        mScore = score;
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
