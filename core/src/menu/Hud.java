package menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import pantallas.PantallaMenu;
import elementos.Texto;
import utiles.Utiles;

public class Hud extends Actor{
	
	private Sprite hud;
	private Sprite popUp;
	private SpriteBatch hudBatch;
	private Texto texto;
	private Screen pantalla;
	
	private float opacidad = 0;
	private Vector2 posicionInicial, posicionLlegada, posicion = new Vector2();
	private float tiempo, duracion = 2f;
	private ShapeRenderer renderer;
	private boolean mover, clickPopUp;
    
	public Hud(String hudNombre, Screen pantalla)  {
		this.pantalla = pantalla;
		hudBatch = new SpriteBatch();
		if(hudNombre != null) {
		hud = new Sprite(new Texture(hudNombre));
		}
		renderer = new ShapeRenderer();
		
		setBounds(0, 0, Utiles.ancho/Utiles.PPM, Utiles.alto/Utiles.PPM);
		addListener(new InputListener(){
	        	@Override
	            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
	        		clickPopUp = true;
	        		
	            	return true; 
	            }
	        	
	        	public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
	        		clickPopUp = false;
	        	}
	        });
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		if (mover) {

			posicion.set(posicionLlegada);
			posicion.sub(posicionInicial);
			posicion.scl(Interpolation.swingOut.apply(tiempo/duracion));
			posicion.add(posicionInicial);
			
			popUp.setPosition(posicion.x, posicion.y);
			
			tiempo += delta;
			
			if(tiempo>duracion) {
				mover = false;
				tiempo = 0;
			}
		}
		
		if (clickPopUp) {
	   		moverPopUp(false); // desde aca se llamaría al metodo inverso

	   		if(pantalla instanceof PantallaMenu) {
	   			((PantallaMenu) pantalla).cambiarModoBotones(Touchable.enabled);
	   		}
	   	}
	}
	
	public void setearPopUp(String popUp) {
		this.popUp = new Sprite(new Texture(popUp));
		posicionInicial = new Vector2(((Utiles.ancho/Utiles.PPM)/2)-this.popUp.getWidth()/2,0-this.popUp.getHeight());
		posicionLlegada = new Vector2(posicionInicial.x,((Utiles.alto/Utiles.PPM)/2)-this.popUp.getHeight()/2);
		this.popUp.setPosition(posicionInicial.x, posicionInicial.y);
		
		//cruz = new Boton("botones/cruz.png", new Vector2(this.popUp.getWidth()-(10*Utiles.PPM),this.popUp.getHeight()-(10*Utiles.PPM)));
	}
	
	public void moverPopUp(boolean entra) { 
		mover = true;
		
		if (!entra) {
			this.setTouchable(Touchable.disabled);
			if(pantalla instanceof PantallaMenu) ((PantallaMenu) pantalla).cambiarModoBotones(Touchable.enabled);
		}else {
			this.setTouchable(Touchable.enabled);
			if(pantalla instanceof PantallaMenu) ((PantallaMenu) pantalla).cambiarModoBotones(Touchable.disabled);
		}
		// hacer que pase un true o false o algo asi
		// y hacer puntosalida = puntollegada o sea darlos vuelta
	}
	
	public void dibujarHud() {
		if (mover || opacidad>0.6f) {
			Gdx.gl.glEnable(GL20.GL_BLEND);
			
			if(opacidad<0.6f) opacidad += 0.01;
			renderer.begin(ShapeType.Filled);
			renderer.setColor(0, 0, 0, opacidad);
			renderer.rect(0, 0, Utiles.ancho/Utiles.PPM, Utiles.alto/Utiles.PPM);
			renderer.end();
			
			Gdx.gl.glDisable(GL20.GL_BLEND);
		}
		
		hudBatch.begin();
		if(hud != null)hud.draw(hudBatch);
		popUp.draw(hudBatch);
		
		hudBatch.end();
	}

	public Sprite getPopUp() {
		return popUp;
	}
	
	public Vector2 getPosicionLlegadaPopUp() {
		return posicionLlegada;
	}
}
