package np.com.pradipkharbuja.flappybee.core.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import np.com.pradipkharbuja.flappybee.core.misc.Constants;

/**
 * Created by Pradip Kharbuja on 12/28/2015.
 */
public class HighScore {

    private static Preferences preferences = Gdx.app.getPreferences(Constants.PREFS_NAME);

    public static int get() {
        return preferences.getInteger("highScore", 0);
    }

    public static void save(int highScore) {
        preferences.putInteger("highScore", highScore);
        preferences.flush();
    }
}
