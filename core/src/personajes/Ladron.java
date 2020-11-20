package personajes;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import utiles.Global;
import utiles.Utiles;

import cuerpos.Cuerpo;

public class Ladron extends Jugador{
	
	private boolean robando;
	private int billeteras = 0, numAzar;
	private Sprite sprRobo;
	
	public Ladron(Cuerpo cuerpo, String sprite) {
		super(cuerpo, sprite);
		if(Global.ronda == 1) {
			sprRobo = new Sprite( new Texture("personajes/badlogic.jpg") );
			sprRobo.setSize(16 * Utiles.PPM, 16 * Utiles.PPM);
		}
		Inputlistener = new InputListener() {
			public boolean keyDown (InputEvent event, int keycode) {
				if (Global.ronda == 1) enviarMovimiento(keycode, true);
				
				if (keycode == Keys.E) robando = true;
				
				if (Global.ronda != 1) {
					keyDown = true;
					keyCode = keycode;
				}
				
				return true;
			}
			public boolean keyUp (InputEvent event, int keycode) {
				if (Global.ronda == 1) enviarMovimiento(keycode, false);
				
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
	}
	public void act(float delta){
		super.act(delta);
		hud.setTexto((int)(cuentaregresiva - Global.tiempo) + "\n" + billeteras + "-5");
		if((Global.ronda==1) && cuentaregresiva - Global.tiempo < 1) super.finalizarRonda(false, this);
	}
	@Override
	public void setDireccion(Vector2 xy) {
		super.setDireccion(xy);
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
		
		if(TiempoAccion == 50) {numAzar = randomF;}
		if(TiempoAccion > 50 && numAzar == randomF) {return 2;}
		
		System.out.println(TiempoAccion);
		if(TiempoAccion++ == 100) {
			TiempoAccion = 0;
			billeteras ++;
			
			hud.setTexto(billeteras + "-5");
			
			if(billeteras > 4) {
				super.finalizarRonda(true, this);
			}
			System.out.println("robo con exito");
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
}
