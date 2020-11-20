package powerUps;

import com.badlogic.gdx.physics.box2d.World;

public class Ralentizacion extends PowerUp {

	public Ralentizacion(World mundo, float positionX, float positionY) {
		super(mundo, positionX, positionY, 2, "personajes/exclamacion-removebg-preview.png");
		super.setEfecto(-0.2f);
	}

}
