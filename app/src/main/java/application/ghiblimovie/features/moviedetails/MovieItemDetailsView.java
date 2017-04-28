package application.ghiblimovie.features.moviedetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import application.ghiblimovie.R;

/**
 * @author anna
 */

public class MovieItemDetailsView extends LinearLayout {

    private final Context mContext;
    private View mView;

    public MovieItemDetailsView(final Context context) {
        super(context);
        mContext = context;
    }

    public void addViewWithDetails(final String key, final String value) {
        final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        mView = layoutInflater.inflate(R.layout.movie_detail_view_item, this, false);
        final TextView keyTextView = (TextView) mView.findViewById(R.id.movie_detail_view_key);
        keyTextView.setText(key);
        final TextView valueTextView = (TextView) mView.findViewById(R.id.movie_detail_view_value);
        valueTextView.setText(value);
        addView(mView);
    }
}
