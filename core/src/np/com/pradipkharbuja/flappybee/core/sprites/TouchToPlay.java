package np.com.pradipkharbuja.flappybee.core.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by Pradip Kharbuja on 12/29/2015.
 */
public class TouchToPlay {

    private Sprite sprite;

    public TouchToPlay(float x, float y) {
        sprite = new Sprite(new Texture("touch.png"));
        sprite.setX(x - getSprite().getWidth() / 2);
        sprite.setY(y);
    }

    public void show() {

    }

    public void hide() {

    }

    public Sprite getSprite() {
        return sprite;
    }
}
