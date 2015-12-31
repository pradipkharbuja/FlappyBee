package np.com.pradipkharbuja.flappybee.core.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by Pradip Kharbuja on 12/29/2015.
 */
public class PlayButton {

    private Sprite sprite;

    public PlayButton(float x, float y) {
        sprite = new Sprite(new Texture("play_button.png"));
        sprite.setX(x);
        sprite.setY(y);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }
}
