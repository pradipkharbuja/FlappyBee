package np.com.pradipkharbuja.flappybee.core.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Created by Pradip Kharbuja on 12/23/2015.
 */
public abstract class State extends Stage{

    protected OrthographicCamera cam;
    protected Vector3 mouse;
    protected GameStateManager gsm;

    protected State(GameStateManager gsm) {
        this.gsm = gsm;
        cam = new OrthographicCamera();
        mouse = new Vector3();
    }

    protected abstract void handleInput();

    public abstract void update(float dt); //delta time is the time difference one frame to another

    public abstract void render(SpriteBatch sb);

    public abstract void dispose();
}

