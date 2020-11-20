package powerUps;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import cuerpos.Cuerpo;
import utiles.Utiles;

public class PowerUp extends Actor{
	
	private Cuerpo cuerpo;
	protected Sprite spr;
	
	private int numeroPowerUp;
	private float efecto, coolDown = 5;
	private boolean activo = true;
	
	public PowerUp(World mundo, float positionX, float positionY, int numeroPowerUp, String fileName) {
		cuerpo = new Cuerpo(mundo, 12, 12, BodyType.StaticBody, positionX, positionY);
		setNumeroPowerUp(numeroPowerUp);
		cuerpo.setUserData(this);
		
		spr = new Sprite(new Texture(fileName));
		spr.setSize(cuerpo.getAncho(), cuerpo.getAlto());
		spr.setPosition(cuerpo.getPosition().x - (cuerpo.getAncho()/2)
					  , cuerpo.getPosition().y - (cuerpo.getAlto()/2) );
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
		cuerpo.setArea(numeroPowerUp);
	}
	public int getNumeroPowerUp() {
		return numeroPowerUp;
	}
	public float getEfecto() {
		return efecto;
	}
	private Body getCuerpo() {
		return cuerpo.getBodyReferencia();
	}
	public boolean isActivo() {
		return activo;
	}
	public float getCoolDown() {
		return coolDown;
	}
	
	protected void setEfecto(float efecto) {
		this.efecto = efecto;
	}
	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	public void setWorldActive(boolean flag) {
		getCuerpo().setActive(flag);
	}
}
