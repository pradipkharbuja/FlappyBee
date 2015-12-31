package np.com.pradipkharbuja.flappybee.core;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import np.com.pradipkharbuja.flappybee.core.misc.Project;
import np.com.pradipkharbuja.flappybee.core.service.DialogInterface;
import np.com.pradipkharbuja.flappybee.core.states.GameStateManager;
import np.com.pradipkharbuja.flappybee.core.states.MenuState;
import np.com.pradipkharbuja.flappybee.core.states.PlayState;

public class FlappyBee extends ApplicationAdapter {

    private DialogInterface dialogInterface;

    public FlappyBee(DialogInterface dialogInterface) {
        this.dialogInterface = dialogInterface;
    }

    @Override
    public void dispose() {
        super.dispose();
        music.dispose();
    }

    public static final int WIDTH = 390;
    public static final int HEIGHT = 650;

    public static final String TITLE = "Flappy Bee";

    private GameStateManager gsm;
    private SpriteBatch batch;

    public static Music music;

    @Override
    public void create() {
        batch = new SpriteBatch();
        gsm = new GameStateManager();

        music = Gdx.audio.newMusic(Gdx.files.internal("theme.mp3"));
        music.setLooping(true);

        Gdx.gl.glClearColor(1, 0, 0, 1);
        gsm.push(new MenuState(gsm, dialogInterface));

        new Project();

        //Gdx.input.setCatchBackKey(true);
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gsm.update(Gdx.graphics.getDeltaTime());
        gsm.render(batch);
    }
}
