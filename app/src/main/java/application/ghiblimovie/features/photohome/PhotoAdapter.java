package application.ghiblimovie.features.photohome;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import application.ghiblimovie.R;
import application.ghiblimovie.photorepository.Photo;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author anna
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoHolder> {

    private final List<Photo> mPhotos;

    public PhotoAdapter(List<Photo> photos) {
        mPhotos = photos;
    }

    @Override
    public PhotoHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_item, parent, false);
        return new PhotoHolder(view);
    }

    @Override
    public void onBindViewHolder(final PhotoHolder holder, final int position) {
        final Photo photo = mPhotos.get(position);
        final String photoPath = photo.getPhotoPath();
        Bitmap thumbnail = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(photoPath), 100, 100);
        holder.setPhotoImage(thumbnail);
    }

    @Override
    public int getItemCount() {
        return mPhotos.size();
    }

    public void addPhoto(final Photo photo) {
        mPhotos.add(photo);
        notifyItemChanged(mPhotos.size()-1);
    }

    public void addPhotos(final List<Photo> photos) {
        mPhotos.addAll(photos);
        notifyDataSetChanged();
    }

    class PhotoHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.photo_item_imageView_photo)
        ImageView mPhotoImageView;

        public PhotoHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setPhotoImage(Bitmap photoBitmat) {
            mPhotoImageView.setImageBitmap(photoBitmat);
        }
    }
}
