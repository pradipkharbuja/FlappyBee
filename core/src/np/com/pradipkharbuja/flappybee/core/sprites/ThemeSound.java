package np.com.pradipkharbuja.flappybee.core.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import np.com.pradipkharbuja.flappybee.core.FlappyBee;
import np.com.pradipkharbuja.flappybee.core.misc.Constants;

/**
 * Created by Pradip Kharbuja on 12/28/2015.
 */
public class ThemeSound {

    private Sprite sprite;
    private boolean selected;

    private Preferences preferences;
    public static final String KEY = "themeSound";

    public ThemeSound(float x, float y) {
        preferences = Gdx.app.getPreferences(Constants.PREFS_NAME);

        sprite = new Sprite(new Texture(this.getTextureImage()));
        sprite.setX(x);
        sprite.setY(y);
    }

    private boolean isSelected() {
        return preferences.getBoolean(KEY);
    }

    private String getTextureImage() {
        if (this.isSelected()) {
            return "sound_on.png";
        } else {
            return "sound_off.png";
        }
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite() {
        sprite.setTexture(new Texture(this.getTextureImage()));
    }

    public void play() {
        if (this.isSelected()) {
            if (FlappyBee.music.isPlaying()) {
                FlappyBee.music.stop();
            }
            FlappyBee.music.play();
        } else {
            FlappyBee.music.stop();
        }
    }

    public void stop() {
        FlappyBee.music.stop();
    }

    public void toggle() {
        selected = !this.isSelected();
        preferences.putBoolean(KEY, selected);
        preferences.flush();

        this.setSprite();
        this.play();
    }

    public void dispose() {
        sprite.getTexture().dispose();
    }
}
