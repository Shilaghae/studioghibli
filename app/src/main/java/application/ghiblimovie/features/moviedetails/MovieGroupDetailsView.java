package application.ghiblimovie.features.moviedetails;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import java.util.Map;
import java.util.Set;

/**
 * @author anna
 */

public class MovieGroupDetailsView extends LinearLayout {

    private final Context mContext;

    public MovieGroupDetailsView(final Context context) {
        this(context, null);
    }

    public MovieGroupDetailsView(final Context context, @Nullable final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MovieGroupDetailsView(final Context context, @Nullable final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public void addMovieDetailsViews(final Map<String, String> details) {
        Set<String> keySet = details.keySet();
        for (String key : keySet) {
            String value = details.get(key);
            if (value != null && !value.isEmpty()) {
                MovieItemDetailsView md = new MovieItemDetailsView(mContext);
                md.addViewWithDetails(key, details.get(key));
                addView(md);
            }
        }
    }
}
