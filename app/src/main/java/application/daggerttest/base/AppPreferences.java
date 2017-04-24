package application.daggerttest.base;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author anna
 */

public class AppPreferences {
    private SharedPreferences mSharedPreferences;

    public AppPreferences(Context context) {
        mSharedPreferences = context.getSharedPreferences(".dagger_test", Context.MODE_PRIVATE);
        mSharedPreferences.edit().putString("init", "initialization").apply();
    }

    public String getInitSetup() {
        return mSharedPreferences.getString("init", "none");
    }
}
