package np.com.pradipkharbuja.flappybee.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import np.com.pradipkharbuja.flappybee.FlappyBee;

/**
 * Created by Pradip Kharbuja on 12/23/2015.
 */
public class MenuState extends State{

    private Texture background;
    private Texture playBtn;
    private Texture textureLogo;
    private Texture textureDialog;
    private Texture textureSound;

    public MenuState(GameStateManager gsm) {
        super(gsm);

        cam.setToOrtho(false, FlappyBee.WIDTH, FlappyBee.HEIGHT);

        background = new Texture("bg.png");
        playBtn = new Texture("playbtn.png");
        textureLogo = new Texture("logo.png");
        textureDialog = new Texture("dialog.png");

        textureSound = new Texture("sound_on.png");
    }

    @Override
    public void handleInput() {
        if (Gdx.input.justTouched()) {
            gsm.set(new PlayState(gsm));
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();

        sb.draw(background, 0, 0);
        sb.draw(textureLogo, cam.position.x - textureLogo.getWidth() / 2, cam.position.y + 100);
        sb.draw(playBtn, cam.position.x - playBtn.getWidth() / 2, cam.position.y);
        sb.draw(textureSound, cam.position.x - textureSound.getWidth() / 2, cam.position.y - 80);
        sb.draw(textureDialog, cam.position.x - textureDialog.getWidth() / 2, cam.position.y - 250);

        sb.end();
    }

    @Override
    public void dispose() {
        //to prevent memory leak
        background.dispose();
        playBtn.dispose();
        textureLogo.dispose();

        System.out.println("Menu state disposed");
    }
}