package powerUps;

import com.badlogic.gdx.physics.box2d.World;

public class DobleSalto extends PowerUp {

	public DobleSalto(World mundo, float positionX, float positionY) {
		super(mundo, positionX, positionY, 3, "personajes/badlogic.jpg");
		super.setEfecto(1);
	}

}
