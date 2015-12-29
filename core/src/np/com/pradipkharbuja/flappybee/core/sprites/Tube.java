package np.com.pradipkharbuja.flappybee.core.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

/**
 * Created by Pradip Kharbuja on 12/24/2015.
 */
public class Tube {

    public static final int TUBE_WIDTH = 87;

    private static final int FLUCTUATION = 100;
    private static final int LOWEST_OPENING = 250;

    private Texture topTube, bottomTube;
    private Vector2 posTopTube, posBotTube;

    private Random random;

    private Rectangle boundsTop, boundsBot;

    private int tubeGap;

    public Tube(float x) {
        topTube = new Texture("toptube.png");
        bottomTube = new Texture("bottomtube.png");

        random = new Random();
        tubeGap = this.getTubeGap();

        posTopTube = new Vector2(x, random.nextInt(FLUCTUATION) + tubeGap + LOWEST_OPENING);
        posBotTube = new Vector2(x, posTopTube.y - tubeGap - bottomTube.getHeight());

        boundsTop = new Rectangle(posTopTube.x, posTopTube.y, topTube.getWidth(), topTube.getHeight());
        boundsBot = new Rectangle(posBotTube.x, posBotTube.y, bottomTube.getWidth(), bottomTube.getHeight());
    }

    public Texture getTopTube() {
        return topTube;
    }

    public Texture getBottomTube() {
        return bottomTube;
    }

    public Vector2 getPosTopTube() {
        return posTopTube;
    }

    public Vector2 getPosBotTube() {
        return posBotTube;
    }

    public void reposition(float x) {
        posTopTube = new Vector2(x, random.nextInt(FLUCTUATION) + tubeGap + LOWEST_OPENING);
        posBotTube = new Vector2(x, posTopTube.y - tubeGap - bottomTube.getHeight());

        boundsTop.setPosition(posTopTube.x, posTopTube.y);
        boundsBot.setPosition(posBotTube.x, posBotTube.y);
    }

    public boolean collides(Rectangle player) {
        return player.overlaps(boundsTop) || player.overlaps(boundsBot);
    }

    public void dispose() {
        topTube.dispose();
        bottomTube.dispose();
    }

    public int getTubeGap() {
        return random.nextInt(50) + 100;
    }
}
