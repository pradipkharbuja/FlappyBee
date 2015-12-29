package np.com.pradipkharbuja.flappybee.core.misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import np.com.pradipkharbuja.flappybee.core.sprites.GameSound;
import np.com.pradipkharbuja.flappybee.core.sprites.ThemeSound;

/**
 * Created by Pradip Kharbuja on 12/29/2015.
 */
public class Project {

    private Preferences preferences;
    private final static String KEY = "firstRun";

    public Project() {
        preferences = Gdx.app.getPreferences(Constants.PREFS_NAME);

        if (!preferences.getBoolean(KEY, false)) {
            preferences.putBoolean(GameSound.KEY, true);
            preferences.putBoolean(ThemeSound.KEY, true);
            preferences.putBoolean(KEY, true);
            preferences.flush();
        }
    }

}
