package pantallas;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
	private Sprite logo;
	private SpriteBatch sprBatch;
	private Hud hud = new Hud(null, this);
	private float posicion = 450;
	private int cont = 0;
	
	@Override
	public void show() {
		camara = new OrthographicCamera();
		viewport = new FitViewport(Utiles.ancho,Utiles.alto,camara);
		sprBatch = new SpriteBatch();
		
		stage = new Stage(viewport); 
		stage.addActor(hud);
		
		logo = new Sprite(new Texture("logo.png"));
		logo.setSize(321, 87);
		logo.setPosition(Utiles.ancho/2, 600);
		
		botonJugar = new Boton("botones/boton 1.png",new Vector2(Utiles.ancho/2,300),null);
		botonConfig = new Boton("botones/boton 2.png",new Vector2(Utiles.ancho/2,180),null);
		botonCreditos = new Boton("botones/boton 3.png",new Vector2(Utiles.ancho/2,60),this);
		hud.setearPopUp("botones/popup.png");
		
		stage.addActor(botonJugar); 
		stage.addActor(botonConfig); 
		stage.addActor(botonCreditos);

		Gdx.input.setInputProcessor(stage);
		System.out.println("pantalla menu" + camara.position);
		System.out.println("pantalla menu viewport" + viewport.getScreenHeight() + " " + viewport.getScreenWidth());
		System.out.println("pantalla menu viewport world" + viewport.getWorldHeight()+ " " + viewport.getWorldWidth());
	}

	@Override
	public void render(float delta) {
		Render.limpiarPantalla();
		stage.draw(); 
		stage.act();
		
		if (cont == 0 || cont < 5) {
			posicion += 0.1f;
			cont += 1;
		}else if (cont == 5 || cont < 10) { 
			posicion -= 0.1f;
			cont += 1;
		}else if (cont==10) cont=0;
		System.out.println("posicion:" + posicion);
		System.out.println("cont:" + cont);
		System.out.println("-----");
		
		sprBatch.begin();
		sprBatch.draw(logo, Utiles.ancho/2-logo.getWidth(), posicion);
		sprBatch.end();
		
		hud.dibujarHud();
		camara.update();	
	
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
