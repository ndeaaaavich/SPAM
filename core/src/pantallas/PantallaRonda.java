package pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

import mapas.Mapa;
import personajes.Guardia;
import personajes.Ladron;
import personajes.SpriteInfo;
import red.HiloCliente;
import utiles.Global;
import utiles.Render;
import utiles.Utiles;

public class PantallaRonda implements Screen {
	
	public Ladron jugadorLadron;
	public Guardia jugadorGuardia;
	
	protected OrthographicCamera camera;

	public World mundo;
	protected Box2DDebugRenderer b2dr;

	protected TiledMap tileMap;
	protected OrthogonalTiledMapRenderer tmr;

	protected Mapa mapa;

	protected FitViewport viewport;
	public Stage stage;

	public float posGuardiaX, posGuardiaY, posLadronX, posLadronY;
	
	public PantallaRonda(Vector2 gravedad, String rutaMapa) {
		//Global.ronda = numRonda;
		int WScreen = Gdx.graphics.getWidth();
		int HScreen = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, (WScreen * Utiles.PPM) / 2, (HScreen * Utiles.PPM) / 2);
		//mundo
		mundo = new World(gravedad, true);
		viewport = new FitViewport((WScreen * Utiles.PPM) / 2, (HScreen * Utiles.PPM) / 2, camera);
		stage = new Stage(viewport);
		//TiledMap
		tileMap = new TmxMapLoader().load(rutaMapa);
		b2dr = new Box2DDebugRenderer();

		tmr = new OrthogonalTiledMapRenderer(tileMap, 1 * Utiles.PPM);
		mapa = new Mapa(tileMap, mundo);
	}
	@Override
	public void show() {
		
	}
	@Override
	public void render(float delta) {
	}
	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}
	@Override
	public void pause() {
	}
	@Override
	public void resume() {
	}
	@Override
	public void hide() {	
	}
	@Override
	public void dispose() {
		Render.batch.dispose();
		stage.dispose();
		mundo.dispose();
	}
	//--------------------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------GETTERS------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------------------------
	
	
	//--------------------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------SETTERS------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------------------------
}
