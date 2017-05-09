package application.ghiblimovie.photorepository;

import android.content.Context;

import java.util.List;

import io.realm.Realm;

/**
 * @author anna
 */

public class PhotoRepository {

    private Realm mRealm;

    public PhotoRepository(final Context context) {
        Realm.init(context);
        mRealm = Realm.getDefaultInstance();
    }

    public List<Photo> getAllPhotos() {
        return mRealm.where(Photo.class).findAll();
    }

    public void addPhoto(final Photo photo) {
        mRealm.executeTransaction(realm -> realm.copyToRealm(photo));
    }

    public void addAllPhotos(List<Photo> photos) {
        mRealm.executeTransaction(realm -> realm.copyToRealm(photos));
    }

    public void removeAllPhotos() {
        mRealm.executeTransaction(realm -> realm.deleteAll());
    }
}
