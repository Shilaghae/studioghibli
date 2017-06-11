package application.ghiblimovie.features.photodetails;

import com.jakewharton.rxbinding2.view.RxView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import javax.inject.Inject;

import application.ghiblimovie.R;
import application.ghiblimovie.base.BaseActivity;
import application.ghiblimovie.base.BasePresenter;
import application.ghiblimovie.features.photodetails.PhotoDetailsContractor.PhotoDetailsView;
import application.ghiblimovie.features.photohome.PhotoItem;
import butterknife.BindView;
import io.reactivex.Observable;

public class PhotoDetailsActivity extends BaseActivity<PhotoDetailsView> implements PhotoDetailsView {

    @Inject
    PhotoDetailsPresenterImpl presenter;
    @BindView(R.id.photodetails_activity_button_getlocation)
    Button getLocationButton;
    @BindView(R.id.photodetails_activity_toolbar_layout) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.photodetails_activity_background_image) SquareImageView squareImageView;
    private static final String PHOTO_ITEM = "PHOTO_ITEM";

    @Override public int getLayoutId() {
        return R.layout.photodetails_activity;
    }

    @Override public PhotoDetailsView getView() {
        return this;
    }

    @Override public BasePresenter<PhotoDetailsView> getPresenter() {
        return presenter;
    }

    @Override public void inject() {
        getAppComponent().getPhotoDetailsComponent(new PhotoDetailsModule()).inject(this);
    }

    @Override public void init() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.photodetails_activity_toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
        squareImageView.setImageBitmap(bitmap);

        collapsingToolbarLayout.setTitle("Title");
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
    }

    @Override public Observable<Object> onClickAddDetails() {
        return RxView.clicks(getLocationButton);
    }

    private Bitmap bitmap;

    @Override protected void onCreate(final Bundle savedInstanceState) {
        final String path = getIntent().getStringExtra(PHOTO_ITEM);
        bitmap = BitmapFactory.decodeFile(path);
        super.onCreate(savedInstanceState);
    }

    public static void startActivity(final Context context, final PhotoItem photoItem) {
        final Intent intent = new Intent(context, PhotoDetailsActivity.class);
        intent.putExtra(PHOTO_ITEM, photoItem.getPath());
        context.startActivity(intent);
    }
}
