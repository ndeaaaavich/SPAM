package cuerpos;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import utiles.Utiles;

public class Plataforma {
	
	private Cuerpo cuerpo;
	
	private Vector2 fuerzas = new Vector2(0,0);
	private Vector2 puntoLlegada, puntoSalida;
	
	private float duracion, tiempo;
	private boolean llegada = false;
	
	public Plataforma(World mundo, float ancho, float alto, float positionX, float positionY) {
		cuerpo = new Cuerpo(mundo, ancho, alto, BodyType.KinematicBody, positionX, positionY);
	}
	public void moverPlataforma(float delta) {
		tiempo += delta;
		if(!llegada) {
			fuerzas.x =(puntoSalida.x < puntoLlegada.x)?1 :(puntoSalida.x > puntoLlegada.x)? -1: 0;
			fuerzas.y =(puntoSalida.y < puntoLlegada.y)?1 :(puntoSalida.y > puntoLlegada.y)? -1: 0;
			if(tiempo > duracion) {
				llegada = true;
				tiempo = 0;
			}
		}else{
			fuerzas.x =(puntoSalida.x > puntoLlegada.x)?1 :(puntoSalida.x < puntoLlegada.x)? -1: 0;
			fuerzas.y =(puntoSalida.y > puntoLlegada.y)?1 :(puntoSalida.y < puntoLlegada.y)? -1: 0;
			if(tiempo > duracion) {
				llegada = false;
				tiempo = 0;
			}
		}
		cuerpo.setLinearVelocity(fuerzas.x, fuerzas.y);
	}
	public void setMovimiento(float duracion, Vector2 puntoSalida, Vector2 puntoLlegada) {
		this.duracion = duracion;
		setPuntoLlegada(puntoLlegada.scl(Utiles.PPM));
		setPuntoSalida(puntoSalida.scl(Utiles.PPM));
	}
	private void setPuntoLlegada(Vector2 puntoLlegada) {
		this.puntoLlegada = puntoLlegada;
	}
	private void setPuntoSalida(Vector2 puntoSalida) {
		this.puntoSalida = puntoSalida;
	}
}
