package powerUps;

import com.badlogic.gdx.physics.box2d.World;

public class AumentoVelocidad extends PowerUp {

	public AumentoVelocidad(World mundo, float positionX, float positionY) {
		super(mundo, positionX, positionY, 1, "personajes/9.png");
		super.setEfecto(0.5f);
	}
	
}
