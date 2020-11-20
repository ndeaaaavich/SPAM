package personajes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import cuerpos.Cuerpo;
import elementos.Texto;
import utiles.Global;
import utiles.Utiles;

public abstract class Jugador extends Entidad{

	protected InputListener Inputlistener;
	
	public boolean cambiarSala;
	public int salaAnterior, TiempoAccion = 0, cuentaregresiva = 500, keyCode;
	protected boolean keyDown, salto = true;
	public float modificadorX = 0, modificadorY = 0, velocidad = 1, tiempoModif = 10;
	//tiempoModif el tiempo que duran las modificaciones
	protected Texto hud;
	
	public Jugador(Cuerpo cuerpo, String sprite) {
		super(cuerpo, sprite);
		
        cuerpo.setUserData(this);
        
        hud = new Texto("fonts/Early GameBoy.ttf", 25, Color.WHITE, false);
		hud.setPosition(Utiles.ancho-90, Utiles.alto-50);
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
		this.animacion.setPosicion(cuerpo.getPosition().x - (cuerpo.getAncho()/2) ,cuerpo.getPosition().y - (cuerpo.getAlto()/2));
		this.animacion.setTexReg(animacionMovimiento());	
		duracion += Gdx.graphics.getRawDeltaTime();
		
		if(Global.ronda != 1) {
			if((modificadorX != 0 || modificadorY != 0) && (int)(duracion % tiempoModif) == 0) {
				modificadorX = 0;
				modificadorY = 0;
			}
			enviarMovimiento(keyCode, keyDown);
		}
	}
	@Override
	public void setDireccion(Vector2 xy) {
		fuerzas = xy;
		cuerpo.setLinearVelocity(fuerzas);
		animacion.setTexReg(animacionMovimiento());
	}
	protected void enviarMovimiento(int keycode, boolean keyDown) {
		Utiles.hc.enviarMensaje("movimiento%" + keycode + "%" + keyDown);
	}
	//--------------------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------ANIMACION-----------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------------------------
	protected TextureRegion animacionMovimiento() {
		if(fuerzas.x>0) {// moverse a la derecha
			setDerecha(true);
			ultimoIndice = 0;
			return animacion.getTexReg(0, duracion);
		}
		if(fuerzas.x<0) {// moverse a la izquierda
			setDerecha(false);
			ultimoIndice = 1;
			return animacion.getTexReg(1, duracion);
		}
		if(fuerzas.y!=0) {
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
	//-------------------------------------------------------------SETTERS------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------------------------
	public void setModificadorX(float modificadorX) {
		if(this.modificadorX != modificadorX) {
			this.modificadorX = modificadorX;
		}
	}
	public void setModificadorY(float modificadorY) {
		if(this.modificadorY != modificadorY) {
			this.modificadorY = modificadorY;
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
	public float getModificadorX() {
		return modificadorX;
	}
	public float getModificadorY() {
		return modificadorY;
	}	
	public void setVectorFuerzas(Vector2 fuerzas) {
		this.fuerzas = fuerzas;
	}
	public Texto getHud() {
		return hud;
	}
}
