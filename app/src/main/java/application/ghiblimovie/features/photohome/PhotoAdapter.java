package application.ghiblimovie.features.photohome;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import application.ghiblimovie.R;
import application.ghiblimovie.photorepository.Photo;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoHolder> {

    private final List<Photo> photos = new ArrayList<>();
    private PublishSubject<Photo> onClickPhotoUtemPublishSubject = PublishSubject.create();
    private Map<String, Bitmap> bitmapMap = new HashMap<>();

    @Override
    public PhotoHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.photo_item, parent, false);
        return new PhotoHolder(view);
    }

    @Override
    public void onBindViewHolder(final PhotoHolder holder, final int position) {
        final Photo photoItem = photos.get(position);
        holder.setPhotoItem(photoItem);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    void addPhoto(final Photo photo, final Bitmap bitmap) {
        photos.add(photo);
        bitmapMap.put(photo.getAbsolutePath(), bitmap);
        notifyItemChanged(photos.size() - 1);
    }

    void addPhotos(final List<Photo> photos) {
        this.photos.addAll(photos);
        notifyDataSetChanged();
    }

    class PhotoHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.photo_item_imageView_photo)
        ImageView mPhotoImageView;

        PhotoHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setPhotoItem(final Photo photo) {
            mPhotoImageView.setImageBitmap(bitmapMap.get(photo.getAbsolutePath()));
            itemView.setOnClickListener(v -> onClickPhotoUtemPublishSubject.onNext(photo));
        }
    }

    Observable<Photo> onClickItem() {
        return onClickPhotoUtemPublishSubject;
    }

}
