package pantallas;

import utiles.Global;
import utiles.Render;
import utiles.Utiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import powerUps.*;
import personajes.Guardia;
import personajes.Ladron;
import personajes.SpriteInfo;

public class PantallaRonda2 extends PantallaRonda {

	public PowerUp[] powerUp;
	public Sprite[] plataformaMovilSprite;
	private float tiempo;

	public PantallaRonda2(Vector2 gravedad, String rutaMapa) {
		super(gravedad, rutaMapa);
	}

	@Override
	public void show() {
		plataformaMovilSprite = new Sprite[mapa.getPlataformasInicioPosition().length];
		powerUp = new PowerUp[mapa.getPowerUps().length];
		crearjugadores();
		crearPlataformas();
		crearPowerUps();
		Utiles.hc.setApp(this);
	}

	@Override
	public void render(float delta) {
		Render.limpiarPantalla();
		
		if(Global.ronda==2) {
			super.render(delta);
	
			update(delta);
			tiempo += Gdx.graphics.getRawDeltaTime();
			tmr.setView(camera);
			tmr.render();
			b2dr.render(mundo, camera.combined);
			
			
			Render.batch.begin();
			for (int i = 0; i < plataformaMovilSprite.length; i++) {
				plataformaMovilSprite[i].draw(Render.batch);
			}
			Render.batch.end();
			
			stage.act();
			stage.draw();
	
			Render.batch.setProjectionMatrix(camera.combined);
			Gdx.input.setInputProcessor(stage);
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
		
		camera.update();
	}

	private void crearjugadores() {
		//VER ESTO
		this.jugadorLadron = new Ladron(SpriteInfo.values()[SpriteInfo.getIndiceLadron()].getFilename(), 0, 0, 0);
		stage.addActor(this.jugadorLadron);
		this.jugadorGuardia = new Guardia("personajes/guardia.png");
		stage.addActor(this.jugadorGuardia);
		stage.setKeyboardFocus( (Global.guardia)? jugadorGuardia : jugadorLadron );
	}
	private void crearPlataformas() {
		for (int i = 0; i < plataformaMovilSprite.length; i++) {
			plataformaMovilSprite[i] = new Sprite(new Texture("botones/boton 1.png"));
			plataformaMovilSprite[i].setSize(mapa.getPlataformasSize()[i].x * Utiles.PPM, mapa.getPlataformasSize()[i].y* Utiles.PPM);
		}
	}
	private void crearPowerUps() {
		for (int i = 0; i < powerUp.length; i++) {
			for (int j = 0; j < PowerUpsEnum.values().length; j++) {
				if(mapa.getPowerUps()[i].equals(PowerUpsEnum.values()[j].getNombre())) {
					powerUp[i] = PowerUpsEnum.values()[j].crearpowerUp(mapa.getPowerUpsPosition()[i].x, 
																	   mapa.getPowerUpsPosition()[i].y);
					stage.addActor(powerUp[i]);
				}
			}
		}
	}
}
