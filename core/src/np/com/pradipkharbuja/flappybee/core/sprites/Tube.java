package np.com.pradipkharbuja.flappybee.core.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

import np.com.pradipkharbuja.flappybee.core.service.CallOutLocation;

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

    private boolean topTubeTouched;

    private CallOutLocation callOutLocation;
    private Rectangle intersectedRectangle;

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

    public boolean collides(Bird player) {
        Rectangle rectangle = new Rectangle();
        Rectangle tubeRectangle = new Rectangle();

        boolean overlapped = false;

        if (Intersector.intersectRectangles(player.getBounds(), boundsTop, rectangle)) {
            this.topTubeTouched = true;
            overlapped = true;
            tubeRectangle = boundsTop;
        }

        if (Intersector.intersectRectangles(player.getBounds(), boundsBot, rectangle)) {
            overlapped = true;
            this.topTubeTouched = false;
            tubeRectangle = boundsBot;
        }

        if (overlapped) {
            if (rectangle.getWidth() > 6 && rectangle.getHeight() > 6) {
                if (rectangle.getHeight() + 10 >= player.getBounds().getHeight()) {
                    this.callOutLocation = CallOutLocation.LEFT;
                    System.out.println("Completely Left");
                } else if (rectangle.getWidth() + 10 >= player.getBounds().getWidth()) {
                    this.callOutLocation = CallOutLocation.TOP;
                    System.out.println("Completely Top");
                } else if (rectangle.getX() == player.getBounds().getX()) {
                    this.callOutLocation = CallOutLocation.TOP_RIGHT;
                    System.out.println("Top Right");
                } else {
                    this.callOutLocation = CallOutLocation.TOP_LEFT;
                    System.out.println("Top Left");
                }

                System.out.println("Player = " + player.getBounds() + "   " + boundsBot);
                System.out.println("Rectangle: " + rectangle);

                this.intersectedRectangle = rectangle;
                return true;
            }
        }

        return false;
    }

    public void dispose() {
        topTube.dispose();
        bottomTube.dispose();
    }

    public int getTubeGap() {
        return random.nextInt(50) + 100;
    }

    public boolean isTopTubeTouched() {
        return topTubeTouched;
    }

    public CallOutLocation getCallOutLocation() {
        return callOutLocation;
    }

    public void setCallOutLocation(CallOutLocation callOutLocation) {
        this.callOutLocation = callOutLocation;
    }

    public Rectangle getIntersectedRectangle() {
        return intersectedRectangle;
    }

    public void setIntersectedRectangle(Rectangle intersectedRectangle) {
        this.intersectedRectangle = intersectedRectangle;
    }
}
