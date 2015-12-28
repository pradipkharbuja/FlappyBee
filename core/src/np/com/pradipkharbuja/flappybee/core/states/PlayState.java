package np.com.pradipkharbuja.flappybee.core.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import np.com.pradipkharbuja.flappybee.core.FlappyBee;
import np.com.pradipkharbuja.flappybee.core.service.HighScore;
import np.com.pradipkharbuja.flappybee.core.sprites.Bird;
import np.com.pradipkharbuja.flappybee.core.sprites.Score;
import np.com.pradipkharbuja.flappybee.core.sprites.Tube;

/**
 * Created by Pradip Kharbuja on 12/23/2015.
 */
public class PlayState extends State implements InputProcessor {

    private static final int TUBE_SPACING = 150;
    private static final int TUBE_COUNT = 4;
    private static final int textureGround_Y_OFFSET = -50;
    private static final int START_TIME = 100;

    private Bird bird;
    private Texture textureBackground;
    private Texture textureGround;
    private Texture textureScoreBoard;

    private Score score;

    private Vector2 textureGroundPos1, textureGroundPos2;

    private Array<Tube> tubes;
    private Music sound;

    private boolean isPlaying;

    private static int point = 0;
    private int delay = 0;
    private int delayOnStart = 0;

    @Override
    public boolean scrolled(int amount) {
        return super.scrolled(amount);
    }

    public PlayState(GameStateManager gsm) {
        super(gsm);
        bird = new Bird(50, 300);
        cam.setToOrtho(false, FlappyBee.WIDTH, FlappyBee.HEIGHT);

        textureBackground = new Texture("background.png");
        textureGround = new Texture("ground.png");
        textureScoreBoard = new Texture("score_board.png");

        textureGroundPos1 = new Vector2(cam.position.x - cam.viewportWidth / 2 - 100, textureGround_Y_OFFSET);
        textureGroundPos2 = new Vector2((cam.position.x - cam.viewportWidth / 2) + textureGround.getWidth() - 100, textureGround_Y_OFFSET);

        tubes = new Array<Tube>();

        for (int i = 1; i <= TUBE_COUNT; i++) {
            tubes.add(new Tube(i * (TUBE_SPACING + Tube.TUBE_WIDTH)));
        }

        score = new Score(cam.position.x, 20);

        sound = Gdx.audio.newMusic(Gdx.files.internal("start.ogg"));

        Gdx.input.setInputProcessor(this);
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched()) {

         /*
            if (!isPlaying) {
                isPlaying = true;
                point = 0;
            } else if (delayOnStart >= START_TIME) {
                bird.jump();
            } */
        }
    }

    @Override
    public void update(float dt) {
        handleInput();

        if (!isPlaying) {
            return;
        }

        if (delayOnStart < START_TIME) {
            return;
        }

        bird.fly(dt);
        bird.update(dt);

        // The default position of camera is 210 and bird is 50
        cam.position.x = bird.getPosition().x + 160;

        updateTextureGround();

        for (int i = 0; i < tubes.size; i++) {
            Tube tube = tubes.get(i);

            if (cam.position.x - (cam.viewportWidth / 2) > tube.getPosTopTube().x + tube.getTopTube().getWidth()) {
                tube.reposition(tube.getPosTopTube().x + ((Tube.TUBE_WIDTH + TUBE_SPACING) * TUBE_COUNT));
            }

            if (tube.collides(bird.getBounds())) {
                this.gameOver();
            }
        }

        if (bird.getPosition().y <= textureGround.getHeight() + textureGround_Y_OFFSET) {
            this.gameOver();
        }

        score.reposition(cam.position.x, 10);

        cam.update();

        if (delay++ > 10) {
            point++;
            delay = 0;
        }
    }

    @Override
    public void render(SpriteBatch sb) {

        sb.setProjectionMatrix(cam.combined);

        sb.begin();

        sb.draw(textureBackground, cam.position.x - (cam.viewportWidth / 2), 0);

        sb.draw(bird.getTexture(), bird.getPosition().x, bird.getPosition().y);

        for (Tube tube : tubes) {
            sb.draw(tube.getTopTube(), tube.getPosTopTube().x, tube.getPosTopTube().y);
            sb.draw(tube.getBottomTube(), tube.getPosBotTube().x, tube.getPosBotTube().y);
        }

        sb.draw(textureGround, textureGroundPos1.x, textureGroundPos1.y);
        sb.draw(textureGround, textureGroundPos2.x, textureGroundPos2.y);

        sb.draw(score.getTextureScore(), score.getPosition().x, score.getPosition().y);
        this.displayScore(point, sb, cam.position.x + 170, 20);

        if (!isPlaying) {
            sb.draw(textureScoreBoard, cam.position.x - textureScoreBoard.getWidth() / 2, cam.position.y - 100);

            //High Score
            int highScore = HighScore.get();
            this.displayScore(highScore, sb, cam.position.x + 120, cam.position.y - 25);

            //Score
            this.displayScore(point, sb, cam.position.x + 120, cam.position.y - 75);

        } else if (delayOnStart < START_TIME) {

            delayOnStart++;
            float x = cam.position.x, y = cam.position.y;

            if (delayOnStart < 30) {
                sb.draw(new Texture("score/3.png"), x, y);
            } else if (delayOnStart < 70) {
                sb.draw(new Texture("score/2.png"), x, y);
            } else if (delayOnStart < START_TIME) {
                sb.draw(new Texture("score/1.png"), x, y);
            }
        }
        sb.end();
    }

    @Override
    public void dispose() {

        bird.dispose();

        textureBackground.dispose();
        textureGround.dispose();
        textureScoreBoard.dispose();

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

    private void updateTextureGround() {

        if (cam.position.x - (cam.viewportWidth / 2) > textureGroundPos1.x + textureGround.getWidth()) {
            textureGroundPos1.add(textureGround.getWidth() * 2, 0);
        }

        if (cam.position.x - (cam.viewportWidth / 2) > textureGroundPos2.x + textureGround.getWidth()) {
            textureGroundPos2.add(textureGround.getWidth() * 2, 0);
        }
    }

    private void gameOver() {
        sound.play();

        if (HighScore.get() < point) {
            HighScore.save(point);
        }

        gsm.set(new PlayState(gsm));
    }

    public void displayScore(int score, SpriteBatch spriteBatch, float x, float y) {
        int tempScore = score;
        int i = 0;
        while (tempScore > 0) {
            int temp = tempScore % 10;
            i++;
            Texture texture = new Texture("score/" + temp + ".png");

            spriteBatch.draw(texture, x - i * (20), y);
            tempScore = tempScore / 10;

        }

        if (score == 0) {
            Texture texture = new Texture("score/0.png");
            spriteBatch.draw(texture, x - 20, y);
        }
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
System.out.println(textureGround.getTextureData());
        System.out.println(screenX + "  " + screenY);
        return super.touchUp(screenX, screenY, pointer, button);
    }
}
