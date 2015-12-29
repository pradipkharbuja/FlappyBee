package np.com.pradipkharbuja.flappybee.core.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import np.com.pradipkharbuja.flappybee.core.FlappyBee;
import np.com.pradipkharbuja.flappybee.core.service.HighScore;
import np.com.pradipkharbuja.flappybee.core.sprites.Bird;
import np.com.pradipkharbuja.flappybee.core.sprites.GameSound;
import np.com.pradipkharbuja.flappybee.core.sprites.Score;
import np.com.pradipkharbuja.flappybee.core.sprites.ThemeSound;
import np.com.pradipkharbuja.flappybee.core.sprites.TouchToPlay;
import np.com.pradipkharbuja.flappybee.core.sprites.Tube;

/**
 * Created by Pradip Kharbuja on 12/23/2015.
 */
public class PlayState extends State {

    private static final int TUBE_SPACING = 150;
    private static final int TUBE_COUNT = 4;
    private static final int textureGround_Y_OFFSET = -50;

    private Bird bird;
    private Texture textureBackground;
    private Texture textureGround;
    private Texture textureScoreBoard;

    private Score score;

    private Vector2 textureGroundPos1, textureGroundPos2;

    private Array<Tube> tubes;
    private Music sound;
    private ThemeSound themeSound;
    private GameSound gameSound;
    private TouchToPlay touchToPlay;

    private boolean isPlaying;

    private static int point = 0;
    private int delay = 0;

    private Sprite spritePlay;

    private boolean blnTouchToPlay = false;
    private boolean blnGameOver = false;

    private SpriteBatch spriteBatch;

    @Override
    public boolean scrolled(int amount) {
        return super.scrolled(amount);
    }

    public PlayState(GameStateManager gsm) {
        super(gsm);

        cam.setToOrtho(false, FlappyBee.WIDTH, FlappyBee.HEIGHT);

        gameSound = new GameSound(cam.position.x + 100, cam.position.y - 150);

        bird = new Bird(gameSound, 50, 300);

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

        spritePlay = new Sprite(new Texture("play_button.png"));

        themeSound = new ThemeSound(cam.position.x - 150, cam.position.y - 150);

        themeSound.play();

        touchToPlay = new TouchToPlay(cam.position.x, cam.position.y + 80);
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched()) {
            int x1 = Gdx.input.getX();
            int y1 = Gdx.input.getY();
            Vector3 input = new Vector3(x1, y1, 0);
            cam.unproject(input);

            if (!isPlaying && spritePlay.getBoundingRectangle().contains(input.x, input.y)) {
                isPlaying = true;
                point = 0;
                themeSound.play();
                System.out.println("Play clicked.");
            } else if (isPlaying && !blnTouchToPlay) {
                System.out.println("Touch to play");
                blnTouchToPlay = true;
            } else if (isPlaying && blnTouchToPlay) {
                bird.jump();
            }
            //Theme Sound
            else if (themeSound.getSprite().getBoundingRectangle().contains(input.x, input.y)) {
                System.out.println("Theme sound");
                themeSound.toggle();
            }

            //Game Sound
            else if (gameSound.getSprite().getBoundingRectangle().contains(input.x, input.y)) {
                System.out.println("GAme sound");
                gameSound.toggle();
            }
        }
    }

    @Override
    public void update(float dt) {
        handleInput();

        if (!isPlaying || blnGameOver) {
            return;
        }

        bird.fly(dt);

        if (!blnTouchToPlay) {
            return;
        }

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
        this.spriteBatch = sb;

        sb.setProjectionMatrix(cam.combined);

        sb.begin();

        sb.draw(textureBackground, cam.position.x - (cam.viewportWidth / 2), 0);

        for (Tube tube : tubes) {
            sb.draw(tube.getTopTube(), tube.getPosTopTube().x, tube.getPosTopTube().y);
            sb.draw(tube.getBottomTube(), tube.getPosBotTube().x, tube.getPosBotTube().y);
        }

        sb.draw(textureGround, textureGroundPos1.x, textureGroundPos1.y);
        sb.draw(textureGround, textureGroundPos2.x, textureGroundPos2.y);

        sb.draw(score.getTextureScore(), score.getPosition().x, score.getPosition().y);
        this.displayScore(point, sb, cam.position.x + 170, 20);

        if (!isPlaying) {
            sb.draw(textureScoreBoard, cam.position.x - textureScoreBoard.getWidth() / 2, cam.position.y - 80);

            //High Score
            int highScore = HighScore.get();
            this.displayScore(highScore, sb, cam.position.x + 120, cam.position.y - 5);

            //Score
            this.displayScore(point, sb, cam.position.x + 120, cam.position.y - 55);

            spritePlay.setX(cam.position.x - spritePlay.getWidth() / 2);
            spritePlay.setY(cam.position.y - 150);
            sb.draw(spritePlay, spritePlay.getX(), spritePlay.getY());

            sb.draw(themeSound.getSprite(), themeSound.getSprite().getX(), themeSound.getSprite().getY());
            sb.draw(gameSound.getSprite(), gameSound.getSprite().getX(), gameSound.getSprite().getY());

        } else if (!blnTouchToPlay) {
            sb.draw(touchToPlay.getSprite(), touchToPlay.getSprite().getX(), touchToPlay.getSprite().getY());
        }

        if (blnGameOver) {
            sb.draw(new Texture("empty_star.png"), bird.getPosition().x + 20, bird.getPosition().y);
        } else if (isPlaying) {
            sb.draw(bird.getTexture(), bird.getPosition().x, bird.getPosition().y);
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
                    ex.printStackTrace();
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
        if (HighScore.get() < point) {
            HighScore.save(point);
        }
        blnGameOver = true;
        bird.dead();

        if (gameSound.isSelected()) {
            sound.play();
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
}
