package pantallas;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.viewport.FitViewport;

import menu.Hud;
import menu.Boton;
import utiles.Render;
import utiles.Utiles;

public class PantallaMenu implements Screen {

	public FitViewport viewport;
	private OrthographicCamera camara;
	private Stage stage;
	private Boton botonJugar; 
	private Boton botonConfig; 
	private Boton botonCreditos; 
	private Hud hud = new Hud(null, this);
	private boolean primerShow = true;
	
	@Override
	public void show() {
		camara = new OrthographicCamera();
		//camara.setToOrtho(false, (Utiles.ancho), (Utiles.ancho));
		viewport = new FitViewport(Utiles.ancho,Utiles.alto,camara);

		stage = new Stage(viewport); 
		stage.addActor(hud);
		
		botonJugar = new Boton("botones/boton 1.png",new Vector2(Utiles.ancho/2,420),null);
		botonConfig = new Boton("botones/boton 2.png",new Vector2(Utiles.ancho/2,300),null);
		botonCreditos = new Boton("botones/boton 3.png",new Vector2(Utiles.ancho/2,180),this);
		hud.setearPopUp("botones/popup.png");
		
		//float x = hud.getPosicionLlegadaPopUp().x+hud.getPopUp().getWidth()-botonCerrar.getSpr().getWidth()-10*Utiles.PPM;
		//float y = hud.getPosicionLlegadaPopUp().y+hud.getPopUp().getHeight()-botonCerrar.getSpr().getHeight()-10*Utiles.PPM;
		
		stage.addActor(botonJugar); 
		stage.addActor(botonConfig); 
		stage.addActor(botonCreditos);

		// stage.setKeyboardFocus(actor1);  pa que ande el input del teclado
		Gdx.input.setInputProcessor(stage);
		System.out.println("pantalla menu" + camara.position);
		System.out.println("pantalla menu viewport" + viewport.getScreenHeight() + " " + viewport.getScreenWidth());
		System.out.println("pantalla menu viewport world" + viewport.getWorldHeight()+ " " + viewport.getWorldWidth());

		primerShow = false;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.5f, 0, 0.5f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.draw(); 
		stage.act();
		hud.dibujarHud();
		camara.update();	
		//botonCerrar.setPosition(hud.getPopUp().getWidth()+hud.getPopUp().getX()-(1*Utiles.PPM),hud.getPopUp().getHeight()+hud.getPopUp().getY()-(1*Utiles.PPM));

		Render.batch.setProjectionMatrix(camara.combined);
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);	
		Utiles.alto = height;
		Utiles.ancho = width;
	}

	@Override
	public void pause() {
	}
	@Override
	public void resume() {	
	}
	@Override
	public void hide() {	
		//cuando cambias de pantalla se llama a este metodo
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

	public void cambiarModoBotones(Touchable estado) {
		botonJugar.setTouchable(estado);
		botonConfig.setTouchable(estado);
		botonCreditos.setTouchable(estado);
	}
	
	public Hud getHud() {
		return hud;
	}
}
