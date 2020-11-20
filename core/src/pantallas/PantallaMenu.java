package pantallas;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

import menu.Hud;
import menu.Interfaz;
import utiles.Global;
import utiles.Utiles;

public class PantallaMenu implements Screen {

	private FitViewport viewport;
	private OrthographicCamera camara;
	private Stage stage; 
	private Interfaz botonJugar; 
	private Interfaz botonConfig; 
	private Interfaz botonCreditos; 
	private Hud hud = new Hud(null);
	private float posicionY, posicionX;
	private boolean mostrarCreditos, fin = false;
	
	@Override
	public void show() {
		camara = new OrthographicCamera(0,0);
		viewport = new FitViewport(Utiles.ancho,Utiles.alto,camara);

		stage = new Stage(viewport); 
		stage.addActor(hud);
		
		hud.setearPopUp("botones/popup.png");
		botonJugar = new Interfaz("botones/boton 1.png",new Vector2(Utiles.ancho/2,420),null);
		botonConfig = new Interfaz("botones/boton 2.png",new Vector2(Utiles.ancho/2,300),null);
		botonCreditos = new Interfaz("botones/boton 3.png",new Vector2(Utiles.ancho/2,180),this);
		stage.addActor(botonJugar); 
		stage.addActor(botonConfig); 
		stage.addActor(botonCreditos); 
		
		// stage.setKeyboardFocus(actor1);  pa que ande el input del teclado
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.5f, 0, 0.5f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.draw(); 
		stage.act();
		
		hud.dibujarHud();
		camara.update();	
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
		stage.dispose(); //
		
	}

	public Hud getHud() {
		return hud;
	}
}
