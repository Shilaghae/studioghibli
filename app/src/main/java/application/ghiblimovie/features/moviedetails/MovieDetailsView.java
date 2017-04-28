package application.ghiblimovie.features.moviedetails;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Map;
import java.util.Set;

import application.ghiblimovie.R;

/**
 * @author anna
 */

public class MovieDetailsView extends LinearLayout {

    private final Context mContext;

    public MovieDetailsView(final Context context) {
        this(context, null);
    }

    public MovieDetailsView(final Context context, @Nullable final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MovieDetailsView(final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

    }

    public void addViews(final Map<String, String> movieDetails) {
        removeAllViews();
        Set<String> keySet = movieDetails.keySet();
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        for (String key : keySet) {
            String value = movieDetails.get(key);
            View view = layoutInflater.inflate(R.layout.movie_detail_item, this, false);
            TextView mkey = (TextView) view.findViewById(R.id.key);
            mkey.setText(key);
            TextView mvalue = (TextView) view.findViewById(R.id.value);
            mvalue.setText(value);
            addView(view);
        }
    }
}
