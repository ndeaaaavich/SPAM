package personajes;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Null;

import utiles.Global;
import utiles.Utiles;

import cuerpos.Cuerpo;
import eventos.InterfaceRobable;
import menu.Hud;

public class NPC extends Entidad implements InterfaceRobable{
	private int movimiento; // 1 arriba 2 abajo 3 izquierda 4 derecha 5-40 nada
	private boolean finRecorrido, detectado, CambioDirec, 
					robado = false, salaRobada = false, dibujarSigno = false, esperandoDialogo = false, mostrarPopUp,fin;
	private float tiempoMov, tiempo, tiempoDetectado;
	private Hud hud;
	private Sprite sprExclamation = new Sprite( new Texture("personajes/!.png") );
	//private Vector2 fuerzas = new Vector2(0,0);
	
	private InputListener mouseListener;
	int[] apariencia = new int[3]; 
	//0 pelo 
	//1 torso
	
	public NPC(Cuerpo cuerpo, String sprite, int[] apariencia, Hud hud) {
		super(cuerpo, sprite);
		this.hud = hud;
		this.apariencia = apariencia;
		setBounds(animacion.getSprite().getX(),
				  animacion.getSprite().getY(),
			      animacion.getSprite().getWidth(),
				  animacion.getSprite().getHeight());
		if(Global.guardia) {
			mouseListener = new InputListener() {
				public void enter (InputEvent event, float x, float y, int pointer, @Null Actor fromActor) {
					if(esperandoDialogo){
						dibujarSigno = true;
					}
				}
				public void exit (InputEvent event, float x, float y, int pointer, @Null Actor toActor) {
					if(esperandoDialogo){
						dibujarSigno = false;
					}
				}
				public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
					if(esperandoDialogo){
						mostrarPopUp = true;
					}
					return false;
				}
			};
	    addListener(mouseListener);
		}
		
        Utiles.addListener(this);
		cuerpo.setUserData(this);
	}
	// --------------------------------------------------------------------------------------------------------------------------------------
	// -------------------------------------------------------------ACCIONES-----------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------------------------
	public void detectarRobo() {
		setCambioDirec(true);
		setRobado(true);
		detectado = true;
	}
	
	@Override
	public void salaRobada(int sala) {
		if(getSala() == sala) {
			setRobado(true);
			salaRobada = true;
		}
	}
	
	// --------------------------------------------------------------------------------------------------------------------------------------
	// -------------------------------------------------------------SCENE 2D-----------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------------------------
	public void draw(Batch batch, float parentAlpha) {
		Color color = getColor();
		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
		animacion.getSprite().draw(batch);
		
		if(detectado){
			sprExclamation.setSize(16 * Utiles.PPM, 11 * Utiles.PPM);
			sprExclamation.setPosition(this.cuerpo.getPosition().x, this.cuerpo.getPosition().y + (cuerpo.getAlto() / 2));
			sprExclamation.draw(batch);
		}
		if(dibujarSigno){
			sprExclamation.setSize(16 * Utiles.PPM, 11 * Utiles.PPM);
			sprExclamation.setPosition(this.cuerpo.getPosition().x, this.cuerpo.getPosition().y + (cuerpo.getAlto() / 2));
			sprExclamation.draw(batch);
		}
	}
	public void act(float delta) {
		this.animacion.setPosicion(this.cuerpo.getPosition().x - (cuerpo.getAncho() / 2) ,
								   this.cuerpo.getPosition().y - (cuerpo.getAlto() / 2));
		this.animacion.setTexReg(animacion.getTexReg(0, delta));
		
		setPosition(cuerpo.getPosition().x,cuerpo.getPosition().y);	
		animacion.setTexReg(animacionMovimiento());
		tiempo += Gdx.graphics.getRawDeltaTime();
		duracion += delta;
		//duración es lo que dura la ida o la vuelta
		//tiempomov es lo que dura el total de los dos
		
		if(detectado) {
			tiempoDetectado += Gdx.graphics.getRawDeltaTime();
			if(tiempoDetectado > 5){
				tiempoDetectado = 0;
				detectado = false;
				setRobado(salaRobada);
			}
		}	
		cuerpo.setLinearVelocity(fuerzas.x, fuerzas.y);
		
		setBounds(animacion.getSprite().getX(),
				  animacion.getSprite().getY(),
			      animacion.getSprite().getWidth(),
				  animacion.getSprite().getHeight());
		
		if (mostrarPopUp) {
			hud.moverPopUp();
			mostrarPopUp = false;
		}
	}
	// --------------------------------------------------------------------------------------------------------------------------------------
	// -------------------------------------------------------------ANIMACION----------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------------------------
	public void setDireccion(Vector2 xy) {
		// 1 arriba 2 abajo 3 izquierda 4 derecha 5-40 nada
		fuerzas.set(xy);
		
		movimiento = (xy.x == 1)? 4: (xy.x == -1)? 3: 5;
		if(movimiento==5) movimiento = (xy.y == 1)? 1: (xy.y == -1)? 2: 5;

		animacion.setTexReg(animacionMovimiento());
	}
	
	@Override
	protected TextureRegion animacionMovimiento(){
		switch (movimiento) {
		case 1:
			setDerecha((ultimoIndice==0)?true:false);
			return animacion.getTexReg(ultimoIndice, duracion);
		case 2:
			setDerecha((ultimoIndice==0)?true:false);
			return animacion.getTexReg(ultimoIndice, duracion);
		case 3:
			if(finRecorrido) {
				setDerecha(true);
				ultimoIndice = 0;
				return animacion.getTexReg(0, duracion); 
			}else {
				setDerecha(false);
				ultimoIndice = 1;
				return animacion.getTexReg(1, duracion);		
			}
		case 4:
			if(finRecorrido) {
				setDerecha(false);
				ultimoIndice = 1;
				return animacion.getTexReg(1, duracion);
			}else {
				setDerecha(true);
				ultimoIndice = 0;
				return animacion.getTexReg(0, duracion);		
			}
		default:
			return animacion.getTexReg(2, duracion);
		}
	}
	// --------------------------------------------------------------------------------------------------------------------------------------
	// -------------------------------------------------------------GETTERS------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------------------------
	public boolean isRobado() {
		return robado;
	}
	public boolean isCambioDirec() {
		return CambioDirec;
	}
	public Vector2 getFuerza() {
		Vector2 fuerza = new Vector2(fuerzaX, fuerzaY); 
		return fuerza;
	}
	
	
	// --------------------------------------------------------------------------------------------------------------------------------------
	// -------------------------------------------------------------SETTERS------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------------------------
	public void setRobado(boolean robado) {
		this.robado = robado;
	}
	public void setPosicion(float x, float y) {
    	this.animacion.setPosicion(x,y);
    	this.cuerpo.setPosition(x,y);
	}
	public void setCambioDirec(boolean parar) {
		this.CambioDirec = parar;
		tiempo = 0;
	}
	public void setTiempoMov(float tiempoMov) {
		this.tiempoMov = tiempoMov;
	}
	public void setEsperandoDialogo(boolean esperandoDialogo) {
		if(Global.guardia) this.esperandoDialogo = esperandoDialogo;
	}

}

