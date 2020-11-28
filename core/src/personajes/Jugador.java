package personajes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import elementos.Texto;
import utiles.Utiles;

public abstract class Jugador extends Entidad{

	protected InputListener Inputlistener;
	
	public boolean cambiarSala;
	public int salaAnterior, TiempoAccion = 0, keyCode;
	protected boolean keyDown, salto = true;
	//tiempoModif el tiempo que duran las modificaciones
	protected Texto hud;
	
	public Jugador(String sprite) {
		super(sprite);
		setUserData(this);
	}
	//--------------------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------SCENE 2D-----------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------------------------
	public void draw(Batch batch, float parentAlpha){
		Color color = getColor();
	    batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
	    this.animacion.getSprite().draw(batch);
	}
	public void act(float delta){
		this.animacion.setTexReg(animacionMovimiento());	
		duracion += Gdx.graphics.getRawDeltaTime();
	}
	protected void enviarMovimiento(int keycode, boolean keyDown) {
		Utiles.hc.enviarMensaje("movimiento%" + keycode + "%" + keyDown);
	}
	//--------------------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------ANIMACION-----------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------------------------
	@Override
	protected TextureRegion animacionMovimiento() {
		if(estado == EstadoMovimiento.corriendoDerecha) {// moverse a la derecha
			setDerecha(true);
			ultimoIndice = 0;
			return animacion.getTexReg(0, duracion);
		}
		if(estado == EstadoMovimiento.corriendoIzquierda) {// moverse a la izquierda
			setDerecha(false);
			ultimoIndice = 1;
			return animacion.getTexReg(1, duracion);
		}
		if(estado == EstadoMovimiento.movimientoY) {
			setDerecha((ultimoIndice==0)?true:false);
			return animacion.getTexReg(ultimoIndice, duracion);
		}
		return animacion.getTexReg((ultimoIndice==1)?3:2, duracion);
	}
	//--------------------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------RONDA--------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------------------------
	public void finalizarRonda(boolean gano, Jugador jugador){
		if(jugador instanceof Guardia) {
			Utiles.hc.enviarMensaje((gano)?"guardia%gano":"guardia%perdio");
		}else if(jugador instanceof Ladron) {
			Utiles.hc.enviarMensaje((gano)?"ladron%gano":"ladron%perdio");
		}
	}
	//--------------------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------GETTERS------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------------------------
	public int getSalaAnterior() {
		return salaAnterior;
	}
	public float getFuerzasX() {
		return super.fuerzaX;
	}
	public float getFuerzasY() {
		return super.fuerzaY;
	}	
	public void setVectorFuerzas(Vector2 fuerzas) {
		this.direcciones = fuerzas;
	}
	public Texto getHud() {
		return hud;
	}
}
