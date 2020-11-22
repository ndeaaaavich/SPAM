package personajes;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import cuerpos.Animacion;
import cuerpos.Cuerpo;

public abstract class Entidad extends Actor{
	
	protected int ultimoIndice;
	protected float duracion;
	protected Animacion animacion;
	
	protected int sala = -1;
	protected float fuerzaX = 0, fuerzaY = 0;
	protected Vector2 fuerzas = new Vector2();
	protected boolean derecha = true; // por default todos los pj aparecen mirando a la derecha
	
	public Object userData;
	
	public Object getUserData() {
		return userData;
	}
	public void setUserData(Object userData) {
		this.userData = userData;
	}
	public Entidad(String sprite) {
		this.animacion = new Animacion(sprite, 21, 4);
		setBounds(animacion.getPosition().x,animacion.getPosition().y,
				animacion.getAncho(),animacion.getAlto());
	}
	//--------------------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------SCENE 2D-----------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------------------------
	public abstract void draw(Batch batch, float parentAlpha);
	public abstract void act(float delta);
	public abstract void setDireccion(Vector2 xy);
	//--------------------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------ANIMACION-----------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------------------------
	protected abstract TextureRegion animacionMovimiento();
	//--------------------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------GETTERS------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------------------------
	/*public Vector2 jugadorGuardia() {
		return cuerpo.getPosition();
	}*/
	
	public int getSala() {
		return this.sala;
	}
	/*public float getAncho() {
		return cuerpo.getAncho();
	}*/
	public float getAncho() {
		return animacion.getAncho();
	}
	public float getAlto() {
		return animacion.getAlto();
	}
	/*public float getAlto() {
		return cuerpo.getAlto();
	}
	public Cuerpo getCuerpo() {
		return cuerpo;
	}*/
	public boolean isDerecha() {
		return derecha;
	}
	public float getDuracion() {
		return duracion;
	}
	/*public Vector2 getPosition() {
		return cuerpo.getPosition();
	}*/
	public Vector2 getSprPosition() {
		return animacion.getPosition();
	}
	public Vector2 getFuerza() {
		Vector2 fuerza = new Vector2(fuerzaX, fuerzaY); 
		return fuerza;
	}
	
	//--------------------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------SETTERS------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------------------------
	public void setSala(int sala) {
		this.sala = sala;
	}
	public void setDuracion(float duracion) {
		this.duracion = duracion;
	}
	public void setDerecha(boolean derecha) {
		this.derecha = derecha;
	}
	public void setFuerzaX(float fuerzaX) {
		this.fuerzaX = fuerzaX;
	}
	public void setFuerzaY(float fuerzaY) {
		this.fuerzaY = fuerzaY;
	}
	public void setPosition(float x, float y) {
		super.setPosition(x, y);
		setSprPosition(x, y);
	}
	public void setSprPosition(float x, float y) {
		animacion.setPosicion(x, y);
	}
}
