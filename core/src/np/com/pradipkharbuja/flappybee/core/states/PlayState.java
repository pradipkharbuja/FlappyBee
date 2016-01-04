package np.com.pradipkharbuja.flappybee.core.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import np.com.pradipkharbuja.flappybee.core.FlappyBee;
import np.com.pradipkharbuja.flappybee.core.service.CallOutLocation;
import np.com.pradipkharbuja.flappybee.core.service.DialogInterface;
import np.com.pradipkharbuja.flappybee.core.service.HighScore;
import np.com.pradipkharbuja.flappybee.core.sprites.Bird;
import np.com.pradipkharbuja.flappybee.core.sprites.GameSound;
import np.com.pradipkharbuja.flappybee.core.sprites.MyProfile;
import np.com.pradipkharbuja.flappybee.core.sprites.PlayButton;
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

    //Sprites
    private Music sound;
    private ThemeSound themeSound;
    private GameSound gameSound;
    private TouchToPlay touchToPlay;
    private MyProfile myProfile;
    private PlayButton playButton;

    private boolean isPlaying;

    public static int point = 0;
    private int delay = 0;

    private boolean blnTouchToPlay = false;
    private boolean blnGameOver = false;

    private boolean topTubeTouched;

    private CallOutLocation callOutLocation;
    private Rectangle intersectedRectangle;

    @Override
    public boolean scrolled(int amount) {
        return super.scrolled(amount);
    }

    public PlayState(GameStateManager gsm, DialogInterface dialogInterface) {
        super(gsm);

        this.dialog = dialogInterface;

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

        themeSound = new ThemeSound(cam.position.x - 150, cam.position.y - 150);
        themeSound.play();

        playButton = new PlayButton(cam.position.x - 90, cam.position.y - 150);

        myProfile = new MyProfile(cam.position.x + 70, cam.position.y - 150);

        touchToPlay = new TouchToPlay(cam.position.x, cam.position.y + 80);
    }

    private DialogInterface dialog;

    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched()) {
            int x1 = Gdx.input.getX();
            int y1 = Gdx.input.getY();
            Vector3 input = new Vector3(x1, y1, 0);
            cam.unproject(input);

            if (!isPlaying) {
                if (playButton.getSprite().getBoundingRectangle().contains(input.x, input.y)) {
                    isPlaying = true;
                    point = 0;
                    themeSound.play();
                } else if (myProfile.getSprite().getBoundingRectangle().contains(input.x, input.y)) {
                    dialog.showDialog();
                }
                //Theme Sound
                else if (themeSound.getSprite().getBoundingRectangle().contains(input.x, input.y)) {
                    themeSound.toggle();
                }
                //Game Sound
                else if (gameSound.getSprite().getBoundingRectangle().contains(input.x, input.y)) {
                    gameSound.toggle();
                }
            } else if (!blnTouchToPlay) {
                blnTouchToPlay = true;
            } else if (blnTouchToPlay) {
                bird.jump();
            }
        }
    }

    private boolean blnGroundGameOver = false;

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

            if (tube.collides(bird)) {
                topTubeTouched = tube.isTopTubeTouched();
                callOutLocation = tube.getCallOutLocation();
                this.intersectedRectangle = tube.getIntersectedRectangle();
                this.gameOver();
            }
        }

        if (bird.getPosition().y <= textureGround.getHeight() + textureGround_Y_OFFSET) {
            blnGroundGameOver = true;
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

            sb.draw(playButton.getSprite(), playButton.getSprite().getX(), playButton.getSprite().getY());

            sb.draw(themeSound.getSprite(), themeSound.getSprite().getX(), themeSound.getSprite().getY());
            sb.draw(gameSound.getSprite(), gameSound.getSprite().getX(), gameSound.getSprite().getY());

            sb.draw(myProfile.getSprite(), myProfile.getSprite().getX(), myProfile.getSprite().getY());

        } else if (!blnTouchToPlay) {
            sb.draw(touchToPlay.getSprite(), touchToPlay.getSprite().getX(), touchToPlay.getSprite().getY());
        }

        if (blnGameOver) {
            sb.draw(bird.getTexture(), bird.getPosition().x, bird.getPosition().y);

            Texture callout = new Texture("callout.png");
            if (blnGroundGameOver) {
                sb.draw(callout, bird.getPosition().x + 10, bird.getPosition().y - 20);
            } else {
                float x = bird.getPosition().x;
                float y = bird.getPosition().y;

                if (topTubeTouched) {
                    if (callOutLocation == CallOutLocation.LEFT) {
                        x += 30; y -= 15;
                    } else if (callOutLocation == CallOutLocation.TOP) {
                        x += 10; y += 0;
                    } else if(callOutLocation == CallOutLocation.TOP_RIGHT){
                        x -= 20; y += 10;
                    } else {
                        x += 20; y += 0;
                    }
                } else {
                    if (callOutLocation == CallOutLocation.LEFT) {
                        x += 30; y -= 10;
                    } else if (callOutLocation == CallOutLocation.TOP) {
                        x += 10; y -= 30;
                    } else if(callOutLocation == CallOutLocation.TOP_RIGHT){
                        x -= 20; y -= 30;
                    } else { //top left
                        x += 20; y -= 20;
                    }
                }
                sb.draw(callout, x, y);
            }

        } else if (isPlaying) {
            sb.draw(bird.getTexture(), bird.getPosition().x, bird.getPosition().y);
        }

        sb.end();
    }

    @Override
    public void dispose() {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        t.start();


        bird.dispose();

        textureBackground.dispose();
        textureGround.dispose();
        textureScoreBoard.dispose();

        for (Tube tube : tubes) {
            tube.dispose();
        }

        score.dispose();

        themeSound.dispose();
        gameSound.dispose();
        touchToPlay.dispose();
        myProfile.dispose();
        playButton.dispose();

        sound.dispose();
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

        dialog.postScore();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        gsm.set(new PlayState(gsm, dialog));
                    }
                });
            }
        }).start();
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
