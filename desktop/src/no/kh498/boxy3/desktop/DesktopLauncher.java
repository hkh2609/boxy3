package no.kh498.boxy3.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import no.kh498.boxy3.BoxyMain;

public class DesktopLauncher {

    public static final int SCREEN_WIDTH = 1200;
    public static final int SCREEN_HEIGHT = 720;

    public static void main(final String[] arg) {

        final LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = SCREEN_WIDTH;
        config.height = SCREEN_HEIGHT;
        config.forceExit = true;
        config.vSyncEnabled = true;
//        config.resizable = false;
//        config.foregroundFPS = 500;
        new LwjglApplication(new BoxyMain(), config);

    }
}
