package pantallas;

import utiles.Global;
import utiles.Render;
import utiles.Utiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import powerUps.*;
import cuerpos.Plataforma;
import personajes.Guardia;
import personajes.Ladron;
import personajes.SpriteInfo;

public class PantallaRonda2 extends PantallaRonda {

	private PowerUp[] powerUp;
	private Plataforma[] plataformaMovil;
	private float tiempo;

	public PantallaRonda2(Vector2 gravedad, String rutaMapa) {
		super(gravedad, rutaMapa);
	}

	@Override
	public void show() {
		plataformaMovil = new Plataforma[mapa.getPlataformasInicioPosition().length];
		powerUp = new PowerUp[mapa.getPowerUps().length];
		crearjugadores();
		jugadorGuardia.setPosition(100, 150);
		jugadorLadron.setPosition(100, 150);
		crearPlataformas();
		crearPowerUps();
		Utiles.hc.setApp(this);
	}

	@Override
	public void render(float delta) {
		if(Global.ronda==2) {
			super.render(delta);
			Render.limpiarPantalla();
	
			update(delta);
			tiempo += Gdx.graphics.getRawDeltaTime();
			tmr.setView(camera);
			tmr.render();
			b2dr.render(mundo, camera.combined);
	
			stage.act();
			stage.draw();
	
			Render.batch.setProjectionMatrix(camera.combined);
			Gdx.input.setInputProcessor(stage);
			for (int i = 0; i < plataformaMovil.length; i++) {
				plataformaMovil[i].moverPlataforma(delta);	
			}
		}else {
			
		}
	}

	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	private void update(float delta) {
		mundo.step(1 / 60f, 6, 2);

		for (int i = 0; i < powerUp.length; i++) {
			if (!powerUp[i].isActivo()) {
				powerUp[i].setWorldActive(false);
			}
			if (!powerUp[i].isActivo() && (int) (tiempo % powerUp[i].getCoolDown()) == 0) {
				powerUp[i].setActivo(true);
				powerUp[i].setWorldActive(true);
			}
		}
		camera.update();
	}

	private void crearjugadores() {
		this.jugadorLadron = new Ladron(SpriteInfo.values()[SpriteInfo.getIndiceLadron()].getFilename());
		stage.addActor(this.jugadorLadron);
		this.jugadorGuardia = new Guardia("personajes/badlogic.jpg");
		stage.addActor(this.jugadorGuardia);
		stage.setKeyboardFocus( (Global.guardia)? jugadorGuardia : jugadorLadron );
	}
	private void crearPlataformas() {
		float[] duracion = new float[] {2f, 2f};
		for (int i = 0; i < plataformaMovil.length; i++) {
			plataformaMovil[i] = new Plataforma(mundo, mapa.getPlataformasSize()[i].x, mapa.getPlataformasSize()[i].y
											   ,mapa.getPlataformasInicioPosition()[i].x, mapa.getPlataformasInicioPosition()[i].y);
			plataformaMovil[i].setMovimiento(duracion[i], mapa.getPlataformasInicioPosition()[i], 
														  mapa.getPlataformasFinalPosition()[i]);
		}
	}
	private void crearPowerUps() {
		for (int i = 0; i < powerUp.length; i++) {
			for (int j = 0; j < PowerUpsEnum.values().length; j++) {
				if(mapa.getPowerUps()[i].equals(PowerUpsEnum.values()[j].getNombre())) {
					powerUp[i] = PowerUpsEnum.values()[j].crearpowerUp(mundo, mapa.getPowerUpsPosition()[i].x, 
																			  mapa.getPowerUpsPosition()[i].y);
					stage.addActor(powerUp[i]);
				}
			}
		}
	}
}
