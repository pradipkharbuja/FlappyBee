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
public class GameSound {

    private Sprite sprite;
    private boolean selected;

    private Preferences preferences;
    public static final String KEY = "gameSound";

    public GameSound(float x, float y) {
        preferences = Gdx.app.getPreferences(Constants.PREFS_NAME);

        sprite = new Sprite(new Texture(this.getTextureImage()));
        sprite.setX(x);
        sprite.setY(y);
    }

    public boolean isSelected() {
        return preferences.getBoolean(KEY);
    }

    private String getTextureImage() {
        if (this.isSelected()) {
            return "game_on.png";
        } else {
            return "game_off.png";
        }
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite() {
        sprite.setTexture(new Texture(this.getTextureImage()));
    }

    public void toggle() {
        selected = !this.isSelected();
        preferences.putBoolean(KEY, selected);
        preferences.flush();

        this.setSprite();
    }
}
