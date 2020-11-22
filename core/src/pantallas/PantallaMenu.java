package pantallas;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

import menu.Hud;
import menu.Boton;
import utiles.Utiles;

public class PantallaMenu implements Screen {

	private FitViewport viewport;
	private OrthographicCamera camara;
	private Stage stage; 
	private Boton botonJugar; 
	private Boton botonConfig; 
	private Boton botonCreditos; 
	private Hud hud = new Hud(null);
	private float posicionY, posicionX;
	private boolean mostrarCreditos, fin = false;
	
	@Override
	public void show() {
		camara = new OrthographicCamera(0,0);
		viewport = new FitViewport(Utiles.ancho,Utiles.alto,camara);

		stage = new Stage(viewport); 
		stage.addActor(hud);
		
		botonJugar = new Boton("botones/boton 1.png",new Vector2(Utiles.ancho/2,420),null);
		botonConfig = new Boton("botones/boton 2.png",new Vector2(Utiles.ancho/2,300),null);
		botonCreditos = new Boton("botones/boton 3.png",new Vector2(Utiles.ancho/2,180),this);
		hud.setearPopUp("botones/popup.png");
		
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
