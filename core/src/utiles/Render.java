package utiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Render {
	public static SpriteBatch batch = new SpriteBatch();

	public static void limpiarPantalla() {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
	public static void limpiarPantalla(float r, float g, float b, float alpha) {
		Gdx.gl.glClearColor(r, g, b, alpha);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
}
