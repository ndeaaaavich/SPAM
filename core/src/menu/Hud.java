package menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
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
import pantallas.PantallaRonda1;
import personajes.Jugador;
import elementos.Texto;
import utiles.Global;
import utiles.Utiles;

public class Hud extends Actor{
	
	private Sprite hud;
	private Sprite popUp;
	private SpriteBatch hudBatch;

	private Texto[] textos = new Texto[4];	
	//0 Tiempo, 1 textoSala, 2 textoLadron, 3 textoGuardia;
	
	private Screen pantalla;
	private float opacidad = 0;
	private Vector2 posicionInicial, posicionLlegada, posicion = new Vector2();
	private float tiempo, duracion = 2f, cuentaregresiva = 500;
	private ShapeRenderer renderer;
	private boolean mover, clickPopUp, entra;
    
	public Hud(String hudNombre, Screen pantalla)  {	
		
	for (int i = 0; i < textos.length; i++) {	
		textos[i] = new Texto("fonts/Early GameBoy.ttf", 25, Color.WHITE, false);	
	}	
		textos[0].setPosition(Utiles.ancho-90, Utiles.alto-50);	
		textos[1].setPosition(50, Utiles.alto-50);	
		textos[2].setPosition(Utiles.ancho-90, Utiles.alto-20);	
		textos[3].setPosition(Utiles.ancho-90, Utiles.alto-20);	
	
		this.pantalla = pantalla;
		setTouchable(Touchable.disabled);
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
				
				posicion.set(posicionInicial);
				posicionInicial.set(posicionLlegada);
				posicionLlegada.set(posicion);
				posicion.setZero();
			}
		}
		
		if (clickPopUp) {
	   		moverPopUp(false); // desde aca se llamaría al metodo inverso

	   		if(pantalla instanceof PantallaMenu) {
	   			((PantallaMenu) pantalla).cambiarModoBotones(Touchable.enabled);
	   		}
	   		
		textos[0].setTexto("" + (int)(cuentaregresiva - Global.tiempo));	
		textos[1].setTexto("" + Global.ronda + "-3");	
			if(Global.empiezaJuego){	
				if(Global.guardia) {	
					//textos[3].setTexto( ((PantallaRonda1)pantalla).jugadorGuardia);	
					//aca se muestra la cantidad de pistas que el guardia tiene	
				}else {	
					textos[2].setTexto( ((PantallaRonda1)pantalla).jugadorLadron.getBilleteras() + "-5" );	
					if(cuentaregresiva - Global.tiempo < 1) {	
						((PantallaRonda1)pantalla).jugadorLadron.finalizarRonda(false, (Jugador)((PantallaRonda1)pantalla).jugadorLadron.getUserData());	
					}	
				}	
			}
	   	}
		
	}
	
	public void setearPopUp(String popUp) {
		this.popUp = new Sprite(new Texture(popUp));
		posicionInicial = new Vector2(((Utiles.ancho)/2)-this.popUp.getWidth()/2,0-this.popUp.getHeight());
		posicionLlegada = new Vector2(posicionInicial.x,((Utiles.alto)/2)-this.popUp.getHeight()/2);
		this.popUp.setPosition(posicionInicial.x, posicionInicial.y);
		
		//cruz = new Boton("botones/cruz.png", new Vector2(this.popUp.getWidth()-(10*Utiles.PPM),this.popUp.getHeight()-(10*Utiles.PPM)));
	}
	
	public void moverPopUp(boolean entra) { 
		mover = true;
		this.entra = entra;
		if (!this.entra) {
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
		if (mover || opacidad>=0.6f) {
			Gdx.gl.glEnable(GL20.GL_BLEND);
			
			if(opacidad<0.6f && entra) opacidad += 0.01;
			if(opacidad>0 && !entra) opacidad -= 0.01;
			renderer.begin(ShapeType.Filled);
			renderer.setColor(0, 0, 0, opacidad);
			renderer.rect(0, 0, Utiles.ancho/Utiles.PPM, Utiles.alto/Utiles.PPM);
			renderer.end();
			
			Gdx.gl.glDisable(GL20.GL_BLEND);
		}
		
		hudBatch.begin();
		if(hud != null)hud.draw(hudBatch);
		
		for (int i = 0; i < textos.length; i++) {	
			textos[i].draw(hudBatch);	
		}
		
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
