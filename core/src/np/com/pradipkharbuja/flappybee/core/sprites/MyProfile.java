package np.com.pradipkharbuja.flappybee.core.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by Pradip Kharbuja on 12/29/2015.
 */
public class MyProfile {

    private Sprite sprite;

    public MyProfile(float x, float y){
        sprite = new Sprite(new Texture("my_profile.png"));
        sprite.setX(x - sprite.getWidth() / 2);
        sprite.setY(y);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void dispose(){
        sprite.getTexture().dispose();
    }
}
