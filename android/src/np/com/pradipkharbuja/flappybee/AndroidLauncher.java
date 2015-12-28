package np.com.pradipkharbuja.flappybee;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import np.com.pradipkharbuja.flappybee.core.FlappyBee;


public class AndroidLauncher extends AndroidApplication {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create the layout
        RelativeLayout layout = new RelativeLayout(this);

        // Do the stuff that initialize() would do for you
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        // Create the libgdx View
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        View gameView = initializeForView(new FlappyBee(), config);

        // Create and setup the AdMob view
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);

        // Test Ads
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");

        //My Ad
        //adView.setAdUnitId("ca-app-pub-3539844738735929/7725607136");


        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        // Add the libgdx view
        layout.addView(gameView);

        // Add the AdMob view
        layout.addView(adView);

        setContentView(layout);
    }
}
