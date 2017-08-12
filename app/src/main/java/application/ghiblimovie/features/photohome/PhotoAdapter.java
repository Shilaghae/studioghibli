package application.ghiblimovie.features.photohome;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
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

    private final List<Photo> mPhotos = new ArrayList<>();
    private final Context context;
    private final float width;
    private PublishSubject<Photo> onClickPhotoUtemPublishSubject = PublishSubject.create();
    private Map<String, Bitmap> map = new HashMap<>();

    public PhotoAdapter(Context context, float width) {

        this.context = context;
        this.width = width;
    }

    @Override
    public PhotoHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.photo_item, parent, false);
        return new PhotoHolder(view);
    }

    @Override
    public void onBindViewHolder(final PhotoHolder holder, final int position) {
        final Photo photoItem = mPhotos.get(position);
        holder.setPhotoItem(photoItem);
    }

    @Override
    public int getItemCount() {
        return mPhotos.size();
    }

    void addPhoto(final Photo photo, final Bitmap bitmap) {
        mPhotos.add(photo);
        map.put(photo.getAbsolutePath(), bitmap);
        notifyItemChanged(mPhotos.size() - 1);
    }

    void addPhotos(final List<Photo> photos) {
        mPhotos.addAll(photos);
        notifyDataSetChanged();
    }

    class PhotoHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.photo_item_imageView_photo)
        ImageView mPhotoImageView;

        PhotoHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setPhotoItem(Photo photoItem) {
            mPhotoImageView.setImageBitmap(map.get(photoItem.getAbsolutePath()));
            itemView.setOnClickListener(v -> onClickPhotoUtemPublishSubject.onNext(photoItem));
        }
    }

    Observable<Photo> onClickItem() {
        return onClickPhotoUtemPublishSubject;
    }

}
