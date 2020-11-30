package main;

import com.badlogic.gdx.Game;

import pantallas.PantallaMenu;
import utiles.Utiles;

public class Principal extends Game {
	@Override
	public void create () {
		Utiles.principal = this;
		Utiles.pantallaMenu = new PantallaMenu();
		this.setScreen(Utiles.pantallaMenu);
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
	}
}
