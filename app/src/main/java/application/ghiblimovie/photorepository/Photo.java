package application.ghiblimovie.photorepository;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;

/**
 * @author anna
 */

public class Photo extends RealmObject implements Parcelable {

    private String photoAbsolutePath;

    public Photo() {
        // To remove "Error:A default public constructor with no argument must be declared if a custom constructor is
        // declared." error.
    }

    public Photo(final String path) {
        photoAbsolutePath = path;
    }

    public Photo(Parcel in) {
        photoAbsolutePath = in.readString();
    }

    public String getAbsolutePath() {
        return photoAbsolutePath;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(photoAbsolutePath);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };
}
