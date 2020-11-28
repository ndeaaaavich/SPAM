

package personajes;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Null;

import utiles.Global;
import utiles.Utiles;

import eventos.InterfaceRobable;
import menu.Hud;

public class NPC extends Entidad implements InterfaceRobable{
	private boolean detectado, CambioDirec, 
					robado = false, salaRobada = false, dibujarSigno = false, esperandoDialogo = false, mostrarPopUp,fin;
	private float tiempoDetectado;
	private Hud hud;
	private Sprite sprExclamation = new Sprite( new Texture("personajes/!.png") );
	//private Vector2 fuerzas = new Vector2(0,0);
	
	private InputListener mouseListener;
	int[] apariencia = new int[3]; 
	//0 pelo 
	//1 torso
	
	public NPC(String sprite, int[] apariencia, Hud hud, int sala) {
		super(sprite);
		this.hud = hud;
		this.apariencia = apariencia;
		this.sala = sala;
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
		setUserData(this);
	}
	// --------------------------------------------------------------------------------------------------------------------------------------
	// -------------------------------------------------------------ACCIONES-----------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------------------------
	public void detectarRobo() {
		//setCambioDirec(true);
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
			sprExclamation.setPosition(this.animacion.getPosition().x + (animacion.getAncho()/2), this.animacion.getPosition().y + animacion.getAlto());
			sprExclamation.draw(batch);
		}
		if(dibujarSigno){
			sprExclamation.setSize(16 * Utiles.PPM, 11 * Utiles.PPM);
			sprExclamation.setPosition(this.animacion.getPosition().x + (animacion.getAncho()/2), this.animacion.getPosition().y + animacion.getAlto());
			sprExclamation.draw(batch);
		}
	}
	public void act(float delta) {
		this.animacion.setTexReg(animacion.getTexReg(0, delta));
		
		animacion.setTexReg(animacionMovimiento());
		
		//tiempo += Gdx.graphics.getRawDeltaTime();
		
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
		
		setBounds(animacion.getSprite().getX(),
				  animacion.getSprite().getY(),
			      animacion.getSprite().getWidth(),
				  animacion.getSprite().getHeight());
		
		if (mostrarPopUp) {
			hud.moverPopUp(true);
			mostrarPopUp = false;
		}
	}
	// --------------------------------------------------------------------------------------------------------------------------------------
	// -------------------------------------------------------------ANIMACION----------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------------------------	
	@Override
	protected TextureRegion animacionMovimiento(){
		if(estado == EstadoMovimiento.corriendoDerecha) {// moverse a la derecha
			setDerecha(true);
			ultimoIndice = 0;
			return animacion.getTexReg(0, duracion);
		}
		if(estado == EstadoMovimiento.corriendoIzquierda) {// moverse a la izquierda
			setDerecha(false);
			ultimoIndice = 1;
			return animacion.getTexReg(1, duracion);
		}
		if(estado == EstadoMovimiento.movimientoY) {
			setDerecha((ultimoIndice==0)?true:false);
			return animacion.getTexReg(ultimoIndice, duracion);
		}
		return animacion.getTexReg((ultimoIndice==1)?3:2, duracion);
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
	// --------------------------------------------------------------------------------------------------------------------------------------
	// -------------------------------------------------------------SETTERS------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------------------------
	public void setRobado(boolean robado) {
		this.robado = robado;
	}
	public void setPosicion(float x, float y) {
    	animacion.setPosicion(x,y);
	}
	/*public void setCambioDirec(boolean parar) {
		this.CambioDirec = parar;
		tiempo = 0;
	}
	public void setTiempoMov(float tiempoMov) {
		this.tiempoMov = tiempoMov;
	}*/
	public void setEsperandoDialogo(boolean esperandoDialogo) {
		if(Global.guardia) this.esperandoDialogo = esperandoDialogo;
	}
	public void setSalaRobada(boolean salaRobada) {
		this.salaRobada = salaRobada;
	}
}

