package application.ghiblimovie.features.photohome;

import android.content.Context;
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
    private final float mSquare;

    public PhotoAdapter(List<Photo> photos, float square) {
        mPhotos = photos;
        mSquare = square;
    }

    private float dp;

    @Override
    public PhotoHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.photo_item, parent, false);
        return new PhotoHolder(view);
    }

    @Override
    public void onBindViewHolder(final PhotoHolder holder, final int position) {
        final Photo photo = mPhotos.get(position);
        final String photoPath = photo.getPhotoPath();
        Bitmap thumbnail = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(photoPath), (int) mSquare, (int) mSquare);
        holder.setPhotoImage(thumbnail);
    }

    @Override
    public int getItemCount() {
        return mPhotos.size();
    }

    public void addPhoto(final Photo photo) {
        mPhotos.add(photo);
        notifyItemChanged(mPhotos.size() - 1);
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
