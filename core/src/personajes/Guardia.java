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

public class Guardia extends Jugador {

	public boolean arrestando;
	private Sprite sprArrestar;


	public Guardia(Cuerpo cuerpo, String sprite) {
		super(cuerpo, sprite);
		sprArrestar = new Sprite( new Texture("personajes/badlogic.jpg") );
		sprArrestar.setSize(16 * Utiles.PPM, 16 * Utiles.PPM);
		
		Inputlistener = new InputListener() {
			public boolean keyDown (InputEvent event, int keycode) {
				enviarMovimiento(keycode, true);	
				
				if (keycode == Keys.E) arrestando = true; 
				
				return true;
			}
			public boolean keyUp (InputEvent event, int keycode) {
				enviarMovimiento(keycode, false);
				
				if (keycode == Keys.E) arrestando = false; TiempoAccion = 0;
				
				return false;
			}
		};
        addListener(Inputlistener);
	}
	
	//--------------------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------SCENE 2D-----------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------------------------	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		hud.setTexto((int)(cuentaregresiva - Global.tiempo) + "\n");
	}
	@Override
	public void act(float delta) {
		super.act(delta);
	}
	@Override
	public void setDireccion(Vector2 xy) {
		super.setDireccion(xy);
	}
	//--------------------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------ANIMACION-----------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------------------------	
	@Override
	protected TextureRegion animacionMovimiento() {
		return super.animacionMovimiento();
	}
	//--------------------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------ACCIONES-----------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------------------------
	public void arrestar(Entidad entidadArrestada) {
		if(TiempoAccion++ == 5) {
			TiempoAccion = 0;
			if(entidadArrestada instanceof Ladron) {
				super.finalizarRonda(true, this);
			}else{
				super.finalizarRonda(false, this);
			}
		}
	}
	//--------------------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------GETTERS------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------------------------
	public boolean isArrestando() {
		return arrestando;
	}
	public Sprite getSprArrestar() {
		return sprArrestar;
	}
}
