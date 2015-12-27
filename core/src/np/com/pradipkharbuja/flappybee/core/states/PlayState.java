package np.com.pradipkharbuja.flappybee.core.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import np.com.pradipkharbuja.flappybee.core.FlappyBee;
import np.com.pradipkharbuja.flappybee.core.sprites.Tube;
import np.com.pradipkharbuja.flappybee.core.sprites.Bird;
import np.com.pradipkharbuja.flappybee.core.sprites.Score;

/**
 * Created by Pradip Kharbuja on 12/23/2015.
 */
public class PlayState extends State {

    private static final int TUBE_SPACING = 150;
    private static final int TUBE_COUNT = 4;
    private static final int GROUND_Y_OFFSET = -50;

    private Bird bird;
    private Texture bg;
    private Texture ground;

    private Score score;

    private Vector2 groundPos1, groundPos2;

    private Array<Tube> tubes;
    private Music sound;

    private boolean isPlaying;
    private static  int point = 1;
    private int delay = 0;

    @Override
    public boolean scrolled(int amount) {
        return super.scrolled(amount);
    }

    public PlayState(GameStateManager gsm) {
        super(gsm);
        bird = new Bird(50, 300);
        cam.setToOrtho(false, FlappyBee.WIDTH, FlappyBee.HEIGHT);
        bg = new Texture("bg.png");
        ground = new Texture("ground.png");

        groundPos1 = new Vector2(cam.position.x - cam.viewportWidth / 2 - 100, GROUND_Y_OFFSET);
        groundPos2 = new Vector2((cam.position.x - cam.viewportWidth / 2) + ground.getWidth() - 100, GROUND_Y_OFFSET);

        tubes = new Array<Tube>();

        for (int i = 1; i <= TUBE_COUNT; i++) {
            tubes.add(new Tube(i * (TUBE_SPACING + Tube.TUBE_WIDTH)));
        }

        score = new Score(cam.position.x, 20);

        sound = Gdx.audio.newMusic(Gdx.files.internal("start.ogg"));
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched()) {

            if (!isPlaying) {
                isPlaying = true;
                point = 1;
            }

            bird.jump();
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        bird.fly(dt);

        if (!isPlaying) {
            return;
        }
        bird.update(dt);

        // The default position of camera is 210 and bird is 50
        cam.position.x = bird.getPosition().x + 160;

        updateGround();

        for (int i = 0; i < tubes.size; i++) {
            Tube tube = tubes.get(i);

            if (cam.position.x - (cam.viewportWidth / 2) > tube.getPosTopTube().x + tube.getTopTube().getWidth()) {
                tube.reposition(tube.getPosTopTube().x + ((Tube.TUBE_WIDTH + TUBE_SPACING) * TUBE_COUNT));
            }

            if (tube.collides(bird.getBounds())) {
                this.gameOver();
            }
        }

        if (bird.getPosition().y <= ground.getHeight() + GROUND_Y_OFFSET) {
            this.gameOver();
        }

        score.reposition(cam.position.x, 10);

        cam.update();

        if(delay++ > 10) {
            point++;
            delay = 0;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(bg, cam.position.x - (cam.viewportWidth / 2), 0);
        sb.draw(bird.getTexture(), bird.getPosition().x, bird.getPosition().y);

        for (Tube tube : tubes) {
            sb.draw(tube.getTopTube(), tube.getPosTopTube().x, tube.getPosTopTube().y);
            sb.draw(tube.getBottomTube(), tube.getPosBotTube().x, tube.getPosBotTube().y);
        }

        sb.draw(ground, groundPos1.x, groundPos1.y);
        sb.draw(ground, groundPos2.x, groundPos2.y);

        sb.draw(score.getTextureScore(), score.getPosition().x, score.getPosition().y);
        this.displayScore(sb, cam);

        sb.end();
    }

    @Override
    public void dispose() {
        bg.dispose();
        bird.dispose();
        ground.dispose();

        for (Tube tube : tubes) {
            tube.dispose();
        }

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    System.out.println("PlayState disposed");
                    sound.dispose();
                } catch (Exception ex) {

                }
            }
        });
        t.start();
    }

    private void updateGround() {

        if (cam.position.x - (cam.viewportWidth / 2) > groundPos1.x + ground.getWidth()) {
            groundPos1.add(ground.getWidth() * 2, 0);
        }

        if (cam.position.x - (cam.viewportWidth / 2) > groundPos2.x + ground.getWidth()) {
            groundPos2.add(ground.getWidth() * 2, 0);
        }
    }

    private void gameOver() {
        sound.play();
        gsm.set(new PlayState(gsm));
    }

    public void displayScore(SpriteBatch spriteBatch, Camera camera) {
        int tempScore = point;
        int i = 0;
        while (tempScore > 0) {
            int temp = tempScore % 10;
            i++;
            Texture texture = new Texture("score/" + temp + ".png");

            spriteBatch.draw(texture, camera.position.x + 180 - i * (20), 20);
            tempScore = tempScore / 10;

        }
    }
}
