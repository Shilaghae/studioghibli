package application.ghiblimovie.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * @author anna
 */

public class NetworkStatusChangeReceiver extends BroadcastReceiver {

    private PublishSubject<Boolean> mOnNetworkStatusChangePublish = PublishSubject.create();

    public Observable<Boolean> onNetworkChangeStatus() {
        return mOnNetworkStatusChangePublish;
    }

    public void onReceive(final Context context, final Intent intent) {
        if (intent.getExtras() != null) {
            NetworkInfo networkInfo = (NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (networkInfo != null && networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                mOnNetworkStatusChangePublish.onNext(true);
            } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
                mOnNetworkStatusChangePublish.onNext(false);
            }
        }
    }
}
