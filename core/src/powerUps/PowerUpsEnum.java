package powerUps;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.badlogic.gdx.physics.box2d.World;


public enum PowerUpsEnum {
	AumentoVelocidad("AumentoVelocidad","powerUps.AumentoVelocidad"),
	DobleSalto("DobleSalto","powerUps.DobleSalto"),
	Ralentizacion("Ralentizacion","powerUps.Ralentizacion");

	String nombre;
	String ruta;
	
	PowerUpsEnum(String nombre, String ruta) {
		this.nombre = nombre;
		this.ruta = ruta;
	}
	public String getNombre() {
		return nombre;
	}
	public PowerUp crearpowerUp(World mundo, float positionX, float positionY) {
		Class clase;
		PowerUp p = null;
		Constructor constructor;
		
		try {
			clase = Class.forName(this.ruta);
			constructor = clase.getConstructor(World.class, float.class, float.class);
			p = (PowerUp) constructor.newInstance(mundo, positionX, positionY); 
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return p;
	}
}
