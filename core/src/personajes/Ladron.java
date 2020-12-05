package personajes;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import elementos.Texto;
import utiles.Global;
import utiles.Utiles;

public class Ladron extends Jugador{
	
	private boolean robando;
	private int billeteras = 0, numAzar; 
	private Sprite sprRobo, indicador;
	private Texto texRobo;

	public Ladron(String sprite, float posX, float posY, int sala) {
		super(sprite);
		
		setPosition(posX, posY);
		setSala(sala);
		cambiarSala = true;
		
		sprRobo = new Sprite( new Texture("personajes/badlogic.jpg") );
		sprRobo.setSize(16 * Utiles.PPM, 16 * Utiles.PPM);
		indicador = new Sprite( new Texture("personajes/flecha_abajo.png") );
		indicador.setSize(20 * Utiles.PPM, 15 * Utiles.PPM);
		
		texRobo = new Texto("fonts/Early GameBoy.ttf", 10, Color.WHITE, false);
		texRobo.setTexto("puto");
		
		Inputlistener = new InputListener() {
			public boolean keyDown (InputEvent event, int keycode) {
				enviarMovimiento(keycode, true);
				
				if (keycode == Keys.E) robando = true;
				
				if (Global.ronda != 1) {
					keyDown = true;
					keyCode = keycode;
				}
				
				return true;
			}
			public boolean keyUp (InputEvent event, int keycode) {
				enviarMovimiento(keycode, false);
				
				if (keycode == Keys.E) robando = false; TiempoAccion = 0;
				
				if (Global.ronda != 1) {
					keyDown = false;
					keyCode = keycode;
				}

				
				return false;
			}
		};
	    addListener(Inputlistener);
	}

	//--------------------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------SCENE 2D-----------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------------------------
	public void draw(Batch batch, float parentAlpha){
		super.draw(batch, parentAlpha);
		if(Global.tiempo <10 && !Global.guardia) {
			indicador.draw(batch);
		}
	}
	public void act(float delta){
		super.act(delta);
		if(Global.tiempo <10 && !Global.guardia) {
			indicador.setPosition(animacion.getPosition().x - 1*Utiles.PPM, animacion.getPosition().y + animacion.getAlto());
		}
	}
	//--------------------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------ANIMACION-----------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------------------------
	protected TextureRegion animacionMovimiento() {
		return super.animacionMovimiento();
	}
	//--------------------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------ACCIONES-----------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------------------------
	public int robar() {
		//0 robo exitoso, 1 nada, 2 detectado
		int randomF = Utiles.r.nextInt(100)+1;
		
		if(TiempoAccion == 25) {numAzar = randomF;}
		if(TiempoAccion > 25 && numAzar == randomF) {return 2;}
		
		if(TiempoAccion++ == 80) {
			TiempoAccion = 0;
			billeteras ++;
			
			if(billeteras > 2) {
				super.finalizarRonda(true, this);
			}
			
			return 0;
		}else {
			return 1;
		}
	}
	//--------------------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------GETTERS------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------------------------
	public boolean isRobando() {
		return robando;
	}	
	public Sprite getSprRobo() {
		return sprRobo;
	}
	public int getBilleteras() {
		return billeteras;
	}
	public Texto getTexRobo() {
		return texRobo;
	}
}
