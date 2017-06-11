package application.ghiblimovie.features.photohome;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class PhotoItem implements Parcelable {

    private final String path;
    private Bitmap bitmap;

    public PhotoItem(String photo, Bitmap bitmap) {
        this.path = photo;
        this.bitmap = bitmap;
    }

    protected PhotoItem(Parcel in) {
        path = in.readString();
        bitmap = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<PhotoItem> CREATOR = new Creator<PhotoItem>() {
        @Override
        public PhotoItem createFromParcel(Parcel in) {
            return new PhotoItem(in);
        }

        @Override
        public PhotoItem[] newArray(int size) {
            return new PhotoItem[size];
        }
    };

    @Override
    public boolean equals(final Object obj) {
        if(obj instanceof PhotoItem) {
            final PhotoItem pi = (PhotoItem) obj;
            return path.equals(pi.getPath());
        }
        return false;
    }

    public String getPath() {
        return path;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(path);
        dest.writeParcelable(bitmap, flags);
    }

    public void setLocation(final String location) {
    }
}
