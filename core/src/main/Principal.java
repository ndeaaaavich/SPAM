package main;

import com.badlogic.gdx.Game;

import pantallas.PantallaMenu;
import utiles.Utiles;

public class Principal extends Game {
	/*PantallaRonda ronda1 = new PantallaRonda1(new Vector2(0, 0), "mapas/escenario.tmx"),
				  ronda2 = new PantallaRonda2(null, null);*/
	
	@Override
	public void create () {
		Utiles.principal = this;
		//ronda2.setHc(ronda1.getHc());
		this.setScreen(new PantallaMenu());
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
	}
}
