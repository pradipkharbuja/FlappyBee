package np.com.pradipkharbuja.flappybee.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import np.com.pradipkharbuja.flappybee.FlappyBee;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.title = FlappyBee.TITLE;
        config.width = FlappyBee.WIDTH;
        config.height = FlappyBee.HEIGHT;
        config.resizable = false;
        config.addIcon("icon.png", Files.FileType.Internal);

        new LwjglApplication(new FlappyBee(), config);
    }
}
