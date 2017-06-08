package no.kh498.boxy3.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import no.kh498.boxy3.BoxyMain;

public class DesktopLauncher {
    public static void main(final String[] arg) {
        final LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = BoxyMain.WIDTH;
        config.height = BoxyMain.HEIGHT;
        new LwjglApplication(new BoxyMain(), config);
    }
}
