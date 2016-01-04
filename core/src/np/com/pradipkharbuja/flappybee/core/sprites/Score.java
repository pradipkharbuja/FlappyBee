package np.com.pradipkharbuja.flappybee.core.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Pradip Kharbuja on 12/26/2015.
 */
public class Score {

    private Texture textureScore;
    private Vector2 position;
    private float negativeX = 180;

    public Score(float x, float y) {
        textureScore = new Texture("score.png");
        position = new Vector2(x - negativeX, y);
    }

    public Texture getTextureScore() {
        return textureScore;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void reposition(float x, int score) {
        position = new Vector2(x - negativeX, position.y);
    }

    public void dispose(){
        textureScore.dispose();
    }
}
