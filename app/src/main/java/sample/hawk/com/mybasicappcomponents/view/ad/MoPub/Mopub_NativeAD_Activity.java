package sample.hawk.com.mybasicappcomponents.view.ad.MoPub;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.webkit.WebView;

import com.mopub.common.MoPub;
import com.mopub.common.util.DeviceUtils;

import java.util.ArrayList;
import java.util.List;

import sample.hawk.com.mybasicappcomponents.R;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.mopub.common.Constants.UNUSED_REQUEST_CODE;

/**
 * refer to Mopub SDK NativeListViewFragment.java
 */

public class Mopub_NativeAD_Activity extends FragmentActivity {

    private static final List<String> REQUIRED_DANGEROUS_PERMISSIONS = new ArrayList<String>();
    static {
        REQUIRED_DANGEROUS_PERMISSIONS.add(ACCESS_COARSE_LOCATION);
        REQUIRED_DANGEROUS_PERMISSIONS.add(WRITE_EXTERNAL_STORAGE);
    }

    // Sample app web views are debuggable.
    static {
        setWebDebugging();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void setWebDebugging() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mopub_activity);

        if (savedInstanceState != null) {
            return;
        }

        List<String> permissionsToBeRequested = new ArrayList<String>();
        for (String permission : REQUIRED_DANGEROUS_PERMISSIONS) {
            if (!DeviceUtils.isPermissionGranted(this, permission)) {
                permissionsToBeRequested.add(permission);
            }
        }

        // Request dangerous permissions
        if (!permissionsToBeRequested.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToBeRequested.toArray(
                    new String[permissionsToBeRequested.size()]), UNUSED_REQUEST_CODE);
        }

        // Set location awareness and precision globally for your app:
        MoPub.setLocationAwareness(MoPub.LocationAwareness.TRUNCATED);
        MoPub.setLocationPrecision(4);
        final String ad_unit_id_native = "11a17b188668469fb0412708c3d16813";
        if (findViewById(R.id.fragment_container) != null) {
            // Copy from AdUnitDataSource.jave:LINE136
            final MoPubSampleAdUnit adConfiguration = new MoPubSampleAdUnit
                    .Builder(ad_unit_id_native, MoPubSampleAdUnit.AdType.LIST_VIEW)
                    .description("MoPub Ad Placer Sample")
                    .build();
            final NativeListViewFragment listFragment = new NativeListViewFragment();
            listFragment.setArguments(adConfiguration.toBundle());
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, listFragment)
                    .commit();
        }

        // Intercepts all logs including Level.FINEST so we can show a toast
        // that is not normally user-facing. This is only used for native ads.
        LoggingUtils.enableCanaryLogging(this);
    }
}
