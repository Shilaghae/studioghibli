package application.ghiblimovie.features.photohome;

import android.graphics.Bitmap;

/**
 * @author anna
 */

public class PhotoItem {

    private final String path;
    private Bitmap bitmap;

    public PhotoItem(String photo, Bitmap bitmap) {
        this.path = photo;
        this.bitmap = bitmap;
    }

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
}
