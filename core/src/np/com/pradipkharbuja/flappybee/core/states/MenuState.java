package np.com.pradipkharbuja.flappybee.core.states;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import np.com.pradipkharbuja.flappybee.core.FlappyBee;
import np.com.pradipkharbuja.flappybee.core.service.DialogInterface;

/**
 * Created by Pradip Kharbuja on 12/23/2015.
 */
public class MenuState extends State {

    private Texture textureBackground;
    private Texture textureLogo;
    private Texture textureLoading;

    private int delay;
    private DialogInterface dialogInterface;

    public MenuState(GameStateManager gsm, DialogInterface dialogInterface) {
        super(gsm);
        this.dialogInterface = dialogInterface;

        cam.setToOrtho(false, FlappyBee.WIDTH, FlappyBee.HEIGHT);

        textureBackground = new Texture("background.png");
        textureLogo = new Texture("logo.png");
        textureLoading = new Texture("loading.png");
    }

    @Override
    public void handleInput() {
        //do nothing
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);

        sb.begin();

        sb.draw(textureBackground, 0, 0);
        sb.draw(textureLogo, cam.position.x - textureLogo.getWidth() / 2, cam.position.y + 80);
        if (++delay > 50) {
            sb.draw(textureLoading, cam.position.x - textureLoading.getWidth() / 2, cam.position.y - 200);
        }

        sb.end();

        if (delay > 1) {
            gsm.set(new PlayState(gsm, dialogInterface));
        }
    }

    @Override
    public void dispose() {
        //to prevent memory leak
        textureBackground.dispose();
        textureLogo.dispose();
        textureLoading.dispose();
    }
}