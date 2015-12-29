package np.com.pradipkharbuja.flappybee.core.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Pradip Kharbuja on 12/24/2015.
 */
public class Bird {

    private static final int GRAVITY = -15;
    private static final int MOVEMENT = 100;

    private Vector3 position;
    private Vector3 velocity;

    private Rectangle bounds;
    private Animation birdAnimation;
    private Texture bird;

    private static final int FRAME_COUNT = 3;

    private Sound flap;
    private GameSound gameSound;

    private boolean blnDead;

    public Bird(GameSound gameSound, int x, int y) {
        this.gameSound = gameSound;

        position = new Vector3(x, y, 0);
        velocity = new Vector3(0, 0, 0);

        bird = new Texture("birdanimation.png");
        birdAnimation = new Animation(new TextureRegion(bird), FRAME_COUNT, 0.5f);

        bounds = new Rectangle(x, y, bird.getWidth() / FRAME_COUNT, bird.getHeight());
        flap = Gdx.audio.newSound(Gdx.files.internal("wing.mp3"));
    }

    public void fly(float dt) {
        if (!blnDead) {
            birdAnimation.update(dt);
        }
    }

    public void update(float dt) {
        if (blnDead) {
            return;
        }

        if (position.y > 0) {
            velocity.add(0, GRAVITY, 0);
        }
        velocity.scl(dt); //multiple everything by delta time
        position.add(MOVEMENT * dt, velocity.y, 0);

        if (position.y < 0) {
            position.y = 0;
        }

        velocity.scl(1 / dt);
        bounds.setPosition(position.x, position.y);
    }

    public Vector3 getPosition() {
        return position;
    }

    public TextureRegion getTexture() {
        return birdAnimation.getFrame();
    }

    public void jump() {
        velocity.y = 250;
        if (gameSound.isSelected()) {
            flap.play();
        }
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void dispose() {
        bird.dispose();
        flap.dispose();
    }

    public void dead() {
        this.blnDead = true;
    }
}
