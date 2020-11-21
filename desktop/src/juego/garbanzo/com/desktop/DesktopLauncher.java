package juego.garbanzo.com.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import main.Principal;


public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new Principal(), config);
		//config.width = 1366;
		//config.height = 768;
		config.width = 800;
		config.height = 550;
	}
}
