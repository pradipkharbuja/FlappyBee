package np.com.pradipkharbuja.flappybee.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import np.com.pradipkharbuja.flappybee.core.FlappyBee;
import np.com.pradipkharbuja.flappybee.core.service.DialogInterface;

public class DesktopLauncher implements DialogInterface {

    private static DialogInterface dialogInterface = new DesktopLauncher();

    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.title = FlappyBee.TITLE;
        config.width = FlappyBee.WIDTH;
        config.height = FlappyBee.HEIGHT;
        config.resizable = false;
        config.addIcon("icon.png", Files.FileType.Internal);

        new LwjglApplication(new FlappyBee(dialogInterface), config);
    }

    @Override
    public void showDialog() {

    }

    @Override
    public void hideDialog() {

    }

    @Override
    public void postScore() {

    }
}
