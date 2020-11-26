package powerUps;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

import utiles.Utiles;

public class PowerUp extends Actor{
	
	protected Sprite spr;
	
	private int numeroPowerUp;
	private float efecto;
	private boolean activo = true;
	
	public PowerUp(float positionX, float positionY, int numeroPowerUp, String fileName) {
		setNumeroPowerUp(numeroPowerUp);
		
		spr = new Sprite(new Texture(fileName));
		spr.setSize(12 * Utiles.PPM, 12 * Utiles.PPM);
		spr.setPosition(positionX * Utiles.PPM
					  , positionY * Utiles.PPM);
	}
	
	public void draw(Batch batch, float parentAlpha){
		Color color = getColor();
	    batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
	    if(activo) {
	    	spr.draw(batch);
	    }
	}
	
	//usamos el setArea para saber cuando los players pasan por arriba de cada power up
	private void setNumeroPowerUp(int numeroPowerUp) {
		this.numeroPowerUp = numeroPowerUp;
	}
	public int getNumeroPowerUp() {
		return numeroPowerUp;
	}
	public float getEfecto() {
		return efecto;
	}
	public boolean isActivo() {
		return activo;
	}

	
	protected void setEfecto(float efecto) {
		this.efecto = efecto;
	}
	public void setActivo(boolean activo) {
		this.activo = activo;
	}
}
